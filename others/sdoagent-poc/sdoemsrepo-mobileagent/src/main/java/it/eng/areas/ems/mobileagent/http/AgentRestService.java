/**
 * 
 */
package it.eng.areas.ems.mobileagent.http;

import static spark.Spark.awaitInitialization;
import static spark.Spark.before;
import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;
import static spark.Spark.init;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;
import static spark.Spark.webSocket;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.pmw.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jayway.jsonpath.JsonPath;

import dagger.Lazy;
import it.eng.area118.mdo.jme.data.Country;
import it.eng.area118.mdo.jme.data.Dynamic;
import it.eng.area118.mdo.jme.data.Locality;
import it.eng.area118.mdo.jme.data.Municipality;
import it.eng.area118.mdo.jme.data.Pathology;
import it.eng.area118.mdo.jme.data.PathologyClass;
import it.eng.area118.mdo.jme.data.Province;
import it.eng.area118.mdo.jme.data.Region;
import it.eng.area118.mdo.jme.data.ServiceProvided;
import it.eng.area118.mdo.jme.data.ServiceResultType;
import it.eng.areas.ems.mobileagent.AgentApp;
import it.eng.areas.ems.mobileagent.artifacts.ExecutionContext.LockDeviceState;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService.AgentState;
import it.eng.areas.ems.mobileagent.artifacts.service.LocalArtifactsService;
import it.eng.areas.ems.mobileagent.artifacts.util.CommandRunner;
import it.eng.areas.ems.mobileagent.artifacts.util.DimmerUtils;
import it.eng.areas.ems.mobileagent.artifacts.util.InstallUtils;
import it.eng.areas.ems.mobileagent.db.LookupService;
import it.eng.areas.ems.mobileagent.db.PerstLookupFactory;
import it.eng.areas.ems.mobileagent.device.DeviceFactory;
import it.eng.areas.ems.mobileagent.device.DeviceInfoUtil;
import it.eng.areas.ems.mobileagent.message.MessageProcessingException;
import it.eng.areas.ems.mobileagent.message.MessageReceiver;
import it.eng.areas.ems.mobileagent.message.MessageService;
import it.eng.areas.ems.mobileagent.message.db.DocumentStore;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DetailItemMap;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DeviceConfiguration;
import it.eng.areas.ems.sdoemsrepo.delegate.model.InstallArtifactRequest;
import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;
import it.eng.areas.ems.sdoemsrepo.delegate.model.MobileDeviceSession;
import it.eng.areas.ems.sdoemsrepo.delegate.model.MobileEmergencyData;
import it.eng.areas.ems.sdoemsrepo.delegate.model.MobileResource;
import net.minidev.json.JSONObject;

/**
 * @author Bifulco Luigi
 *
 */
public class AgentRestService {

	private LockDeviceState LOCK_STATE = LockDeviceState.UNLOCKED;

	public static final String VERSION = "1.6.2";

	private static int ERROR_CODE = 550;

	private LocalArtifactsService localArtifactsService;

	private String groupId;

	private String localStorePath;

	private Lazy<MessageService> messageServiceFactory;

	private Lazy<MessageReceiver> messageReceiverFactory;

	private MessageService messageService;

	private MessageReceiver messageReceiver;

	private ArtifactsManagerStateService artifactsManagerStateService;

	private DimmerUtils dimmer;

	private static JsonMapper mapper;

	private Lazy<DocumentStore> documentStore;

	private Lazy<PerstLookupFactory> dbService;

	private ScheduledExecutorService messageReceiverScheduler;

	@Inject
	public AgentRestService(LocalArtifactsService localArtifactsService, //
			@Named("agent.artifacts.store.groupId") String groupId, //
			@Named("agent.artifacts.store.path") String localStorePath, //
			Lazy<MessageService> messageService, //
			Lazy<MessageReceiver> messageReceiverFactory, //
			ArtifactsManagerStateService artifactsManagerStateService, //
			DimmerUtils dimmerUtils, //
			JsonMapper mapper, //
			Lazy<DocumentStore> documentStore, //
			Lazy<PerstLookupFactory> dbService) {
		this.localArtifactsService = localArtifactsService;
		this.groupId = groupId;
		this.localStorePath = localStorePath;
		this.messageServiceFactory = messageService;
		this.messageReceiverFactory = messageReceiverFactory;
		this.artifactsManagerStateService = artifactsManagerStateService;
		this.dimmer = dimmerUtils;
		this.documentStore = documentStore;
		AgentRestService.mapper = mapper;
		Logger.info("creating new instance of AgentRestService");
		this.dbService = dbService;
	}

	public void createService(String externalStaticFileLocation, int port) {
		// Logger logger = Logger.getLogger(SparkMain.class);
		externalStaticFileLocation(externalStaticFileLocation);// "/esel/terminal/"
		port(port);
		// initExceptionHandler((e)->{
		// e.printStackTrace();
		// });

		webSocket("/event", WsHandler.class);
		enableCORS("*", "OPTIONS,GET,POST,PUT,DELETE", "");
		// SparkUtils.createServerWithRequestLog(logger); // staticFileLocation(root);

		// servizio di test del'agent
		/* operazione per testare se l'agent è vivo */
		get("/test", (request, response) -> {
			Logger.info("Invoking test");
			response.body(mapper.stringify(DeviceInfoUtil.createDeviceInfo("")));
			response.header("Content-type", "application/json");
			return response.body();
		});

		get("/deviceId", (request, response) -> {
			Logger.info("Invoking deviceId");
			String payload = "{\"deviceId\": \"" + DeviceInfoUtil.createDeviceInfo("").getDeviceId() + "\"}";
			response.body(payload);
			response.header("Content-type", "application/json");
			return response.body();
		});

		/*
		 * operazione per verificare che versione dell'agent si sta interrogando
		 */
		get("/getVersion", (request, response) -> {
			Logger.info("Invoking getVersion");
			response.header("Content-type", "application/json");
			response.body("{\"version:\": \"" + VERSION + "\"}");
			return response.body();
		});

		/* operazione per verificare lo stato di un artefatto scaricato */
		get("/getArtifact/:artifactId", (request, response) -> {
			String artifactId = request.params("artifactId");
			Logger.info("Invoking getArtifact " + artifactId);
			response.header("Content-type", "application/json");
			try {
				LocalArtifactsService s = this.localArtifactsService;
				String res = s.getLastArtifact(this.groupId, artifactId);
				response.body(res);
			} catch (Exception e) {
				Logger.error(e, "Exception in getArtifact");
				response.status(ERROR_CODE);
				return response.body();
			}
			return response.body();
		});

		/*
		 * operazione che permette di resettare il local store degli artefatti
		 * installati
		 */
		get("/resetLocalStore", (request, response) -> {
			Logger.info("ResetLocalStore ");
			response.header("Content-type", "application/json");
			LocalArtifactsService s = this.localArtifactsService;
			boolean result = s.resetLocalStore(this.groupId);
			return "[{\"result\": \"" + result + "\"}]";
		});

		/* permette di pulire la cache del browser */
		get("/clearAppCache", (request, response) -> {
			Logger.info("Invoking clearAppCache ");

			response.header("Content-type", "application/json");
			boolean result = InstallUtils.clearAppCache("terminal-electron");
			return "[{\"result\": \"" + result + "\"}]";
		});

		/*
		 * permette di resettare uno store json di documents in base alla sua tipologia
		 */
		get("/resetDocumentStore/:type", (request, response) -> {

			response.header("Content-type", "application/json");
			String type = request.params("type");

			Logger.info("Invoking resetDocumentStore " + type);

			boolean result = this.messageService.resetDocumentStore(type);

			return "[{\"result\": \"" + result + "\"}]";
		});

		get("/messageStore", (request, response) -> {
			Logger.info("Invoking messageStore");

			List<Message> allMessages = this.messageService.getDocumentStore().listAllMessages();
			response.header("Content-type", "application/json");
			response.body(mapper.stringify(allMessages));
			return response.body();
		});

		/* operazione che permette di capire lo stato dell'agent corrente */
		get("/getAgentState", (request, response) -> {
			Logger.info("Invoking getAgentState");

			AgentState state = this.artifactsManagerStateService.getState();
			response.header("Content-type", "application/json");
			return "[{\"result\": \"" + state.name() + " - " + state.getReason() + "\"}]";
		});

		/*
		 * operazione che permette di aumentare o diminuire la luminosità in base ad un
		 * effetto dimmer
		 */
		get("/setBrightness/:opacity", (request, response) -> {

			String op = request.params("opacity");

			Logger.info("Invoking setBrightness " + op);
			// this.dimmer.dimmerOff();
			// this.dimmer.setBrightness(Double.valueOf(op));
			// this.dimmer.dimmerOn();

			DeviceFactory.get().getDisplay().setBrightness(Integer.parseInt(op));

			response.header("Content-type", "application/json");
			return "[{\"result\": \"" + true + "\"}]";
		});

		/*
		 * operazione che permette di ottenere il valore corrente di luminosità
		 */
		get("/getBrightness", (request, response) -> {
			// this.dimmer.dimmerOff();
			// this.dimmer.setBrightness(Double.valueOf(op));
			// this.dimmer.dimmerOn();
			Logger.info("Invoking getBrightness");
			int value = DeviceFactory.get().getDisplay().getBrightness();

			response.header("Content-type", "application/json");
			return "[{\"result\": \"" + value + "\"}]";
		});

		/* operazione che permette di disattivare l'overlay del dimmer */
		get("/dimmeroff", (request, response) -> {
			Logger.info("Invoking dimmeroff");
			this.dimmer.dimmerOff();
			response.header("Content-type", "application/json");
			return "[{\"result\": \"" + true + "\"}]";
		});

		/*
		 * operazione che permette di mettere in modalità dedicata il dispositivo
		 *
		 * sulla famiglia fi sistemi operativi windows uccide explorer
		 */

		get("/lock", (request, response) -> {
			Logger.info("Invoking lock");

			setLockState(LockDeviceState.LOCKED);
			CommandRunner.stopExplorer();
			response.header("Content-type", "application/json");
			return "[{\"result\": \"" + getLockState().name() + "\"}]";
		});

		/*
		 * complementare di lock avvia una shell di explorer e una shell dei comandi
		 */
		get("/unlock", (request, response) -> {
			Logger.info("Invoking unlock");

			setLockState(LockDeviceState.UNLOCKED);
			CommandRunner.startExplorer();
			CommandRunner.runCmdShell();
			response.header("Content-type", "application/json");
			return "[{\"result\": \"" + getLockState().name() + "\"}]";
		});

		/*
		 * permette di ottenere lo stato corrente del dispositivo in riferimento al
		 * lock/unlock del sistema operativo
		 */
		get("/getLockState", (request, response) -> {
			Logger.info("Invoking getLockState");

			response.header("Content-type", "application/json");
			return "[{\"result\": \"" + getLockState().name() + "\"}]";
		});

		/* permette di comandare un riavvio schedulato entro 5 secondi */
		get("/restart", (request, response) -> {
			Logger.info("Invoking restart");

			response.header("Content-type", "application/json");
			response.body(mapper.stringify(DeviceInfoUtil.createDeviceInfo("")));
			Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {

				@Override
				public void run() {
					try {
						CommandRunner.restart();
					} catch (IOException e) {
						Logger.error(e, "IOException in restart command");
					}
					System.exit(0);
				}
			}, 5, 5, TimeUnit.SECONDS);

			return response.body();
		});

		/* permette di comandare lo spegnimento del dispositivo */
		get("/shutdown", (request, response) -> {
			Logger.info("Invoking shutdown");

			response.header("Content-type", "application/json");
			response.body(mapper.stringify(DeviceInfoUtil.createDeviceInfo("")));
			Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {

	@Override
				public void run() {
					try {
						CommandRunner.forceShutdow();
					} catch (IOException e) {
						Logger.error(e, "IOException in forceShutdow command");
					}
					System.exit(0);
				}
			}, 5, 5, TimeUnit.SECONDS);

			return response.body();
		});

		get("/stopProcess/:process/:child", (request, response) -> {
			response.header("Content-type", "application/json");
			response.body(mapper.stringify(DeviceInfoUtil.createDeviceInfo("")));

			String process = request.params("process");
			String child = request.params("child");

			Logger.info("Invoking stopProcess " + process + " - " + child);

			CommandRunner.stopProcess(process, child.equals("1"));

			return response.body();

		});

		get("/startProcess/:cmd/", (request, response) -> {
			response.header("Content-type", "application/json");
			response.body(mapper.stringify(DeviceInfoUtil.createDeviceInfo("")));

			String cmd = request.params("cmd");

			Logger.info("Invoking startProcess " + cmd);

			CommandRunner.runProcess(cmd, System.getProperty("agent.workingDir") + "/bin");

			return response.body();

		});

		/*
		 * permette di forzare l'installazione di un artefatto tramite le informazioni
		 * dell'artefatto @see InstallArtifactRequest
		 */
		post("/install", (request, response) -> {
			response.header("Content-type", "application/json");

			Logger.info("Invoking install ");

			String json = request.body();
			// mapper.parse(src, valueType)
			InstallArtifactRequest req = mapper.parse(json, InstallArtifactRequest.class);
			ArtifactInfo artifact = req.getArtifact();
			String archive = this.localStorePath;
			archive += "/" + artifact.getGroupId() + "/" + artifact.getArtifactId() + "-" + artifact.getVersion() + "."
					+ artifact.getArtifactType();

			boolean res = false;

			try {
				res = InstallUtils.extractAndCopy(archive, req.getDestinationPath());
				Logger.info("copy: " + res);
				if (res) {
					res = InstallUtils.installationDone(archive);
					Logger.info("delete: " + res);
				}
				response.body("{result: \"" + res + "\", request: " + json + "}");
			} catch (Exception e) {
				Logger.error(e, "Exception in install command");
				response.status(ERROR_CODE);
				response.body("{error: \"" + e.getMessage() + "\" result: \"" + res + "\", request: " + json + "}");
				return response.body();
			}

			return response.body();
		});

		/*
		 * la subscribe verso il broker mqtt normalmente questo operazione non è
		 * necessaria poiché la subscribe parte all'avvio dell'agent
		 */
		get("/subscribe", (request, response) -> {
			Logger.info("Invoking subscribe ");
			response.header("Content-type", "application/json");
			String res = this.messageReceiver.subscribe();
			response.body("{\"result\": \"" + res + "\"}");
			return response.body();
		});

		/*
		 * operazione di receive dal broker mqtt normalmente questa operazione non è
		 * necessaria poiché l'agent si mette in receive automaticamente dal broker
		 */
		get("/receive", (request, response) -> {
			Logger.info("Invoking receive");
			response.header("Content-type", "application/json");
			String res = this.messageReceiver.receive();
			response.body("{\"result\": \"" + res + "\"}");
			return response.body();
		});

		/*
		 * operazione per poter inviare un messaggio dal terminale verso la centrale
		 * tramite il canale mqtt
		 */
		post("/publish", (request, response) -> {
			Logger.info("Invoking publish");
			String json = request.body();
			Message message = mapper.parse(json, Message.class);
			MessageReceiver mr = this.messageReceiver;
			mr.publish(message.getTo(), json);
			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + true + "\"}");
			return response.body();
		});

		/* operazione di test per inviare un messaggio al device stesso */
		get("/publishTest", (request, response) -> {
			MessageReceiver mr = this.messageReceiver;
			for (int i = 0; i < 10; i++) {
				mr.publish(DeviceInfoUtil.createDeviceInfo("").getDeviceId(), "Test attivazione " + i);
			}
			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + true + "\"}");
			return response.body();
		});

		/*
		 * operazione che permette di invocare una chiamata Remote Procedure Call verso
		 * il server chi invoca normalmente si mette in attesa della risposta
		 * applicativa
		 */
		post("/rpc", (request, response) -> {
			Logger.info("Invoking rpc");
			response.header("Content-type", "application/json");
			String json = request.body();
			int ttlInMillis = JsonPath.read(json, "ttl");
			ttlInMillis = ttlInMillis * 1000;

			String result = "{}";
			try {
				Logger.info("--> rpc send message: " + json);
				result = this.messageService.sendMessage(json, ttlInMillis);
				Logger.info("<-- rpc response message: " + result);
			} catch (MessageProcessingException e) {
				Logger.error(e, "MessageProcessingException in rpc command");

				Message command = mapper.parse(json, Message.class);
				Message erroreMessage = createMessage(command, "ERROR", "{\"error\": \"" + e.getMessage() + "\"}");
				response.status(500);
				response.body(mapper.stringify(erroreMessage));

			}

			response.body(result);
			return response.body();
		});

		/* operazione che permette di scaricare le risorse del terminale */
		get("/resource/:type", (request, response) -> {
			String type = request.params("type");
			Logger.info("Invoking Resource type: " + type);
			response.header("Content-type", "application/json");
			String result = "{}";
			boolean configuration = type.equalsIgnoreCase("configuration");
			try {
				// String[] preserve = type.equalsIgnoreCase("VEHICLE")
				result = this.messageService.extractResource(type, configuration);
			} catch (MessageProcessingException e) {
				Logger.error(e, "MessageProcessingException in resource type");

				Message erroreMessage = createMessage(null, "ERROR", "{\"error\": \"" + e.getMessage() + "\"}");
				response.status(500);
				response.body(mapper.stringify(erroreMessage));
				return response.body();
			}
			response.body(result);
			return response.body();
		});

		get("/resource/configuration/:type", (request, response) -> {
			String type = request.params("type");
			Logger.info("Invoking Resource configuration: " + type);
			response.header("Content-type", "application/json");
			String result = "{}";
			try {
				// String[] preserve = type.equalsIgnoreCase("VEHICLE")
				result = this.messageService.extractResource(type, true);
			} catch (MessageProcessingException e) {
				Logger.error(e, "MessageProcessingException in Resource configuration");
				// TODO create ERROR MESSAGE with payload containing error class
				Message erroreMessage = createMessage(null, "ERROR", "{\"error\": \"" + e.getMessage() + "\"}");
				response.status(500);
				response.body(mapper.stringify(erroreMessage));
				return response.body();
			}
			response.body(result);
			return response.body();
		});

		get("/localResource/:type", (request, response) -> {
			String type = request.params("type");
			Logger.info("Invoking localResource : " + type);
			response.header("Content-type", "application/json");
			String result = "{}";
			boolean configuration = type.equalsIgnoreCase("configuration");
			try {
				// String[] preserve = type.equalsIgnoreCase("VEHICLE")
				result = this.messageService.extractLocalResource(type, configuration);
			} catch (MessageProcessingException e) {
				Logger.error(e, "MessageProcessingException in localResource");
				// TODO create ERROR MESSAGE with payload containing error class
				Message erroreMessage = createMessage(null, "ERROR", "{\"error\": \"" + e.getMessage() + "\"}");
				response.status(500);
				response.body(mapper.stringify(erroreMessage));
				return response.body();
			}
			response.body(result);
			return response.body();
		});

		/*
		 * permette di ottenre la versione delle risorse da scaricare in modo da non
		 * richiederle se già aggiornate
		 */
		get("/getResourceVersion/", (request, response) -> {
			Logger.info("Invoking getResourceVersion");

			response.header("Content-type", "application/json");
			String result = "{}";
			try {
				// String[] preserve = type.equalsIgnoreCase("VEHICLE")
				result = this.messageService.getResourceVersion();
			} catch (MessageProcessingException e) {
				Logger.error(e, "MessageProcessingException in getResourceVersion");
				// TODO create ERROR MESSAGE with payload containing error class
				Message erroreMessage = createMessage(null, "ERROR", "{\"error\": \"" + e.getMessage() + "\"}");
				response.status(500);
				response.body(mapper.stringify(erroreMessage));
				return response.body();
			}
			response.body(result);
			return response.body();
		});

		/*
		 * operazione che permette di accodare un messaggio asincrono da inviare alla
		 * centrale normalmente chi invoca questa operazione non è interessato alla
		 * risposta applicativa ma solo alla presa in carico del messaggio nel frattempo
		 * potrebbero essere accodati altri messaggi
		 */
		post("/enqueue", (request, response) -> {
			Logger.info("Invoking enqueue");
			response.header("Content-type", "application/json");
			String json = request.body();
			Message message = mapper.parse(json, Message.class);
			boolean _result = false;
			try {
				_result = this.messageService.enqueueMessage(message);
			} catch (MessageProcessingException e) {
				Logger.error(e, "MessageProcessingException in enqueue");
				// TODO create ERROR MESSAGE with payload containing error class
				Message command = mapper.parse(json, Message.class);
				Message erroreMessage = createMessage(command, "ERROR", "{\"error\": \"" + e.getMessage() + "\"}");
				response.status(500);
				response.body(mapper.stringify(erroreMessage));
			}
			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + _result + "\"}");
			return response.body();
		});

		/*
		 * operazione che permette di verificare se l'agent è connesso al server MQTT
		 */
		get("/isMQTTConnected", (request, response) -> {
			Logger.info("Invoking isMQTTConnected");

			response.header("Content-type", "application/json");
			boolean _result = false;

			_result = this.messageReceiver.isConnected();

			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + _result + "\"}");
			return response.body();
		});

		/**/
		get("/callback", (request, response) -> {
			Logger.info("Invoking callback");
			response.header("Content-type", "application/json");
			// String json = request.body();

			WsHandler.sendEvent(createActivationMessage());
			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + true + "\"}");
			return response.body();
		});

		/*
		 * questa operazione permette di inizializzare il broker MQTT a contesto già
		 * avviato serve per RICEVERE i messaggi dalla centrale
		 */
		// post("/initMessageReceiver", (request, response) -> {
		// response.header("Content-type", "application/json");
		// String mqttUrl = request.body();
		// //ReadContext ctx = JsonPath.parse(json);
		// Logger.info("mqttUrl: " + mqttUrl);
		// getInstance().getExecutionContext().kReceiver(mqttUrl);
		// response.header("Content-type", "application/json");
		// response.body("{\"result\": \"" + true + "\"}");
		// return response.body();
		// });

		/*
		 * questa operazione permette di inizializzare i servizi per lo scambio dei
		 * messaggi con la centrale operativa
		 * 
		 */
		post("/initMessageService", (request, response) -> {
			Logger.info("Invoking initMessageService");

			response.header("Content-type", "application/json");
			String siteInfo = request.body();
			String messageServiceUrl = JsonPath.read(siteInfo, "$.messageServiceUrl");

			String mqttUrl = JsonPath.read(siteInfo, "$.messageReceiverUrl");
			int mqttQosLevel = JsonPath.read(siteInfo, "$.mqttQosLevel");
			System.setProperty("agent.message.outbound.url", messageServiceUrl);
			String username = JsonPath.read(siteInfo, "username");
			String password = JsonPath.read(siteInfo, "password");

			// FIXME:
			System.setProperty("agent.message.outbound.defaultTimeoutInMillis", String.valueOf(30000));
			System.setProperty("agent.message.outbound.connectTimeoutInMillis", String.valueOf(10000));
			System.setProperty("agent.message.outbound.readTimeoutInMillis", String.valueOf(10000));
			System.setProperty("agent.message.outbound.writeTimeoutInMillis", String.valueOf(10000));

			/*
			 * int mqttKeepAlive, long connectAttemptMax, long reconnectAttemptsMax, long
			 * reconnectDelay,long reconnectDelayMax
			 */
			int mqttKeepAlive = JsonPath.read(siteInfo, "$.mqttKeepAlive");
			long connectAttemptMax = Long.valueOf(JsonPath.read(siteInfo, "$.mqttConnectAttemptMax").toString());
			int reconnectAttemptsMax = JsonPath.read(siteInfo, "$.mqttReconnectAttemptsMax");
			long reconnectDelay = Long.valueOf(JsonPath.read(siteInfo, "$.mqttReconnectDelay").toString());
			long reconnectDelayMax = Long.valueOf(JsonPath.read(siteInfo, "$.mqttReconnectDelayMax").toString());
			long receiveFreqInMillis = Long.valueOf(JsonPath.read(siteInfo, "$.mqttReceiveFreqInMillis").toString());

			System.setProperty("agent.message.inbound.mqttURl", mqttUrl);
			System.setProperty("agent.message.inbound.mqttQosLevel", String.valueOf(mqttQosLevel));
			System.setProperty("agent.message.inbound.mqttKeepAlive", String.valueOf(mqttKeepAlive));
			System.setProperty("agent.message.inbound.mqttConnectAttemptMax", String.valueOf(connectAttemptMax));
			System.setProperty("agent.message.inbound.mqttReconnectAttemptsMax", String.valueOf(reconnectAttemptsMax));
			System.setProperty("agent.message.inbound.mqttReconnectDelay", String.valueOf(reconnectDelay));
			System.setProperty("agent.message.inbound.mqttReconnectDelayMax", String.valueOf(reconnectDelayMax));
			System.setProperty("agent.message.inbound.receiveFreqInMillis", String.valueOf(receiveFreqInMillis));

			Logger.info("messageServiceUrl: " + messageServiceUrl);
			Logger.info("mqttUrl: " + mqttUrl);

			this.messageService = this.messageServiceFactory.get();

			this.messageService.setUsername(username);
			this.messageService.setPassword(password);

			if (this.messageReceiver != null) {
				try {
					boolean connected = this.messageReceiver.isConnected();
					if (!connected) {
						// this.messageReceiver.restart();
						Logger.info("WARNING messageReceiver IS NOT MORE CONNECTED!!");
						this.messageReceiver.shutdown();
						this.messageReceiver.rebuild();
						this.messageReceiver.init(10000, mqttKeepAlive, connectAttemptMax, reconnectAttemptsMax,
								reconnectDelay, reconnectDelayMax, receiveFreqInMillis);
					}
				} catch (Exception e) {
					Logger.error(e, "Exception in initMessageService");
				}
			}

			if (this.messageReceiver == null) {
				try {
					this.messageReceiver = this.messageReceiverFactory.get();
					ThreadFactory fact2 = new ThreadFactoryBuilder()
							.setNameFormat("MessageReceiverConnMonitor-thread-%d").build();
					messageReceiverScheduler = Executors.newScheduledThreadPool(1, fact2);
					
//					messageReceiverScheduler.scheduleAtFixedRate(new Runnable() {
//						boolean reconnecting = false;
//						
//						@Override
//						public void run() {
//							try {
//								boolean connected = messageReceiver.isConnected();
//								Logger.info("messageReceiver.isConnected(): " + connected);
//								if (!connected && !reconnecting) {
//									reconnecting = true;
//									messageReceiver.shutdown();
//									messageReceiver.rebuild();
//									messageReceiver.init(60000, mqttKeepAlive, connectAttemptMax, reconnectAttemptsMax,
//											reconnectDelay, reconnectDelayMax, receiveFreqInMillis);
//									Thread.sleep(5000);
//									connected = messageReceiver.isConnected();
//								}
//							} catch (Exception e) {
//								Logger.error(e, "Exception in messageReceiver");
//							}
//						}
//					}, 120000, receiveFreqInMillis, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					Logger.error(e, "Exception in messageReceiver");
				}
			}

			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + true + "\"}");
			System.gc();
			return response.body();
		});

		/*
		 * permette di fare un restart interno dell'agent utile quando bisogna switchare
		 * al sito di backup....
		 */
		get("/restartAgent", (request, response) -> {
			Logger.info("Invoking restartAgent");
			Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {

				@Override
				public void run() {
					AgentApp.stopAgent();
					AgentApp.startAgent();

				}
			}, 1, 1, TimeUnit.SECONDS);
			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + true + "\"}");
			return response.body();
		});

	post("/reInitMessageService", (request, response) -> {
			Logger.info("Invoking reInitMessageService");

			response.header("Content-type", "application/json");
			String siteInfo = request.body();
			String messageServiceUrl = JsonPath.read(siteInfo, "$.messageServiceUrl");
			String mqttUrl = JsonPath.read(siteInfo, "$.messageReceiverUrl");
			int mqttQosLevel = JsonPath.read(siteInfo, "$.mqttQosLevel");

			/*
			 * int mqttKeepAlive, long connectAttemptMax, long reconnectAttemptsMax, long
			 * reconnectDelay,long reconnectDelayMax
			 */
			int mqttKeepAlive = JsonPath.read(siteInfo, "$.mqttKeepAlive");
			long connectAttemptMax = Long.valueOf(JsonPath.read(siteInfo, "$.mqttConnectAttemptMax").toString());
			int reconnectAttemptsMax = JsonPath.read(siteInfo, "$.mqttReconnectAttemptsMax");
			long reconnectDelay = Long.valueOf(JsonPath.read(siteInfo, "$.mqttReconnectDelay").toString());
			long reconnectDelayMax = Long.valueOf(JsonPath.read(siteInfo, "$.mqttReconnectDelayMax").toString());

			Logger.info("messageServiceUrl: " + messageServiceUrl);
			Logger.info("mqttUrl: " + mqttUrl);

			this.messageService = this.messageServiceFactory.get();
			this.messageReceiver = this.messageReceiverFactory.get();
			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + true + "\"}");

			return response.body();
		});

		post("/initDevice", (request, response) -> {
			Logger.info("Invoking initDevice");

			response.header("Content-type", "application/json");
			String deviceConfiguration = request.body();
			Logger.info("INITdEVICE PAYLOAD: " + deviceConfiguration);
			DeviceConfiguration conf = mapper.parse(deviceConfiguration, DeviceConfiguration.class);
			// getInstance().getExecutionContext().createDevice(conf);
			DeviceFactory.get().createDevice(conf, mapper);
			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + true + "\"}");
			return response.body();
		});

		/* Permette di resettare il terminale alle impostazioni iniziali */
		get("/resetAll", (request, response) -> {
			Logger.info("Invoking resetAll");

			response.header("Content-type", "application/json");
			// getInstance().getExecutionContext().shutdownMessageReceiver();
			// getInstance().getExecutionContext().shutdownMessageService();
			// getInstance().getExecutionContext().resetDeviceConfiguration();
			WsHandler.sendEvent("[{\"type\":\"RESET\" }]");
			new Thread(new Runnable() {

				@Override
				public void run() {
					boolean done = documentStore.get().resetAll();
					AgentApp.stopAgent();
					// try {
					// // Importante questo tempo di attesa deve essere propagato anche sulla user
					// // interface della webapp
					// Thread.sleep(2000);
					// } catch (InterruptedException e) {
					// Logger.error(e, "InterruptedException in resetAll");
					// }
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					AgentApp.restartAgent();

				}
			}).start();
			response.body("{\"result\": \"" + true + "\"}");
			return response.body();
		});

		get("/requestDeviceState", (request, response) -> {
			Logger.info("Invoking requestDeviceState");

			DeviceFactory.get().sendDeviceStatus(this.mapper, this.messageReceiver);
			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + true + "\"}");
			return response.body();
		});

		get("/beepAlarm", (request, response) -> {
			Logger.info("Invoking beepAlarm");

			// getInstance().getExecutionContext().getAudioService().resume();
			try {
				DeviceFactory.get().getAudioService().restart();
			} catch (Exception e) {
				Logger.error(e, "Exception in beepAlarm");
			}
			DeviceFactory.get().getAudioService().resume();
			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + true + "\"}");
			return response.body();
		});

		get("/stopBeepAlarm", (request, response) -> {
			Logger.info("Invoking stopBeepAlarm");
			DeviceFactory.get().getAudioService().suspend();
			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + true + "\"}");
			return response.body();
		});

		get("/resetBeepAlarm", (request, response) -> {
			Logger.info("Invoking resetBeepAlarm");
			DeviceFactory.get().getAudioService().restart();
			response.header("Content-type", "application/json");
			response.body("{\"result\": \"" + true + "\"}");
			return response.body();
		});

		/* operazione per ottenere i riferimenti di legame con una centrale */
		// get("/getSiteInfo/:siteId", (request, response) -> {
		post("/getSiteInfo", (request, response) -> {
			Logger.info("Invoking getSiteInfo");

			response.header("Content-type", "application/json");
			String body = request.body();
			String siteId = JsonPath.read(body, "siteId");
			String username = JsonPath.read(body, "username");
			String password = JsonPath.read(body, "password");
			// String siteId = request.params("siteId");

			String apiBaseUrl = System.getProperty("agent.apiSelector.baseUrl");
			// String baseUrl = getInstance().getExecutionContext().getApiBaseUrl();
			String token = MessageService.login(apiBaseUrl, username, password);

			String siteInfo = MessageService.getSiteInfo(apiBaseUrl, siteId, username, password, token);
			response.body(siteInfo);
			return response.body();
		});

		post("/saveSession", (request, response) -> {
			Logger.info("Invoking saveSession");

			response.header("Content-type", "application/json");
			String body = request.body();
			Logger.info("saving session: " + body);
			try {
				String sessionId = JsonPath.read(body, "sessionId");
				MobileDeviceSession session = this.mapper.parse(body, MobileDeviceSession.class);
				this.documentStore.get().saveSession(session);
				response.body(this.mapper.stringify(this.documentStore.get().getSession(sessionId)));
				return response.body();
			} catch (Exception e) {
				Logger.error(e, "error while saving session: " + e.getMessage());
			}
			response.body("{}");
			return response.body();
			// MobileDeviceSession updatedSession = new MobileDeviceSession(sessionId);
			// updatedSession.addEntry("test", "value of test sessione entry");

		});

		get("/getCurrentSession/:sessionId", (request, response) -> {
			Logger.info("Invoking getCurrentSession");

			response.header("Content-type", "application/json");
			String sessionId = request.params("sessionId");

			response.body(this.mapper.stringify(this.documentStore.get().getSession(sessionId)));
			return response.body();

		});

		// chiamata xhr esterna per il dump118
		post("/dispatch", (request, response) -> {
			Logger.info("Invoking dispatch");

			String body = request.body();
			String method = JsonPath.read(body, "method");
			String op = JsonPath.read(body, "op");
			Map map = JsonPath.read(body, "payload");
			String payload = JSONObject.toJSONString(map);
			response.body(messageService.dispatch(method, op, payload));
			response.header("Content-type", "application/json");

			return response.body();

			// String test = FileUtils
			// .readFileToString(new File(System.getProperty("agent.workingDir") +
			// "/etc/dump.json"));
			// response.body(test);

		});

		/* permette di accedere alle risorse del db locale */
		get("/db/v2/listAll/:type", (request, response) -> {
			// TODO: IMPORTANTE SENZA QUESTA FEATURE SI AVRANNO PROBLEMI DI MANUTENZIONE
			// FIXME:
			/*
			 * IMPLEMENTARE SISTEMA DIA CCESSO AI DATI CON PERST UTILIZZANDO UN UNICA
			 * ENTITA' AL MOMENTO CE LO SCONTIAMO PERCHE RIUTILIZZIAMO LE STESSE ENTITA DEL
			 * VECCHIO
			 */
			return null;
		});

		/* permette di accedere alle risorse del db locale */
		post("/db/v2/lookup/:type", (request, response) -> {
			// TODO: IMPORTANTE SENZA QUESTA FEATURE SI AVRANNO PROBLEMI DI MANUTENZIONE
			// FIXME:
			/*
			 * IMPLEMENTARE SISTEMA DIA CCESSO AI DATI CON PERST UTILIZZANDO UN UNICA
			 * ENTITA' AL MOMENTO CE LO SCONTIAMO PERCHE RIUTILIZZIAMO LE STESSE ENTITA DEL
			 * VECCHIO
			 */
			return null;
		});

		/* permette di accedere alle risorse del db locale */
		get("/db/v1/listAll/:type", (request, response) -> {

			String type = request.params("type");
			Logger.info("Invoking db/v1/listAll/: " + type);
			String payload = request.body();
			response.header("Content-type", "application/json");
			PerstLookupFactory factory = this.dbService.get();
			Class<?> clazz = getClassFromType(type);
			if (clazz == null) {
				response.body("[]");
				return response.body();
			}
			LookupService service = factory.getLookUpService(clazz);
			List<?> list = null;
			list = service.getAll();
			List<MobileResource> result = createResourceFromList(list, type, clazz);
			response.body(this.mapper.stringify(result));
			return response.body();
		});

		/* permette di accedere alle risorse del db locale */
		post("/db/v1/lookup/:type", (request, response) -> {
			String type = request.params("type");

			Logger.info("Invoking db/v1/lookup/: " + type);

			String payload = request.body();
			String filter = null;
			try {
				filter = JsonPath.read(payload, "$.filter");
			} catch (Exception e) {
				Logger.error(e, "Exception in db/v1/lookup");
				filter = null;
			}
			response.header("Content-type", "application/json");
			PerstLookupFactory factory = this.dbService.get();
			Class<?> clazz = getClassFromType(type);
			if (clazz == null) {
				response.body("[]");
				return response.body();
			}
			LookupService service = factory.getLookUpService(clazz);
			List<?> list = null;
			if (filter == null) {
				list = service.getAll();
			} else {
				list = service.lookup(filter);
			}
			List<MobileResource> result = createResourceFromList(list, type, clazz);
			response.body(this.mapper.stringify(result));
			return response.body();
		});

	}

	@SuppressWarnings("unchecked")
	private List<MobileResource> createResourceFromList(List l, String type, Class<?> clazz) {
		List<MobileResource> list = new ArrayList<>();
		l.forEach((e) -> {
			MobileResource r = new MobileResource();
			r.setType(type);
			try {
				r.setId(clazz.getMethod("getCode", new Class[] {}).invoke(e, new Object[] {}).toString());
				r.setName(clazz.getMethod("getName", new Class[] {}).invoke(e, new Object[] {}).toString());
				r.setValue(clazz.getMethod("getName", new Class[] {}).invoke(e, new Object[] {}).toString());
			} catch (NoSuchMethodException | SecurityException e1) {
				Logger.error(e1, "Exception in createResourceFromList");
			} catch (IllegalAccessException e1) {
				Logger.error(e1, "Exception in createResourceFromList");
			} catch (IllegalArgumentException e1) {
				Logger.error(e1, "Exception in createResourceFromList");
			} catch (InvocationTargetException e1) {
				Logger.error(e1, "Exception in createResourceFromList");
			}
			list.add(r);
		});
		return list;

	}

	private Class<?> getClassFromType(String type) {
		if (type.equalsIgnoreCase(Country.getType())) {
			return Country.class;
		}
		if (type.equalsIgnoreCase(Dynamic.getType())) {
			return Dynamic.class;
		}
		if (type.equalsIgnoreCase(Locality.getType())) {
			return Locality.class;
		}
		if (type.equalsIgnoreCase(Municipality.getType())) {
			return Municipality.class;
		}
		if (type.equalsIgnoreCase(Pathology.getType())) {
			return Pathology.class;
		}
		if (type.equalsIgnoreCase(PathologyClass.getType())) {
			return PathologyClass.class;
		}
		if (type.equalsIgnoreCase(Province.getType())) {
			return Province.class;
		}
		if (type.equalsIgnoreCase(Region.getType())) {
			return Region.class;
		}
		if (type.equalsIgnoreCase(ServiceProvided.getType())) {
			return ServiceProvided.class;
		}
		if (type.equalsIgnoreCase(ServiceResultType.getType())) {
			return ServiceResultType.class;
		}
		return null;

	}

	private static String createActivationMessage() throws JsonProcessingException {

		SimpleDateFormat formtDate = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formthour = new SimpleDateFormat("HH:mm");
		Date d = Calendar.getInstance().getTime();
		String ds = formtDate.format(d);
		String hs = formthour.format(d);

		Message message = new Message();

		message.setType("ACT");

		MobileEmergencyData objectMessageActive = new MobileEmergencyData();
		objectMessageActive.setLuogo("TVZ".toUpperCase());
		objectMessageActive.setPatologia("TST".toUpperCase());
		objectMessageActive.setCriticita("B".toUpperCase());
		objectMessageActive.setModAttivazione("Avanzato BLU".toUpperCase());
		objectMessageActive.setSirena("NO".toUpperCase());
		objectMessageActive.setComune("BO".toUpperCase());
		objectMessageActive.setLocalita("San Benedetto Val di Sambro".toUpperCase());
		objectMessageActive.setCap("40131".toUpperCase());
		objectMessageActive.setIndirizzo("Via Cristoforo Colombo".toUpperCase());
		objectMessageActive.setLuogoPubblico("Prova Luogo Pubblico".toUpperCase());
		objectMessageActive.setCivico("110".toUpperCase());
		objectMessageActive.setPiano("3°");
		objectMessageActive.setCodEmergenza("12345678");
		objectMessageActive.setDataOraSegnalazione(ds);
		objectMessageActive.setPersonaRif("Gianluigi Albertazzi".toUpperCase());
		objectMessageActive.setTelefono("3453456789");
		objectMessageActive.setnPazienti("1");
		objectMessageActive.setSex("M".toUpperCase());
		objectMessageActive.setEta("34 anni".toUpperCase());
		objectMessageActive.setNote("PROVA EMERGENZA DI TEST PER VISUALIZZAZIONE NOTE SU TERMINALE WEB".toUpperCase());

		DetailItemMap detailItemMessageActive = new DetailItemMap();
		detailItemMessageActive.getDetailMap().put("Intervista base",
				"VEDE ACCADUTO: SI, RESPIRA MALE: NO".toUpperCase());
		detailItemMessageActive.getDetailMap().put("TRAUMA", "MALORE, DOLORE TORACICO".toUpperCase());
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setCriticitaObject(detailItemMessageActive);

		detailItemMessageActive = new DetailItemMap();

		detailItemMessageActive.getDetailMap().put("Data", ds);
		detailItemMessageActive.getDetailMap().put("Ora", hs);
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setDataOraSegnalazioneObject(detailItemMessageActive);

		detailItemMessageActive = new DetailItemMap();
		detailItemMessageActive.getDetailMap().put("Note strada".toUpperCase(),
				"Test visualizzazione note della strada o incrocio".toUpperCase());
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setIndirizzoObject(detailItemMessageActive);

		detailItemMessageActive = new DetailItemMap();
		detailItemMessageActive.getDetailMap().put("Note Luogo Pubblico".toUpperCase(),
				"TIPOLOGIA DI LUOGO PUBBLICO A RISCHIO PRESTARE ATTENZIONE".toUpperCase());
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setLuogoPubblicoObject(detailItemMessageActive);

		detailItemMessageActive = new DetailItemMap();
		detailItemMessageActive.getDetailMap().put("Note Localita'".toUpperCase(),
				"Prova visualizzazione note indicate sulla località del luogo dell'emergenza".toUpperCase());
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setLocalitaObject(detailItemMessageActive);

		detailItemMessageActive = new DetailItemMap();
		detailItemMessageActive.getDetailMap().put("Lista Mezzi In soccorso".toUpperCase(),
				"MIKE01, BRAVO06".toUpperCase());
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setCodEmergenzaObject(detailItemMessageActive);

		detailItemMessageActive = new DetailItemMap();
		detailItemMessageActive.getDetailMap().put("", objectMessageActive.getNote()
				+ " Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur. Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur. Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur. Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur. Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur. Lorem ipsum dolor sit amet, consectetur adipisci elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur."
						.toUpperCase());
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setNoteObject(detailItemMessageActive);

		message.setPayload(mapper.stringify(objectMessageActive));

		return mapper.stringify(message);
	}

	private static Message fromMessage(Message m) {
		Message newM = new Message();
		newM.setFrom(m.getTo());
		newM.setTo(m.getFrom());
		newM.setTimestamp(Calendar.getInstance().getTimeInMillis());
		newM.setRelatesTo(m.getId());
		newM.setType(m.getType());
		newM.setRpcOperation(m.getRpcOperation());
		newM.setId(UUID.randomUUID().toString());
		return newM;
	}

	private static Message createMessage(Message from, String type, String payload) {
		Message m = new Message();
		m.setId(UUID.randomUUID().toString());
		if (from != null) {
			m.setRelatesTo(from.getRelatesTo());
			m.setTo(from.getFrom());
		}
		m.setProcessed(true);
		m.setType(type);
		m.setTimestamp(Calendar.getInstance().getTimeInMillis());
		if (payload != null && !payload.isEmpty()) {
			m.setPayload(payload);
		}
		return m;
	}

	// Enables CORS on requests. This method is an initialization method and
	// should be called once.
	private static void enableCORS(final String origin, final String methods, final String headers) {

		options("/*", (request, response) -> {

			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}

			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}

			return "OK";
		});

		before((request, response) -> {
			response.header("Access-Control-Allow-Origin", origin);
			response.header("Access-Control-Request-Method", methods);
			// response.header("Access-Control-Allow-Headers", headers);
			// Note: this may or may not be necessary in your particular
			// application
			response.type("application/json");
		});
		init();
		awaitInitialization();
		Logger.info("Agent rest service awaitInitialization completed");
	}

	public void setLockState(LockDeviceState newState) {
		this.LOCK_STATE = newState;
	}

	public LockDeviceState getLockState() {
		return LOCK_STATE;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		// test parsing coordinate
		// mapper = new JsonMapper();
		// String json = "";
		// Message message = mapper.parse(json, Message.class);
		// System.out.println(message.getPayload());

	}

	/**
	 * @return the artifactsManagerStateService
	 */
	public ArtifactsManagerStateService getArtifactsManagerStateService() {
		return artifactsManagerStateService;
	}

	/**
	 * @return the messageService
	 */
	public MessageService getMessageService() {
		return messageService;
	}

	/**
	 * @return the mapper
	 */
	public JsonMapper getMapper() {
		return mapper;
	}

	public void startup() {
		this.documentStore.get().init();
	}

	public void shutdown() {
		try {
			AgentRestService.stopService();

			if (messageReceiverScheduler != null)
				messageReceiverScheduler.shutdownNow();
			this.documentStore.get().close();
			if (this.messageReceiver != null)
				this.messageReceiver.shutdown();
			if (this.messageService != null)
				this.messageService.closeMessageService();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void stopService() {
		stop();
	}

}
