package it.eng.areas.ems.sdodaeservices.rest.service.portal;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import it.eng.areas.ems.sdodaeservices.delegate.EventDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.Event;
import it.eng.areas.ems.sdodaeservices.delegate.model.NotificheEvento;
import it.eng.areas.ems.sdodaeservices.entity.filter.NotificheEventoFilterDO;
import it.eng.areas.ems.sdodaeservices.rest.exception.AppException;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;
import it.eng.areas.ems.sdodaeservices.rest.service.app.EmergencyServiceREST;

@Component
@Path("/portal/emergencyService")
@Api(value = "/portal/emergencyService", protocols = "http", description = "Servizio dedicato alla gestione dei First Responder")
public class EmergencyPortalServiceREST extends EmergencyServiceREST {

	@Context
	private SecurityContext secContext;

	@Autowired
	private EventDelegateService eventDelegateService;

	@POST
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("saveEvent")
	public Response saveEvent(Event event) throws AppException {
		try {
			Event saved = eventDelegateService.saveEvent(event);
			return Response.ok(saved).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING saveEvent", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING saveEvent", e.getMessage(), "");
		}
	}

	@POST
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("searchNotifiche")
	public Response searchNotifiche(NotificheEventoFilterDO filter) throws AppException {
		try {
			List<NotificheEvento> notifiche = eventDelegateService.searchNotifiche(filter);
			return Response.ok(notifiche).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING saveEvent", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING saveEvent", e.getMessage(), "");
		}
	}

}
