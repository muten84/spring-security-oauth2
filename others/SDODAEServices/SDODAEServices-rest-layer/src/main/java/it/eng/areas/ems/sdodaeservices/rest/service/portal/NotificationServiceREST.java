package it.eng.areas.ems.sdodaeservices.rest.service.portal;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MessagesDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.PushNotificationService;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.Messaggio;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.FirstResponderFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.MessaggioFilter;
import it.eng.areas.ems.sdodaeservices.rest.exception.AppException;
import it.eng.areas.ems.sdodaeservices.rest.model.MessageFRFilter;
import it.eng.areas.ems.sdodaeservices.rest.model.MessageFRList;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;

@Component
@Path("/portal/notificationService")
@Api(value = "/portal/notificationService", protocols = "http", description = "Servizio dedicato all'invio delle notifiche verso i client")
public class NotificationServiceREST {

	private Logger logger = LoggerFactory.getLogger(NotificationServiceREST.class);

	@Autowired
	protected PushNotificationService notificationService;

	@Autowired
	protected FirstResponderDelegateService frDelegateService;

	@Autowired
	protected MessagesDelegateService messaggiService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sendNotificationToFirstResponderList")
	@ApiOperation(value = "sendNotificationToFirstResponderList", notes = "Metodo che consente di inviare una notifica generia agli FR")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione eseguita correttamente"),
			@ApiResponse(code = 500, message = "Errore durante l'invio della notifica") })
	@Secured
	public Response sendNotificationToFirstResponderList(
			@ApiParam(value = "messaggio e lista first responder") MessageFRList responders) throws AppException {
		try {
			notificationService.notifyMessageToFirstResponderList(responders.getMessage(),
					responders.getFirstResponders());
			return Response.ok().build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING sendNotificationToFirstResponderList", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING sendNotificationToFirstResponderList", e.getMessage(),
					"");
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sendNotificationByFilter")
	@ApiOperation(value = "sendNotificationByFilter", notes = "Metodo che consente di inviare una notifica generia ai agli filtrati")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione eseguita correttamente"),
			@ApiResponse(code = 500, message = "Errore durante l'invio della notifica") })
	@Secured
	public Response sendNotificationByFilter(@ApiParam(value = "messaggio e filtro FR") MessageFRFilter filter)
			throws AppException {
		try {
			FirstResponderFilter f = filter.getFilter();
			List<FirstResponder> frs = frDelegateService.searchFirstResponderByFilter(f);
			notificationService.notifyMessageToFirstResponderList(filter.getMessage(), frs);
			return Response.ok().build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING sendNotificationByFilter", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING sendNotificationByFilter", e.getMessage(), "");
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("searchNotificationByFilter")
	@ApiOperation(value = "searchNotificationByFilter", notes = "Metodo che consente di cercare le notifiche inviate")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", responseContainer = "List", response = Messaggio.class),
			@ApiResponse(code = 500, message = "Errore durante la ricerca") })
	@Secured
	public Response searchNotificationByFilter(@ApiParam(value = "filter") MessaggioFilter filter) throws AppException {
		try {
			List<Messaggio> messaggi = messaggiService.getMessaggioByFilter(filter);
			return Response.ok(messaggi).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchNotificationByFilter", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING searchNotificationByFilter", e.getMessage(), "");
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("deleteNotification/{id}")
	@ApiOperation(value = "deleteNotification", notes = "Metodo che consente di cancellare le notifiche inviate")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione eseguita correttamente"),
			@ApiResponse(code = 500, message = "Errore durante la ricerca") })
	@Secured
	public Response deleteNotification(@PathParam(value = "id") String id) throws AppException {
		try {
			boolean deleted = messaggiService.deleteMessaggioById(id);
			return Response.ok(deleted).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING deleteNotification", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING deleteNotification", e.getMessage(), "");
		}
	}

	@POST
	@Path("/getNotificationFR/{messageId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "getNotificationFR", notes = "Operazione utile ad ottenere il detaglio dei destinatari di un messaggio")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione eseguita correttamente"),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	@Secured
	public Response getNotificationFR(@PathParam("messageId") String messageId) throws AppException {
		try {
			List<FirstResponder> list = messaggiService.getMessageResponders(messageId);
			return Response.ok(list).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getNotificationFR", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING getNotificationFR", e.getMessage(), "");
		}
	}

}
