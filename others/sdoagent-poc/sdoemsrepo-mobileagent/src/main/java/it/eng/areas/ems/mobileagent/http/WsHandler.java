/**
 * 
 */
package it.eng.areas.ems.mobileagent.http;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.pmw.tinylog.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jayway.jsonpath.JsonPath;

import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService.AgentState;
import it.eng.areas.ems.mobileagent.device.DeviceFactory;
import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;

/**
 * @author Bifulco Luigi
 *
 */
@WebSocket
public class WsHandler {

	private static Session currentSession;

	private ScheduledExecutorService service;

	private static ExecutorService worker;
	// = Executors.newSingleThreadExecutor();

	private static AgentRestService _AgentRestService;

	private static List<String> pendingMessage = new ArrayList<>();

	public static void injectAgentRestService(AgentRestService _AgentRestService) {
		WsHandler._AgentRestService = _AgentRestService;
	}

	public static void sendEvent(String payload) {
		//Luigi: commentato perché conviene farlo suonare quando effettivamente il messaggio viene processato
//		try {
//			if (payload != null && payload.contains("beep")) {
//				boolean beep = JsonPath.read(payload, "$.msg.hd.beep");
//				if (beep) {
//					DeviceFactory.get().getAudioService().resume();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		if (currentSession == null) {
			pendingMessage.add(payload);
			return;
		}

		submitEvent(payload);

	}

	protected static void submitEvent(String payload) {
		if (worker == null) {
			ThreadFactory fact = new ThreadFactoryBuilder().setNameFormat("WsHandlerWorker-thread-%d").build();
			worker = Executors.newFixedThreadPool(1, fact);
		}
		worker.submit(new Runnable() {

			@Override
			public void run() {
				try {
					if (currentSession != null) {

						currentSession.getRemote().sendString(payload);
					} else {
						Logger.info("Session is null warning");
					}
				} catch (Exception e) {
					Logger.error(e,"Exception in submitEvent");
				}

			}
		});
	}

	@OnWebSocketConnect
	public void onConnect(Session s) throws Exception {
		if (worker == null) {
			ThreadFactory fact = new ThreadFactoryBuilder().setNameFormat("WsHandlerWorker-thread-%d").build();
			worker = Executors.newFixedThreadPool(1, fact);
		}
		try {
			// _AgentRestService.setWsHandler(this);
			Logger.info("onConnect: " + s.getRemote().getInetSocketAddress());
			Logger.info(s.getProtocolVersion());
			currentSession = s;
			AgentState state = _AgentRestService.getArtifactsManagerStateService().getState();
			String version = _AgentRestService.getMessageService().getResourceVersion();
			// + " - " + state.getReason() +
			WsHandler.sendEvent("[{\"type\":\"SETUP\", \"resourceVersion\":" + version + ", \"result\": \""
					+ state.name() + "\"}]");
			if (service == null) {
				ThreadFactory fact = new ThreadFactoryBuilder().setNameFormat("WsKeepAlive-thread-%d").build();

				service = Executors.newSingleThreadScheduledExecutor(fact);
				service.scheduleAtFixedRate(new Runnable() {

					@Override
					public void run() {
						if (pendingMessage.size() > 0) {
							pendingMessage.forEach((m) -> {
								submitEvent(m);
							});
							pendingMessage.clear();
						}
						sendEvent("{\"type\": \"h\"}");

					}
				}, 5, 5, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			Logger.error(e,"Exception in onConnect handler");
		}
	}

	@OnWebSocketClose
	public void onClose(Session s, int statusCode, String reason) {
		currentSession = null;
		Logger.info("onClose: " + statusCode + " - " + reason);
		worker.shutdownNow();
		worker = null;
		service.shutdownNow();
		service = null;
	}

	@OnWebSocketMessage
	public void onMessage(Session s, String message) {
		try {
			String type = JsonPath.read(message, "type");
			switch (type) {
			case "LOG":
				JsonMapper mapper = _AgentRestService.getMapper();
				Message m = mapper.parse(message, Message.class);
				logMessage(m.getPayload());
				break;

			default:
				break;
			}
		} catch (Exception e) {
			Logger.error(e,"Exception in onMessage handler");
		}
	}

	private void logMessage(String data) {
		String level = JsonPath.read(data, "verbosity");
		String message = JsonPath.read(data, "message");
		Logger.info("logging data: " + level + " - " + message);
		switch (level) {
		case "info":
			Logger.info(message);
			break;
		case "warning":
			Logger.warn(message);
			break;
		case "error":
			Logger.error(message);
			break;
		case "debug":
			Logger.debug(message);
			break;
		}
	}

}
