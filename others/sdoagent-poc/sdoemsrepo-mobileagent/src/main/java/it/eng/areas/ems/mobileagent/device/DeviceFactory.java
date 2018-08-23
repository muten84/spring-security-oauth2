/**
 * 
 */
package it.eng.areas.ems.mobileagent.device;

import java.util.Calendar;

import org.pmw.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.device.battery.BatteryConfiguration;
import it.eng.areas.ems.mobileagent.device.battery.BatteryData;
import it.eng.areas.ems.mobileagent.device.battery.BatteryObserver;
import it.eng.areas.ems.mobileagent.device.gps.GPSData;
import it.eng.areas.ems.mobileagent.device.gps.GpsConfiguration;
import it.eng.areas.ems.mobileagent.device.gps.GpsObserver;
import it.eng.areas.ems.mobileagent.device.idle.IdleListener;
import it.eng.areas.ems.mobileagent.http.WsHandler;
import it.eng.areas.ems.mobileagent.jnative.win32.Win32Device;
import it.eng.areas.ems.mobileagent.jnative.win32.Win32IdleTime;
import it.eng.areas.ems.mobileagent.message.MessageReceiver;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DeviceConfiguration;
import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;

/**
 * @author Bifulco Luigi
 *
 */
public class DeviceFactory {

	private static DeviceFactory INSTANCE;
	private IBattery batteryService;
	private IGps gpsService;
	private IAudio audioService;
	private IIdleMonitor idleService;
	private IDisplay display;

	private DeviceFactory() {

	}

	public static synchronized DeviceFactory get() {
		if (INSTANCE == null) {
			INSTANCE = new DeviceFactory();
		}
		return INSTANCE;
	}

	public void destroyDevice() {
		if (this.idleService != null)
			this.idleService.stop();
		if (this.batteryService != null)
			this.batteryService.shutdown();
		if (this.gpsService != null)
			this.gpsService.shutdown();
		if (this.audioService != null)
			this.audioService.shutdown();
		if (this.display != null)
			this.display.stop();

		this.idleService = null;
		this.batteryService = null;
		this.gpsService = null;
		this.audioService = null;
		this.display = null;
		INSTANCE = null;
	}

	public void createDevice(DeviceConfiguration conf, JsonMapper mapper) {
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
							Logger.error(e, "error while processing battery data");
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
							Logger.error(e, "error while processing battery data");
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
							Logger.error(e, "error while processing battery data");
						}

					}
				});
			}
		}
		if (this.audioService == null) {
			audioService = Win32Device.createAudioService(conf.getBeepSoundLocation());
		}
		if (this.idleService == null) {
			idleService = new IIdleMonitor() {

				@Override
				public void stop() {
					Win32IdleTime.stop();

				}

				@Override
				public void start(int idleTime, int awayTime, long checkFreq) {
					Win32IdleTime.start(idleTime, awayTime, checkFreq, new IdleListener() {

						@Override
						public void onOnline() {
							System.out.println("Win32IdleTime: onOnline");
							Message m = new Message();
							m.setFrom("DEVICE");
							m.setPayload("{\"status\": \"ONLINE\"}");
							m.setTimestamp(Calendar.getInstance().getTimeInMillis());
							m.setType("DEVICE_IDLE_STATUS");
							try {
								WsHandler.sendEvent(mapper.stringify(m));
							} catch (JsonProcessingException e) {
								Logger.error(e, "JsonProcessingException onOnline");
							}

						}

						@Override
						public void onIdle() {
							System.out.println("Win32IdleTime: onIdle");
							Message m = new Message();
							m.setFrom("DEVICE");
							m.setPayload("{\"status\": \"IDLE\"}");
							m.setTimestamp(Calendar.getInstance().getTimeInMillis());
							m.setType("DEVICE_IDLE_STATUS");
							try {
								WsHandler.sendEvent(mapper.stringify(m));
							} catch (JsonProcessingException e) {
								Logger.error(e, "JsonProcessingException onIdle");
							}

						}

						@Override
						public void onAway() {
							System.out.println("Win32IdleTime: onAway");
							Message m = new Message();
							m.setFrom("DEVICE");
							m.setPayload("{\"status\": \"AWAY\"}");
							m.setTimestamp(Calendar.getInstance().getTimeInMillis());
							m.setType("DEVICE_IDLE_STATUS");
							try {
								WsHandler.sendEvent(mapper.stringify(m));
							} catch (JsonProcessingException e) {
								Logger.error(e, "JsonProcessingException onAway");
							}

						}
					});

				}
			};

			idleService.start(conf.getIdleTime(), conf.getAwayTime(), conf.getIdleMonitorFrequencyCheck());
		}
		if (display == null) {
			display = Win32Device.createDisplay();
		}

	}

	public void sendDeviceStatus(JsonMapper mapper, MessageReceiver receiver) throws JsonProcessingException {
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
		if (receiver != null) {
			boolean connected = receiver.isConnected();
			Message m = new Message();
			m.setFrom("DEVICE");
			m.setPayload("{\"connected\": " + connected + "}");
			m.setTimestamp(Calendar.getInstance().getTimeInMillis());
			m.setType("DEVICE_CONNECTION_STATUS");
			WsHandler.sendEvent(mapper.stringify(m));
		}

	}

	/**
	 * @return the batteryService
	 */
	public IBattery getBatteryService() {
		return batteryService;
	}

	/**
	 * @return the gpsService
	 */
	public IGps getGpsService() {
		return gpsService;
	}

	/**
	 * @return the audioService
	 */
	public IAudio getAudioService() {
		return audioService;
	}

	/**
	 * @return the idleService
	 */
	public IIdleMonitor getIdleService() {
		return idleService;
	}

	/**
	 * @return the display
	 */
	public IDisplay getDisplay() {
		return display;
	}

}
