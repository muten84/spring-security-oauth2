package it.eng.areas.ems.sdodaeservices.rest.service.portal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.PushNotificationService;
import it.eng.areas.ems.sdodaeservices.delegate.model.EntityType;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericException;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Image;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplateEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.FirstResponderFilter;
import it.eng.areas.ems.sdodaeservices.entity.FRStatoProfiloEnum;
import it.eng.areas.ems.sdodaeservices.rest.model.UpdateFRAvailabilityBean;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;
import it.eng.areas.ems.sdodaeservices.rest.service.app.FirstResponderServiceREST;
import it.eng.areas.ems.sdodaeservices.rest.utils.FilterUtils;

@Component
@Path("/portal/frservice")
@Api(value = "/portal/frservice", protocols = "http", description = "Servizio dedicato alla gestione dei First Responder")
public class FirstResponderPortalServiceREST extends FirstResponderServiceREST {

	private Logger logger = LoggerFactory.getLogger(FirstResponderPortalServiceREST.class);

	@Autowired
	protected FirstResponderDelegateService frDelegateService;

	@Context
	private ServletContext context;

	@Context
	private SecurityContext secContext;

	@Autowired
	private MailDelegateService mailService;

	@Autowired
	private PushNotificationService notificationService;

	@Autowired
	private FilterUtils filterUtils;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("saveFirstResponder")
	@Secured
	@ApiOperation(value = "saveFirstResponder", notes = "Metodo che consente di modificare un First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = FirstResponder.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response addNewFirstResponder(@Context HttpServletRequest httpServletRequest,
			@ApiParam(value = "First Responder") FirstResponder firstResponder) {

		try {
			Utente u = (Utente) secContext.getUserPrincipal();
			// se l'utente non è nullo si sta salvando dal portale
			if (filterUtils.isEnabledToSave(u, firstResponder)) {
				// se l'utente è abilitato al salvataggio su quella provincia
				FirstResponder newFirstResponder = frDelegateService.saveFirstResponder(firstResponder, u);
				FirstResponder fr = processFR(httpServletRequest, newFirstResponder);
				return Response.ok(fr).build();
			} else {
				GenericResponse ndr = new GenericResponse();
				ndr.setError(true);
				ndr.setMessage("Utente non autrizzato al salvataggio del First Responder in quel Comune");
				return Response.ok(ndr).build();
			}
		} catch (GenericException e) {
			logger.error("ERROR WHILE EXECUTING addNewFirstResponder", e);
			GenericResponse gr = new GenericResponse();
			gr.setResponse(false);
			gr.setMessage(e.getMessage());
			return Response.ok(gr).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING addNewFirstResponder", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("deleteFR")
	@Secured
	@ApiOperation(value = "deleteFR", notes = "Metodo che consente di ricercare i First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", responseContainer = "List", response = FirstResponder.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response deleteFR(FirstResponder fr) {
		try {
			Utente utente = (Utente) secContext.getUserPrincipal();

			boolean ret = frDelegateService.deleteLogicallyFR(fr, utente);
			GenericResponse ndr = new GenericResponse();
			ndr.setResponse(ret);
			return Response.ok(ndr).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING deleteDAE", e);
			GenericResponse ndr = new GenericResponse();
			ndr.setResponse(false);
			return Response.ok(ndr).build();

		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("searchFirstResponderByFilter")
	@Secured
	@ApiOperation(value = "searchFirstResponderByFilter", notes = "Metodo che consente di ricercare i First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", responseContainer = "List", response = FirstResponder.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response searchFirstResponderByFilter(@Context HttpServletRequest httpServletRequest,
			@ApiParam(value = "First Responder Filter") FirstResponderFilter filter) {
		try {

			filterUtils.addProvinceToFilter(secContext, filter);

			filter.setLogonLike(true);

			List<FirstResponder> firstResponders = frDelegateService.searchFirstResponderByFilter(filter);

			firstResponders = processFRList(httpServletRequest, firstResponders);

			filterUtils.setReadOnly(secContext, firstResponders);

			return Response.ok(firstResponders).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchFirstResponderByFilter", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllFR")
	@Secured
	@ApiOperation(value = "getAllFR", notes = "Metodo che consente di ottenere la lista di tutti i FirstResponder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", responseContainer = "List", response = FirstResponder.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione della ricerca") })
	public Response getAllFR(@Context HttpServletRequest httpServletRequest) {
		try {
			FirstResponderFilter filter = new FirstResponderFilter();
			filter.setFetchRule(FirstResponderDeepDepthRule.NAME);
			filterUtils.addProvinceToFilter(secContext, filter);

			List<FirstResponder> firstResponders = frDelegateService.searchFirstResponderByFilter(filter);

			firstResponders = processFRList(httpServletRequest, firstResponders);

			filterUtils.setReadOnly(secContext, firstResponders);

			return Response.ok(firstResponders).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllFR", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("updateFirstResponderProfileActivation")
	@ApiOperation(value = "APPLICATION_JSON", notes = "Metodo che consente di aggiornare")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = FirstResponder.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	@Secured
	public Response updateFirstResponderProfileActivation(@Context HttpServletRequest httpServletRequest,
			UpdateFRAvailabilityBean bean) {
		try {
			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderById(FirstResponderDeepDepthRule.NAME,
					bean.getFrID());
			logger.info("EXECUTING updateFirstResponderProfileActivation ID: " + bean.getFrID());

			List<FirstResponder> distlist = new ArrayList<>();
			distlist.add(fr);
			if (bean.isActive()) {
				fr.setStatoProfilo(FRStatoProfiloEnum.ATTIVO);
				// se attivo invio la mail
				mailService.sendMail(fr.getEmail(), fr, MailTemplateEnum.ENABLE_FR_MAIL_TEMPLATE, EntityType.USER,
						fr.getId());
			} else {
				fr.setStatoProfilo(FRStatoProfiloEnum.IN_ATTESA_DI_ATTIVAZIONE);

			}
			notificationService.notifyMessageToFirstResponderList("Aggiornamento del tuo stato profilo", distlist);

			fr = frDelegateService.saveFirstResponder(fr, u);
			fr = processFR(httpServletRequest, fr);
			return Response.ok(fr).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING updateFirstResponderProfileActivation", e);
			return Response.serverError().build();
		}
	}

	protected FirstResponder processFR(HttpServletRequest httpServletRequest, FirstResponder fr) {
		Image img = new Image();
		img.setData("");
		String path = httpServletRequest.getContextPath() + "/rest/portal/frservice/getFRImg/" + fr.getId();
		img.setUrl(path);
		fr.setImmagine(img);

		if (fr.getCertificatoFr() != null) {
			Image img1 = new Image();
			img1.setData("");
			path = httpServletRequest.getContextPath() + "/rest/portal/frservice/getFRCertImg/" + fr.getId();
			img1.setUrl(path);
			fr.getCertificatoFr().setImmagine(img1);
		}
		return fr;
	}

}
