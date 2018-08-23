
package it.eng.areas.ems.sdodaeservices.rest.service.app;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderBareRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.EventDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.CheckInterventionBean;
import it.eng.areas.ems.sdodaeservices.delegate.model.CheckInterventionResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Event;
import it.eng.areas.ems.sdodaeservices.delegate.model.EventInfoRequest;
import it.eng.areas.ems.sdodaeservices.delegate.model.EventManagedEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.FRPlaceArrivalBean;
import it.eng.areas.ems.sdodaeservices.delegate.model.FRPlaceArrivalBeanResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Intervento;
import it.eng.areas.ems.sdodaeservices.delegate.model.NotificheEvento;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.EventFilter;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;
import it.eng.areas.ems.sdodaeservices.rest.utils.FilterUtils;

@Component
@Path("/emergencyService")
@Api(value = "/emergencyService", protocols = "http", description = "Servizio dedicato alla gestione dei First Responder")
public class EmergencyServiceREST {

	protected Logger logger = LoggerFactory.getLogger(EmergencyServiceREST.class);

	@Autowired
	private EventDelegateService eventDelegateService;

	@Autowired
	protected FirstResponderDelegateService frDelegateService;

	@Context
	private SecurityContext secContext;

	@Autowired
	private FilterUtils filterUtils;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sendEmergencyEvent")
	@ApiOperation(value = "sendEmergencyEvent", notes = "Metodo che consente di notificare ai First Responder una nuova emergenza")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Event.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione della ricerca") })
	public Response sendEmergencyEvent(@ApiParam(value = "Evento") Event event) {

		try {
			logger.info("SEND EMERGENCY EVENT: " + event.toString());
			Event retEVent = eventDelegateService.insertEvent(event);
			eventDelegateService.alertFirstResponderForNewEvent(retEVent);
			return Response.ok(retEVent).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING sendEmergencyEvent", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("checkIntervention")
	@Secured
	@ApiOperation(value = "checkIntervention", notes = "Metodo che consente di verificare se un particolare First Responder può intervenire per il soccorso ad un particolare evento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = CheckInterventionResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response checkIntervention(
			@ApiParam(value = "Bean di verifica disponibilità intervento") CheckInterventionBean checkIntervention) {
		try {

			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderBareRule.NAME, u.getId());
			logger.info(
					"CHECKING INTERVENTION FOR FR: " + fr.getId() + " EVENT: " + checkIntervention.getEvent().getId());
			CheckInterventionResponse check = eventDelegateService.checkIntervention(checkIntervention, fr);
			return Response.ok(check).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE checkIntervention", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getMyEvents")
	@Secured
	@ApiOperation(value = "getMyEvents", notes = "Metodo che consente di ottenere la lista degli eventi gestiti o di tutti gli eventi potenzialmente gestibili")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", responseContainer = "List", response = Event.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getMyEvents() {

		try {
			List<Event> evtToReturn = new ArrayList<Event>();

			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderDeepDepthRule.NAME,
					u.getId());
			boolean disp = fr.getDisponibile();
			logger.info("GETTING MY AVAILABLE EVENTS...I'M AVAILABLE: " + disp);
			Intervento inter = eventDelegateService.getInterventoAttivoByFirstResponder(fr.getId());
			if (inter != null) {
				Event actInter = inter.getEvent();
				if (inter.getDataChiusura() != null) {
					actInter.setManagementStatus(EventManagedEnum.SELF_MANAGED_CLOSED);
				} else {
					actInter.setManagementStatus(EventManagedEnum.SELF_MANAGED);
				}

				actInter.setFrAcceptDate(inter.getDataAccettazione());
				actInter.setFrCloseDate(inter.getDataChiusura());

				evtToReturn.add(actInter);
			} else if (disp == true) {
				// gli eventi disponibili li mostro solo se disponibile
				List<Event> evt = eventDelegateService.getAvailableEventsForFirstResponder(fr);
				for (Event event : evt) {
					event.setManagementStatus(EventManagedEnum.NOT_MANAGED);
					evtToReturn.add(event);
				}

			}
			return Response.ok(evtToReturn).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE rejectEvent", e);
			return Response.serverError().build();
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("rejectEvent")
	@Secured
	@ApiOperation(value = "rejectEvent", notes = "Metodo che consente ad un First Responder di rifiutare l'evento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = GenericResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response rejectEvent(@ApiParam(value = "Bean informazioni evento") EventInfoRequest eventInfo) {
		try {

			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderDeepDepthRule.NAME,
					u.getId());
			logger.info("REJECT EVENT FOR FIRST RESPONDER: " + fr.getNome() + " " + fr.getCognome() + " eventID : "
					+ eventInfo.getEventId());
			GenericResponse check = eventDelegateService.rejectEvent(eventInfo, fr);
			return Response.ok(check).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE rejectEvent", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("abortEvent")
	@ApiOperation(value = "abortEvent", notes = "Metodo che consente di comunicare che un'emergenza è stata chiusa/annullata")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = GenericResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response abortEvent(@ApiParam(value = "Evento da abortire") EventInfoRequest event) {
		try {
			logger.info("INVOKED ABORT EVENT FOR CARTELLINO: " + event.getCartellino() + " CO RIFERIMENTO: "
					+ event.getCoRiferimento());
			int notify = eventDelegateService.closeOrAbortInterventionByEvent(event, true);
			GenericResponse resp = new GenericResponse();
			resp.setResponse(true);
			resp.setMessage(notify + " WAS ABORTED SUCCESSFULLY");
			return Response.ok(resp).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING abortEvent", e);
			GenericResponse resp = new GenericResponse();
			resp.setResponse(false);
			resp.setMessage("ERROR WHILE EXECUTING abortEvent");
			return Response.ok(resp).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("updateEvent")
	@ApiOperation(value = "updateEvent", notes = "Metodo che consente di comunicare che un'emergenza è stata modificata")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = GenericResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response updateEvent(@ApiParam(value = "Evento da aggiornare") Event event) {
		try {
			logger.info("INVOKED UPDATE EVENT FOR CARTELLINO: " + event.getCartellino() + " CO RIFERIMENTO: "
					+ event.getCoRiferimento());
			List<Intervento> abInter = eventDelegateService.updateEvent(event);
			GenericResponse resp = new GenericResponse();
			resp.setResponse(true);
			resp.setMessage(abInter.size() + " WAS UPDATED SUCCESSFULLY");
			return Response.ok(resp).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING updateEvent", e);
			GenericResponse resp = new GenericResponse();
			resp.setResponse(false);
			resp.setMessage("ERROR WHILE EXECUTING updateEvent");
			return Response.ok(resp).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("placeArrival")
	@Secured
	@ApiOperation(value = "placeArrival", notes = "Metodo che consente di comunicare che un FR è arrivato nei pressi dell'emergenza")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = FRPlaceArrivalBeanResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response closeIntervention(
			@ApiParam(value = "Bean di arrivo nei pressi dell'emergenza") FRPlaceArrivalBean placeArrivalBean) {

		GenericResponse gr = new GenericResponse();

		try {
			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderDeepDepthRule.NAME,
					u.getId());
			gr.setMessage("Operazione avvenuta correttamente");
			boolean ret = eventDelegateService.closeIntervention(fr.getId(), placeArrivalBean.getEventId(),
					placeArrivalBean.getCoordinate());
			gr.setResponse(ret);
		} catch (Exception e) {
			logger.error("ERROR WHILE executing closeIntervention", e);
			gr.setMessage("Errure durante l'esecuzione del metodo");
			gr.setResponse(true);
		}

		return Response.ok(gr).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getEventById")
	@ApiOperation(value = "getEventById", notes = "Metodo che consente di ottenere le info dell'evento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Event.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getEventById(@ApiParam(value = "Bean di ricerca evento per ID") EventInfoRequest eventInfo) {
		Event evt = eventDelegateService.getEventById(eventInfo.getEventId());
		return Response.ok(evt).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getEventInterventionDetails")
	@ApiOperation(value = "getEventInterventionDetails", notes = "Metodo che consente di ottenere le info rispetto agli interventi attivi su un Evento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Event.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getEventInterventionDetails(
			@ApiParam(value = "Bean di ricerca evento per ID") EventInfoRequest eventInfo) {

		try {
			Event respEvt = eventDelegateService.getEventInterventionDetails(eventInfo);

			return Response.ok(respEvt).build();
		} catch (Exception e) {
			GenericResponse gr = new GenericResponse();
			gr.setMessage("Errore durante l'esecuzione del metodo");
			gr.setResponse(false);
			return Response.ok(gr).build();
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sendNotificheEvento")
	@ApiOperation(value = "sendNotificheEvento", notes = "Metodo che consente di notificare ai First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = NotificheEvento.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione della ricerca") })
	public Response sendEmergencyNotificheEvento(@ApiParam(value = "NotificheEvento") NotificheEvento event) {
		eventDelegateService.insertNotificheEvento(event);
		return Response.ok(new NotificheEvento()).build();
	}

	@POST
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("searchEventByFilter")
	@ApiOperation(value = "searchEventByFilter", notes = "Metodo che consente di cercare gli eventi")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Event.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione della ricerca") })
	public Response searchEventByFilter(@ApiParam(value = "Filtro") final EventFilter filter) {
		try {
			if (filter.getDataDA() != null) {
				filter.getDataDA().setTimeZone(TimeZone.getDefault());
			}

			if (filter.getDataA() != null) {
				filter.getDataA().setTimeZone(TimeZone.getDefault());
			}

			filterUtils.addProvinceToFilter(secContext, filter);
			List<Event> events = eventDelegateService.searchEventByFilter(filter);

			events.forEach(e -> {
				e.getInterventi().forEach(i -> {
					if (i.getDataAccettazione() != null) {
						e.setAccettati(e.getAccettati() + 1);
					}

					if (i.getDataChiusura() != null) {
						e.setArrivati(e.getArrivati() + 1);
					}
				});
				if (filter.getFetchRule() == null || filter.getFetchRule().equals("SEARCH")) {
					e.setConteggioNotifiche(eventDelegateService.countNotified(e.getId()));
				}
			});

			return Response.ok(events).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE executing searchEventByFilter", e);
			GenericResponse gr = new GenericResponse();
			gr.setMessage("Errore durante l'esecuzione del metodo");
			gr.setResponse(false);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(gr).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getEventList/{page}")
	@Secured
	@ApiOperation(value = "getEventList", notes = "Metodo che consente di ottenere la lista degli eventi gestiti dal FR che ha invocato il metodo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", responseContainer = "List", response = Event.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getEventList(
			@ApiParam(value = "utilizzato per la paginazione dei risultati", required = true) @PathParam("page") Integer page) {
		if (page == null) {
			page = 0;
		}
		EventFilter filter = new EventFilter();
		Utente u = (Utente) secContext.getUserPrincipal();
		FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderDeepDepthRule.NAME, u.getId());

		filter.setFrID(fr.getId());
		filter.setAccepted(true);

		filter.setFetchRule("APP");

		// filter.setMaxResult(10);
		// filter.setPaging(new Paging(page * 10, 10));

		List<Event> events = eventDelegateService.searchEventByFilter(filter);

		return Response.ok(events).build();
	}

}
