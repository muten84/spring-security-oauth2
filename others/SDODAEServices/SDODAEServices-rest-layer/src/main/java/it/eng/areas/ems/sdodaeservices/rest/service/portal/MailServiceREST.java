package it.eng.areas.ems.sdodaeservices.rest.service.portal;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import it.eng.areas.ems.sdodaeservices.delegate.DaeDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.EntityType;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplate;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTrace;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.MailTraceFilter;
import it.eng.areas.ems.sdodaeservices.rest.exception.AppException;
import it.eng.areas.ems.sdodaeservices.rest.model.MailTest;

@Component
@Path("/portal/mailService")
@Api(value = "/mailService", protocols = "http", description = "Servizio dedicato alla gestione delle Mail")
public class MailServiceREST {

	private Logger logger = LoggerFactory.getLogger(MailServiceREST.class);

	@Autowired
	protected MailDelegateService mailService;

	@Autowired
	protected DaeDelegateService daeService;

	@Autowired
	protected FirstResponderDelegateService frService;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllMailTemplate")
	@ApiOperation(value = "getAllMailTemplate", notes = "Metodo che restituisce la lista dei Template ")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Metodo che restituisce la lista dei Template", responseContainer = "List", response = MailTemplate.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllMailTemplate() throws AppException {
		try {
			List<MailTemplate> templates = mailService.getAllMailTemplate();
			return Response.ok(templates).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllMailTemplate", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("saveMailTemplate")
	@ApiOperation(value = "saveMailTemplate", notes = "Metodo per salvare un Template ")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Salvataggio Template andato a buon fine", response = MailTemplate.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response saveMailTemplate(@ApiParam(value = "Template da salvare") MailTemplate template)
			throws AppException {
		try {
			MailTemplate t = mailService.saveMailTemplate(template);
			return Response.ok(t).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllMailTemplate", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sendMail")
	@ApiOperation(value = "sendMail", notes = "Metodo per inviare una mail ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Mail inviata correttamente"),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response sendMail(@ApiParam(value = "Dati della mail da inviare") MailTest test) throws AppException {
		try {
			if (test.getEntityType() != null && test.getEntityId() != null) {
				Object entity = null;
				if (test.getEntityType() == EntityType.DAE) {
					entity = daeService.getDaeById(test.getEntityId());
				} else if (test.getEntityType() == EntityType.USER) {
					entity = frService.getUtenteById(test.getEntityId());
				}

				if (entity != null) {
					mailService.sendMail(test.getDestinatario(), entity, test.getMailTemplate(), test.getEntityType(),
							test.getEntityId());
				}

			} else {
				mailService.sendMail(test.getDestinatario(), test.getMail(), test.getObject());
			}
			return Response.ok().build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING sendMail", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("searchMailByFilter")
	@ApiOperation(value = "searchMailByFilter", notes = "Metodo per cercare le mail inviate")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "dati caricati dal db", response = MailTrace.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response searchMailByFilter(@ApiParam(value = "filtro di ricerca") MailTraceFilter filter)
			throws AppException {
		try {
			List<MailTrace> traces = mailService.searchMailTraceByFilter(filter);
			return Response.ok(traces).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchMailByFilter", e);
			return Response.serverError().build();
		}
	}
}
