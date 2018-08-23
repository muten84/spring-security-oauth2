package it.eng.areas.ems.sdodaeservices.delegate.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.EventDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MessagesDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.PushNotificationService;
import it.eng.areas.ems.sdodaeservices.delegate.PushwooshNotification;
import it.eng.areas.ems.sdodaeservices.delegate.model.Event;
import it.eng.areas.ems.sdodaeservices.delegate.model.EventNotificationOperationEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.Messaggio;
import it.eng.areas.ems.sdodaeservices.delegate.model.Notification;
import it.eng.areas.ems.sdodaeservices.delegate.model.NotificheEvento;
import it.eng.areas.ems.sdodaeservices.delegate.model.Request;
import it.eng.areas.ems.sdodaeservices.entity.EventDO;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.NotificheEventoDO;
import it.eng.areas.ems.sdodaeservices.entity.NotificheTypeEnum;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.service.EventTransactionalService;

@Component
public class PushWooshNotificationService implements PushNotificationService {

	private Logger logger = LoggerFactory.getLogger(PushWooshNotificationService.class);

	@Autowired
	private AnagraficheDelegateService anagraficheService;

	@Autowired
	private MessagesDelegateService messagesServce;

	@Autowired
	private EventDelegateService eventDelegate;

	@Autowired
	private EventTransactionalService eventTrans;

	@Value("${pushwoosh.ios.sound}")
	private String iosSound;

	@Value("${pushwoosh.android.sound}")
	private String androidSound;

	private RestTemplate createRestTemplate() throws Exception {
		Boolean useProxy = Boolean.valueOf(anagraficheService.getParameter(ParametersEnum.USE_PROXY.name()));

		if (useProxy) {

			String username = anagraficheService.getParameter(ParametersEnum.PROXY_USERNAME.name());

			final String password = anagraficheService.getParameter(ParametersEnum.PROXY_PASSWORD.name());
			final String proxyUrl = anagraficheService.getParameter(ParametersEnum.PROXY_ADDRESS.name());
			final int port = Integer.valueOf(anagraficheService.getParameter(ParametersEnum.PROXY_PORT.name()));

			logger.info(
					String.format("Use proxy : [%s:%d - user(%s) password(%s)] ", proxyUrl, port, username, password));

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(proxyUrl, port),
					new UsernamePasswordCredentials(username, password));

			HttpHost myProxy = new HttpHost(proxyUrl, port);
			HttpClientBuilder clientBuilder = HttpClientBuilder.create();

			clientBuilder.setProxy(myProxy).setDefaultCredentialsProvider(credsProvider).disableCookieManagement();

			HttpClient httpClient = clientBuilder.build();
			HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
			factory.setHttpClient(httpClient);

			return new RestTemplate(factory);
		} else {
			logger.info("No proxy");

			return new RestTemplate();
		}
	}

	@Override
	public String notifyNewEmergencyToFirstResponder(Event event, FirstResponder fR,
			NotificheEvento.Type tipoSelezione) {
		try {
			JsonObject obj = new JsonObject();
			obj.addProperty("eventId", event.getId());
			obj.addProperty("operation", EventNotificationOperationEnum.NEW.name());

			List<String> destinations = new ArrayList<>();

			if (fR.getDispositivo() != null && !StringUtils.isEmpty(fR.getDispositivo().getPushToken())) {
				destinations.add(fR.getDispositivo().getPushToken());
			}

			String message = anagraficheService.getParameter(ParametersEnum.NEW_EVENT_MESSAGE_TEXT.name(),
					"Nuova emergenza DAE");

			String toRet = sendMessageWithAudio(message, obj, destinations.toArray(new String[destinations.size()]));
			NotificheEvento not = new NotificheEvento();
			not.setEvent(event);
			not.setTimestamp(Calendar.getInstance().getTime());
			not.setFirstResponder(fR);
			not.setTipoNotifica(NotificheTypeEnum.NUOVA_EMERGENZA);
			not.setEsito(toRet);

			not.setTipoSelezione(tipoSelezione);
			if (fR.getLastPosition() != null && fR.getLastPosition().getLatitudine() != null
					&& fR.getLastPosition().getLongitudine() != null) {
				not.setLatitudine(fR.getLastPosition().getLatitudine().doubleValue());
				not.setLongitudine(fR.getLastPosition().getLongitudine().doubleValue());
				not.setCoordTimestamp(fR.getLastPosition().getTimeStamp());
			}

			eventDelegate.insertNotificheEvento(not);
			return toRet;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING notifyToFirstResponder", e);
			return " ERROR - NOTIFY NOT SENT";
		}
	}

	@Override
	public String notifyMessageToFirstResponderList(String message, List<FirstResponder> fRs) throws Exception {
		// lista di id a cui mandare i messaggi
		List<String> destinations = new ArrayList<>();
		// lista di FR a cui ho effettivamente mandato il messaggio
		List<FirstResponder> toSave = new ArrayList<>();

		fRs.forEach(fr -> {
			if (fr.getDispositivo() != null
					&& !it.esel.parsley.lang.StringUtils.isEmpty(fr.getDispositivo().getPushToken())) {
				destinations.add(fr.getDispositivo().getPushToken());
				toSave.add(fr);
			}
		});

		byte[] utf8JsonString = message.getBytes("UTF8");
		message = new String(utf8JsonString);

		JsonObject obj = new JsonObject();
		obj.addProperty("message", message);

		String toRet = sendMessage(message, obj, destinations.toArray(new String[destinations.size()]));

		logger.info("Result Invio Push :" + toRet);
		// se tutto è andato a buon fine salvo il messaggio sul db
		Messaggio messaggio = new Messaggio();
		messaggio.setInvio(new Date());
		messaggio.setResponders(toSave);
		messaggio.setTesto(message);
		messaggio.setStato(toRet.length() <= 512 ? toRet : toRet.substring(0, 512));
		messagesServce.insertMessaggio(messaggio);

		return toRet;
	}

	public String sendMessage(String messaggioNotifica, JsonElement jsonContent, String... devices) throws Exception {

		Boolean usePush = Boolean
				.valueOf(anagraficheService.getParameter(ParametersEnum.PUSH_NOTIFICATION_ENABLED.name()));

		if (usePush) {
			RestTemplate restTemplate = createRestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Request req = new Request();
			String pushApplication = anagraficheService.getParameter(ParametersEnum.PUSH_APPLICATION.name());
			// req.setApplication("B719A-01522");
			req.setApplication(pushApplication);
			String pushAuth = anagraficheService.getParameter(ParametersEnum.PUSH_AUTH.name());

			// req.setAuth("1N7OCff0vAeZISXEvpdmG99TTLlDCU5FFzCuo3jJSRiQbqdRFpwlCgvivMz4TcBoikP2puKwznBAZ5fgSoIg");
			req.setAuth(pushAuth);
			Notification not = new Notification();
			not.setData(jsonContent);

			not.setContent(messaggioNotifica);

			not.setSend_date("now");
			List<Notification> notL = new ArrayList<Notification>();

			if (devices != null) {
				not.setDevices(devices);
			}

			notL.add(not);
			req.setNotifications(notL);

			PushwooshNotification pn = new PushwooshNotification();
			pn.setRequest(req);

			Gson gs = new Gson();
			String json = gs.toJson(pn);
			HttpEntity<String> request = new HttpEntity<>(json, headers);

			String pushURL = anagraficheService.getParameter(ParametersEnum.PUSHWOOSH_URL.name());

			logger.info("URL: " + pushURL);
			logger.info("Request: " + json);

			String returnedUser = restTemplate.postForObject(pushURL, request, String.class);

			return returnedUser;
		} else {
			return "PUSH_NOT_ENABLED";
		}
	}

	public String sendMessageWithAudio(String messaggioNotifica, JsonElement jsonContent, String... devices)
			throws Exception {

		Boolean usePush = Boolean
				.valueOf(anagraficheService.getParameter(ParametersEnum.PUSH_NOTIFICATION_ENABLED.name()));

		if (usePush) {
			RestTemplate restTemplate = createRestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			Request req = new Request();
			String pushApplication = anagraficheService.getParameter(ParametersEnum.PUSH_APPLICATION.name());
			// req.setApplication("B719A-01522");
			req.setApplication(pushApplication);
			String pushAuth = anagraficheService.getParameter(ParametersEnum.PUSH_AUTH.name());

			// req.setAuth("1N7OCff0vAeZISXEvpdmG99TTLlDCU5FFzCuo3jJSRiQbqdRFpwlCgvivMz4TcBoikP2puKwznBAZ5fgSoIg");
			req.setAuth(pushAuth);
			Notification not = new Notification();
			not.setData(jsonContent);
			not.setContent(messaggioNotifica);
			// Mauro : sostituito file audio
			not.setAndroid_sound(androidSound);
			not.setAndroid_vibration("true");
			not.setIos_sound(iosSound);

			not.setSend_date("now");
			List<Notification> notL = new ArrayList<Notification>();

			if (devices != null) {
				not.setDevices(devices);
			}

			notL.add(not);
			req.setNotifications(notL);

			PushwooshNotification pn = new PushwooshNotification();
			pn.setRequest(req);

			Gson gs = new Gson();
			String json = gs.toJson(pn);
			HttpEntity<String> request = new HttpEntity<>(json, headers);

			String pushURL = anagraficheService.getParameter(ParametersEnum.PUSHWOOSH_URL.name());

			logger.info("URL: " + pushURL);
			logger.info("Request: " + json);

			String returnedUser = restTemplate.postForObject(pushURL, request, String.class);

			return returnedUser;
		} else {
			return "PUSH_NOT_ENABLED";
		}
	}

	@Override
	public String notifyAbortToFirstResponder(String messageToSend, Event event, String pushToken) {
		return notifyAbortToFirstResponder(messageToSend, event.getId(), pushToken);
	}

	@Override
	public String notifyEventUpdateToFirstResponder(Event event, String pushToken) {
		return notifyEventUpdateToFirstResponder(event.getId(), pushToken);
	}

	@Override
	public String notifyEventUpdateToFirstResponder(String eventId, String pushToken) {
		try {
			JsonObject obj = new JsonObject();
			obj.addProperty("eventId", eventId);
			obj.addProperty("operation", EventNotificationOperationEnum.UPDATE.name());

			List<String> destinations = new ArrayList<>();

			if (!StringUtils.isEmpty(pushToken)) {
				destinations.add(pushToken);
			}
			String message = anagraficheService.getParameter(ParametersEnum.UPDATE_EVENT_MESSAGE_TEXT.name(),
					"Aggiornamento Emergenza DAE");

			String toRet = sendMessageWithAudio(message, obj, destinations.toArray(new String[destinations.size()]));
			return toRet;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING notifyAbortToFirstResponder", e);
			return " ERROR - NOTIFY NOT SENT";
		}
	}

	@Override
	public String notifyAbortToFirstResponder(String messageToSend, Event event, FirstResponder fR) {
		return notifyAbortToFirstResponder(messageToSend, event, fR.getDispositivo().getPushToken());
	}

	@Override
	public String notifyEventUpdateToFirstResponder(Event event, FirstResponder fR) {
		String ret = notifyEventUpdateToFirstResponder(event, fR.getDispositivo().getPushToken());
		NotificheEvento not = new NotificheEvento();
		not.setEvent(event);
		not.setTimestamp(Calendar.getInstance().getTime());
		not.setFirstResponder(fR);
		not.setTipoNotifica(NotificheTypeEnum.UPDATE);
		not.setEsito(ret);
		eventDelegate.insertNotificheEvento(not);
		return ret;
	}

	@Override
	public String notifyAbortToFirstResponder(String messageToSend, String eventId, String pushToken) {
		try {
			JsonObject obj = new JsonObject();
			obj.addProperty("eventId", eventId);
			obj.addProperty("operation", EventNotificationOperationEnum.ABORT.name());
			obj.addProperty("description", messageToSend);

			List<String> destinations = new ArrayList<>();

			if (!StringUtils.isEmpty(pushToken)) {
				destinations.add(pushToken);
			}

			byte[] utf8JsonString = messageToSend.getBytes("UTF8");
			messageToSend = new String(utf8JsonString);
			String toRet = sendMessageWithAudio(messageToSend, obj,
					destinations.toArray(new String[destinations.size()]));
			return toRet;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING notifyAbortToFirstResponder", e);
			return " ERROR - NOTIFY NOT SENT";
		}
	}

	@Override
	public String notifyEventUpdateToFirstResponder(EventDO event, FirstResponderDO fR) {
		String ret = notifyEventUpdateToFirstResponder(event.getId(), fR.getDispositivo().getPushToken());
		NotificheEventoDO not = new NotificheEventoDO();
		not.setEvent(event);
		not.setTimestamp(Calendar.getInstance().getTime());
		not.setFirstResponder(fR);
		not.setTipoNotifica(NotificheTypeEnum.UPDATE);
		not.setEsito(ret);
		eventTrans.insertNotificheEvento(not);
		return ret;
	}
}
