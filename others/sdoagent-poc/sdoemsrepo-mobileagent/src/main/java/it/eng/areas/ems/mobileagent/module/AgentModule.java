/**
 * 
 */
package it.eng.areas.ems.mobileagent.module;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import org.pmw.tinylog.Logger;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.delegate.impl.ArtifactsManagerImpl;
import it.eng.areas.ems.mobileagent.artifacts.job.CheckDownloadedArtifacts;
import it.eng.areas.ems.mobileagent.artifacts.job.MultipleArtifactsJob;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerService;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService.AgentState;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import it.eng.areas.ems.mobileagent.artifacts.service.LocalArtifactsService;
import it.eng.areas.ems.mobileagent.artifacts.util.DimmerUtils;
import it.eng.areas.ems.mobileagent.db.DBNotFoundException;
import it.eng.areas.ems.mobileagent.db.PerstLookupFactory;
import it.eng.areas.ems.mobileagent.device.DeviceInfoUtil;
import it.eng.areas.ems.mobileagent.message.MessageReceiver;
import it.eng.areas.ems.mobileagent.message.MessageService;
import it.eng.areas.ems.mobileagent.message.db.DocumentStore;
import okhttp3.OkHttpClient;

/**
 * @author Bifulco Luigi
 *
 */

@Module
public class AgentModule {

	@Provides
	public PerstLookupFactory provideLocalDbFactory() {
		String path = System.getProperty("agent.workingDir") + "/db/perst.dbs";
		Logger.info("try to open db from: " + path);
		File f = new File(path);
		if (!f.exists()) {
			throw new RuntimeException("Db file does not exist: " + path);
		}
		try {
			return new PerstLookupFactory(path);
		} catch (DBNotFoundException e) {
			Logger.error(e,"Error during provideLocalDbFactory");
			throw new RuntimeException(e);
		}
	}

	@Provides
	public OkHttpClient provideHttpClient(
			@Named("agent.message.outbound.connectTimeoutInMillis") String connectTimeoutInMillis, //
			@Named("agent.message.outbound.readTimeoutInMillis") String readTimeoutInMillis, //
			@Named("agent.message.outbound.writeTimeoutInMillis") String writeTimeoutInMillis//
	) {
		OkHttpClient client = new OkHttpClient();

		return client.newBuilder().connectTimeout(Long.parseLong(connectTimeoutInMillis), TimeUnit.MILLISECONDS)
				.readTimeout(Long.parseLong(readTimeoutInMillis), TimeUnit.MILLISECONDS)
				.writeTimeout(Long.parseLong(writeTimeoutInMillis), TimeUnit.MILLISECONDS).build();

	}

	@Provides
	public CheckDownloadedArtifacts provideCheckDownloadedArtifacts(
			@Named("agent.artifacts.store.groupId") String groupId,
			@Named("agent.artifacts.store.path") String localStorePath, LocalArtifactsService localArtifactsService,
			JsonMapper mapper) {
		return new CheckDownloadedArtifacts(groupId, localStorePath, localArtifactsService, mapper);
	}

	@Provides
	MultipleArtifactsJob provideMultipleArtifactsJob(@Named("agent.artifacts.store.groupId") String groupId,
			@Named("agent.artifacts.store.path") String localStorePath, ArtifactsManager artifactsManager,
			JsonStoreService jsonStoreService, ArtifactsManagerStateService artifactsManagerStateService) {
		return new MultipleArtifactsJob(groupId, localStorePath, artifactsManager, jsonStoreService,artifactsManagerStateService);
	}

	@Provides
	@Singleton
	DocumentStore provideDocumentStore() {
		return new DocumentStore();
	}

	@Provides
	@Singleton
	static DimmerUtils provideDimmerUtils() {
		return new DimmerUtils(null);
	}

	@Provides
	MessageService provideMessageService(@Named("agent.message.outbound.url") String messageServiceUrl, //
			@Named("agent.message.outbound.defaultTimeoutInMillis") String defaultMessageTimeoutINMillis, //
			JsonMapper jsonMapper, Lazy<OkHttpClient> clientFactory, DocumentStore documentStore) {
		MessageService ms = new MessageService(messageServiceUrl, defaultMessageTimeoutINMillis, jsonMapper,
				clientFactory, documentStore);
		Logger.info("providing new MessageService: " + ms);
		return ms;

	}

	@Provides
	@Singleton
	static ArtifactsManagerStateService provideArtifactsManagerStateService() {
		return new ArtifactsManagerStateService(AgentState.START);
	}

	@Provides
	@Singleton
	static JsonMapper provideJsonMapper() {
		return new JsonMapper();
	}

	@Provides
	@Singleton
	static ArtifactsManagerService provideArtifactsManagerService(@Named("agent.artifacts.http.baseUrl") String baseUrl,
			@Named("agent.artifacts.http.restPath") String restPath) {
		return new ArtifactsManagerService(baseUrl, restPath);
	}

	@Provides
	@Singleton
	static ArtifactsManager provideArtifactsManager(ArtifactsManagerService service,
			JsonStoreService jsonStoreService) {
		return new ArtifactsManagerImpl(service, jsonStoreService);
	}

	@Provides
	@Singleton
	static LocalArtifactsService provideLocalArtifactsService(
			@Named("agent.artifacts.store.path") String localStorePath, JsonStoreService storeService,
			JsonMapper mapper) {
		return new LocalArtifactsService(localStorePath, storeService, mapper);
	}

	@Provides
	MessageReceiver provideMessageReceiver(@Named("agent.message.inbound.mqttURl") String mqttURl,
			@Named("agent.message.inbound.mqttQosLevel") String qosLevel,
			@Named("agent.message.inbound.mqttKeepAlive") String mqttKeepAlive,
			@Named("agent.message.inbound.mqttConnectAttemptMax") String connectAttemptMax,
			@Named("agent.message.inbound.mqttReconnectAttemptsMax") String reconnectAttemptsMax,
			@Named("agent.message.inbound.mqttReconnectDelay") String reconnectDelay,
			@Named("agent.message.inbound.mqttReconnectDelayMax") String reconnectDelayMax,
			@Named("agent.message.inbound.receiveFreqInMillis") String receiveFreqInMillis) {
		Logger.info("providing new message receiver");

		String deviceId = DeviceInfoUtil.createDeviceInfo("").getDeviceId();
		String[] mqtt = mqttURl.split(":");
		MessageReceiver messageReceiver = new MessageReceiver(deviceId, mqtt[0], Integer.parseInt(mqtt[1]), 30000,
				Integer.parseInt(qosLevel));
		try {
			messageReceiver.init(5000, Integer.parseInt(mqttKeepAlive), Long.parseLong(connectAttemptMax),
					Long.parseLong(reconnectAttemptsMax), Long.parseLong(reconnectDelay),
					Long.parseLong(reconnectDelayMax), Long.parseLong(receiveFreqInMillis));
		} catch (Exception e) {
			Logger.error(e,"Error during provideMessageReceiver");
		}

		return messageReceiver;
	}

	@Provides
	@Named("agent.message.inbound.receiveFreqInMillis")
	public String getInboundReceiveFreq() {
		return System.getProperty("agent.message.inbound.receiveFreqInMillis");
	}

	@Provides
	@Named("agent.server.port")
	public String getAgentServerPort() {
		return System.getProperty("agent.server.port");
	}

	@Provides
	@Named("agent.server.rootFolder")
	public String getAgentServerRootFolder() {
		return System.getProperty("agent.server.rootFolder");
	}

	@Provides
	@Named("agent.message.inbound.mqttURl")
	public String getMqttUrl() {
		return System.getProperty("agent.message.inbound.mqttURl");
	}

	@Provides
	@Named("agent.message.inbound.mqttQosLevel")
	public String getMqttQosLevel() {
		return System.getProperty("agent.message.inbound.mqttQosLevel");
	}

	@Provides
	@Named("agent.message.inbound.mqttKeepAlive")
	public String getMqttKeepAlive() {
		return System.getProperty("agent.message.inbound.mqttKeepAlive");
	}

	@Provides
	@Named("agent.message.inbound.mqttConnectAttemptMax")
	public String getMqttConnectAttemptMax() {
		return System.getProperty("agent.message.inbound.mqttConnectAttemptMax");
	}

	@Provides
	@Named("agent.message.inbound.mqttReconnectAttemptsMax")
	public String getMqttReconnectAttemptsMax() {
		return System.getProperty("agent.message.inbound.mqttReconnectAttemptsMax");
	}

	@Provides
	@Named("agent.message.inbound.mqttReconnectDelay")
	public String getMqttReconnectDelay() {
		return System.getProperty("agent.message.inbound.mqttReconnectDelay");
	}

	@Provides
	@Named("agent.message.inbound.mqttReconnectDelayMax")
	public String getMqttReconnectDelayMax() {
		return System.getProperty("agent.message.inbound.mqttReconnectDelayMax");
	}

	@Provides
	@Singleton
	static JsonStoreService provideJsonStoreService(@Named("agent.artifacts.store.path") String storePath,
			@Named("agent.artifacts.store.groupId") String groupId, JsonMapper mapper) {
		return new JsonStoreService(storePath, groupId, mapper);
	}

	@Provides
	@Named("agent.artifacts.http.restPath")
	public String getHttpRestPath() {
		return System.getProperty("agent.artifacts.http.restPath");
	}

	@Provides
	@Named("agent.apiSelector.baseUrl")
	public String getHttpBaseUrl() {
		return System.getProperty("agent.apiSelector.baseUrl");
	}

	@Provides
	@Named("agent.artifacts.store.path")
	public String getStorePath() {
		return System.getProperty("agent.artifacts.store.path");
	}

	@Provides
	@Named("agent.artifacts.store.groupId")
	public String getGroupId() {
		return System.getProperty("agent.artifacts.store.groupId");
	}

	@Provides
	@Named("agent.message.outbound.url")
	public String getOutboundUrl() {
		return System.getProperty("agent.message.outbound.url");
	}

	@Provides
	@Named("agent.message.outbound.defaultTimeoutInMillis")
	public String getOutboundTimeoutInMillis() {
		return System.getProperty("agent.message.outbound.defaultTimeoutInMillis");
	}

	@Provides
	@Named("agent.artifacts.http.baseUrl")
	public String getArtifactsHttpBaseUrl() {
		return System.getProperty("agent.artifacts.http.baseUrl");
	}

	@Provides
	@Named("agent.message.outbound.connectTimeoutInMillis")
	public String getOutboundConnectTimeout() {
		return System.getProperty("agent.message.outbound.connectTimeoutInMillis");
	}

	@Provides
	@Named("agent.message.outbound.readTimeoutInMillis")
	public String getOutboundReadTimeout() {
		return System.getProperty("agent.message.outbound.readTimeoutInMillis");
	}

	@Provides
	@Named("agent.message.outbound.writeTimeoutInMillis")
	public String getOutboundWriteTimeout() {
		return System.getProperty("agent.message.outbound.writeTimeoutInMillis");
	}

}
