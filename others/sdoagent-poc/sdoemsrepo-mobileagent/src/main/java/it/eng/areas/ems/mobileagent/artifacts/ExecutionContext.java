/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.knowm.sundial.SundialJobScheduler;
import org.pmw.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.delegate.impl.ArtifactsManagerImpl;
import it.eng.areas.ems.mobileagent.artifacts.job.CheckAndDownloadJob;
import it.eng.areas.ems.mobileagent.artifacts.job.MultipleArtifactsJob;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerService;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService.AgentState;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import it.eng.areas.ems.mobileagent.artifacts.service.LocalArtifactsService;
import it.eng.areas.ems.mobileagent.artifacts.util.DimmerUtils;
import it.eng.areas.ems.mobileagent.device.DeviceInfoUtil;
import it.eng.areas.ems.mobileagent.device.IAudio;
import it.eng.areas.ems.mobileagent.device.IBattery;
import it.eng.areas.ems.mobileagent.device.IGps;
import it.eng.areas.ems.mobileagent.device.battery.BatteryConfiguration;
import it.eng.areas.ems.mobileagent.device.battery.BatteryData;
import it.eng.areas.ems.mobileagent.device.battery.BatteryObserver;
import it.eng.areas.ems.mobileagent.device.gps.GPSData;
import it.eng.areas.ems.mobileagent.device.gps.GpsConfiguration;
import it.eng.areas.ems.mobileagent.device.gps.GpsObserver;
import it.eng.areas.ems.mobileagent.http.WsHandler;
import it.eng.areas.ems.mobileagent.jnative.win32.Win32Device;
import it.eng.areas.ems.mobileagent.message.MessageProcessor;
import it.eng.areas.ems.mobileagent.message.MessageReceiver;
import it.eng.areas.ems.mobileagent.message.MessageService;
import it.eng.areas.ems.mobileagent.message.job.MessageReceiverJob;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DeviceConfiguration;
import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;

/**
 * @author Bifulco Luigi
 *
 */
public class ExecutionContext {

	public static enum LockDeviceState {
		LOCKED, UNLOCKED
	}

	private LockDeviceState LOCK_STATE = LockDeviceState.UNLOCKED;
	private ArtifactsManager manager;
	private ArtifactsManagerService service;
	// private String apiBaseUrl = "http://127.0.0.1:8080/artifacts-manager/";
	private String groupId = "NACI";
	private String artifactId = "terminal";
	private String localStorePath = "/esel/terminal/download";
	private JsonStoreService storeService;
	private LocalArtifactsService localArtifactService;
	private JsonMapper mapper;
	private ArtifactsManagerStateService stateService;
	private int serverPort;
	private DimmerUtils dimmer;
	private MessageService messageService;
	private MessageReceiver messageReceiver;
	private WsHandler wsHandler;
	private String apiBaseUrl;
	private IGps gpsService;
	private IBattery batteryService;
	private IAudio audioService;

	public ExecutionContext() {

	}

	public ExecutionContext createExecutionContext(String apiBaseUrl, String mqttURl, String groupId, String artifactId,
			String localStorePath, int serverPort) {
		this.stateService = new ArtifactsManagerStateService(AgentState.START);
		this.apiBaseUrl = apiBaseUrl;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.localStorePath = localStorePath;
		this.mapper = new JsonMapper();
		this.serverPort = serverPort;
		// this.dimmer = new DimmerUtils(this);

		Logger.info("check localStorePath: " + checkLocalStorePath());

		/**/
		service = new ArtifactsManagerService(apiBaseUrl, "api/rest/artifacts");

		// messageService = new MessageService(processor, apiBaseUrl,
		// "api/rest/message");
		storeService = new JsonStoreService(localStorePath, this.groupId, this.mapper);
		manager = new ArtifactsManagerImpl(service, storeService);
		localArtifactService = new LocalArtifactsService(localStorePath, storeService, this.mapper);

		// capire come creare l'agentrestservice AgentRestService.create(this);

		/* for backwar compatibility purpose mqtt url is optional */
		// if (mqttURl != null) {
		// String[] mqtt = mqttURl.split(":");
		// messageReceiver = new MessageReceiver(deviceId, mqtt[0],
		// Integer.parseInt(mqtt[1]), 5000);
		// try {
		// messageReceiver.init(5000);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		return this;

	}

	public MessageService initMessageService(String messageServiceUrl) {
		if (this.messageService != null) {
			return this.messageService;
		}

		MessageProcessor processor = new MessageProcessor(30000);
		// this.messageService = new MessageService(processor, apiUrl,
		// endpoint!=null?endpoint:"api/rest/message");
		this.messageService = new MessageService(processor, messageServiceUrl);
		return this.messageService;
	}

	public boolean shutdownMessageService() {
		if (this.messageService != null) {
			this.messageService.closeMessageService();
			this.messageService = null;

		}
		return true;
	}

	public boolean shutdownMessageReceiver() {
		if (this.messageReceiver != null) {
			SundialJobScheduler.removeTrigger("MessageReceiverTrigger");
			SundialJobScheduler.removeJob("MessageReceiverJob");
			this.messageReceiver.shutdown();
			this.messageReceiver = null;
		}
		return true;
	}

	public MessageService reinitMessageService(String messageServiceUrl) {
		if (this.messageService != null) {
			this.messageService.closeMessageService();
			this.messageService = null;

		}
		MessageProcessor processor = new MessageProcessor(30000);
		this.messageService = new MessageService(processor, messageServiceUrl);
		return this.messageService;
	}

	public MessageReceiver initMessageReceiver(String mqttURl, int qosLevel, int mqttKeepAlive, long connectAttemptMax,
			long reconnectAttemptsMax, long reconnectDelay, long reconnectDelayMax, boolean fromReset) {
		if (this.messageReceiver != null) {
			return this.messageReceiver;
		}
		String deviceId = DeviceInfoUtil.createDeviceInfo("").getDeviceId();
		String[] mqtt = mqttURl.split(":");
		messageReceiver = new MessageReceiver(deviceId, mqtt[0], Integer.parseInt(mqtt[1]), 30000, qosLevel);
		try {
			messageReceiver.init(5000, mqttKeepAlive, connectAttemptMax, reconnectAttemptsMax, reconnectDelay,
					reconnectDelayMax,1);
		} catch (Exception e) {
			Logger.error(e,"Exception in initMessageReceiver");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ExecutionContext", ExecutionContext.this);
		// if (!fromReset) {
//		SundialJobScheduler.addJob("MessageReceiverJob", MessageReceiverJob.class, params, false);
//		SundialJobScheduler.addSimpleTrigger("MessageReceiverTrigger", "MessageReceiverJob", -1, 1);
		// }
		return this.messageReceiver;
	}

	protected boolean checkLocalStorePath() {
		File f = new File(this.localStorePath);
		if (!f.exists()) {
			return f.mkdirs();
		}
		return true;
	}

	public CheckAndDownloadJob createCheckAndDownloadJob() {
		// return new CheckAndDownloadJob(this, this.storeService, this.manager,
		// this.groupId, this.artifactId,
		// this.localStorePath);
		return null;
	}

	public MultipleArtifactsJob createMultipleArtifactsJob() {
		// return new MultipleArtifactsJob(this, this.groupId);
		return null;
	}

	public CheckAndDownloadJob createCheckAndDownloadJob(String groupId, String artifactId, String localStorePath) {
		// return new CheckAndDownloadJob(this, this.storeService, this.manager,
		// groupId, artifactId, localStorePath);
		return null;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public JsonMapper getJsonMapper() {
		return this.mapper;
	}

	public ArtifactsManagerStateService getArtifactsManagerStateService() {
		return this.stateService;
	}

	public void setLockState(LockDeviceState newState) {
		this.LOCK_STATE = newState;
	}

	public LockDeviceState getLockState() {
		return LOCK_STATE;
	}

	/**
	 * @return the manager
	 */
	public ArtifactsManager getArtifactManager() {
		return manager;
	}

	/**
	 * @return the service
	 */
	public ArtifactsManagerService getArtifactsManagerService() {
		return service;
	}

	/**
	 * @return the storeService
	 */
	public JsonStoreService getStoreService() {
		return storeService;
	}

	public LocalArtifactsService getLocalArtifactsService() {

		return localArtifactService;

	}

	public String getLocalStorePath() {
		return this.localStorePath;
	}

	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort
	 *            the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return the dimmer
	 */
	public DimmerUtils getDimmer() {
		return dimmer;
	}

	public MessageService getMessageService() {
		return this.messageService;
	}

	public MessageReceiver getMessageReceiver() {
		return this.messageReceiver;
	}

	/**
	 * @param wsHandler
	 */
	public void setWsHandler(WsHandler wsHandler) {
		this.wsHandler = wsHandler;

	}

	public WsHandler getWsHandler() {
		return this.wsHandler;
	}

	/**
	 * @return the apiBaseUrl
	 */
	public String getApiBaseUrl() {
		return apiBaseUrl;
	}

	public IBattery getBatteryService() {
		return this.batteryService;
	}

	public IGps getGpsService() {
		return this.gpsService;
	}

	public IAudio getAudioService() {
		return this.audioService;
	}

	public void sendDeviceStatus() throws JsonProcessingException {
		if (this.batteryService != null) {
			BatteryData data = new BatteryData();
			data.setAcConnected(this.batteryService.getBatteryAcConnected());
			data.setLifePercent(this.batteryService.getBatteryLevelPercent());
			Message m = new Message();
			m.setFrom("DEVICE");
			m.setPayload(mapper.stringify(data));
			m.setTimestamp(Calendar.getInstance().getTimeInMillis());
			m.setType("DEVICE_BATTERY");
			WsHandler.sendEvent(mapper.stringify(m));
		}
		/* FIXME: commentato per problema su invio dello stato */
		if (this.gpsService != null) {
			short status = this.gpsService.getStatus();
			Message m = new Message();
			m.setFrom("DEVICE");
			m.setPayload("{\"gpsStatus\": " + status + "}");
			m.setTimestamp(Calendar.getInstance().getTimeInMillis());
			m.setType("DEVICE_GPS_STATUS");
			WsHandler.sendEvent(mapper.stringify(m));
		}
		if (this.messageReceiver != null) {
			boolean connected = this.messageReceiver.isConnected();
			Message m = new Message();
			m.setFrom("DEVICE");
			m.setPayload("{\"connected\": " + connected + "}");
			m.setTimestamp(Calendar.getInstance().getTimeInMillis());
			m.setType("DEVICE_CONNECTION_STATUS");
			WsHandler.sendEvent(mapper.stringify(m));
		}

	}

	public void resetDeviceConfiguration() {
		this.batteryService.shutdown();
		this.gpsService.shutdown();
		this.audioService.shutdown();
		this.batteryService = null;
		this.gpsService = null;
	}

	/**
	 * @param conf
	 */
	public void createDevice(DeviceConfiguration conf) {
		if (this.batteryService == null) {
			if (conf.getOsType().equalsIgnoreCase("win32")) {
				// battery
				BatteryConfiguration batConf = new BatteryConfiguration();
				batConf.setInteralPoller(true);
				batConf.setReadOffsetInSeconds(conf.getBatteryReadOffsetInSeconds());
				this.batteryService = Win32Device.createBatteryService(batConf, new BatteryObserver() {

					@Override
					public void onBatteryData(BatteryData data) {
						try {
							Message m = new Message();
							m.setFrom("DEVICE");
							m.setPayload(mapper.stringify(data));
							m.setTimestamp(Calendar.getInstance().getTimeInMillis());
							m.setType("DEVICE_BATTERY");
							WsHandler.sendEvent(mapper.stringify(m));
						} catch (Exception e) {
							Logger.error(e,"error while processing battery data");
						}

					}
				});
			}
		}
		if (this.gpsService == null) {
			if (conf.getOsType().equalsIgnoreCase("win32")) {
				GpsConfiguration gpsConf = new GpsConfiguration();
				gpsConf.setAutoPortScan(conf.isGpsAutoPortScan());
				gpsConf.setBaudRate(conf.getGpsBaudRate());
				gpsConf.setPort(conf.getGpsPort());
				gpsConf.setReadFrequencyInMillis(conf.getGpsReadFrequencyInMillis());
				this.gpsService = Win32Device.createGpsService(gpsConf, new GpsObserver() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see it.eng.areas.ems.mobileagent.device.gps.GpsObserver#onGpsStatus(short)
					 */
					@Override
					public void onGpsStatus(short status) {
						try {
							Message m = new Message();
							m.setFrom("DEVICE");
							m.setPayload("{\"gpsStatus\": " + status + "}");
							m.setTimestamp(Calendar.getInstance().getTimeInMillis());
							m.setType("DEVICE_GPS_STATUS");
							WsHandler.sendEvent(mapper.stringify(m));
						} catch (Exception e) {
							Logger.error(e,"error while processing onGpsStatus data");
						}

					}

					@Override
					public void onGpsData(GPSData data) {
						try {
							Message m = new Message();
							m.setFrom("DEVICE");
							m.setPayload(mapper.stringify(data));
							m.setTimestamp(Calendar.getInstance().getTimeInMillis());
							m.setType("DEVICE_GPS_COORD");
							WsHandler.sendEvent(mapper.stringify(m));
						} catch (Exception e) {
							Logger.error(e,"error while processing onGpsData data");
						}

					}
				});
			}
		}
		if (this.audioService == null) {
			audioService = Win32Device.createAudioService(conf.getBeepSoundLocation());
		}

	}

}
