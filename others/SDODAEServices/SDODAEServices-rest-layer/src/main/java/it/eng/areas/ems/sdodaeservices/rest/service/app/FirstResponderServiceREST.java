package it.eng.areas.ems.sdodaeservices.rest.service.app;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
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
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderCertificatoImageRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderImageRule;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.UserDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.FRLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponderImageUpload;
import it.eng.areas.ems.sdodaeservices.delegate.model.GPSLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericException;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Image;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplateEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.PrivacyAgreement;
import it.eng.areas.ems.sdodaeservices.delegate.model.UpdateAvailabilityBean;
import it.eng.areas.ems.sdodaeservices.delegate.model.UpdateAvailabilityResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.FirstResponderFilter;
import it.eng.areas.ems.sdodaeservices.delegate.service.PrivacyDelegateService;
import it.eng.areas.ems.sdodaeservices.rest.exception.ErrorMessage;
import it.eng.areas.ems.sdodaeservices.rest.model.AvailabilityTimeRequest;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;
import it.eng.areas.ems.sdodaeservices.rest.service.UpdateFRLocationCallable;
import it.eng.areas.ems.sdodaeservices.rest.utils.ImageUtils;
import it.eng.areas.ems.sdodaeservices.rest.utils.ImageUtils.Size;
import it.eng.areas.ems.sdodaeservices.utils.PasswordUtils;

@Component
@Path("/frservice")
@Api(value = "/frservice", protocols = "http", description = "Servizio dedicato alla gestione dei First Responder")
public class FirstResponderServiceREST {

	private Logger logger = LoggerFactory.getLogger(FirstResponderServiceREST.class);

	@Autowired
	protected FirstResponderDelegateService frDelegateService;

	@Context
	private ServletContext context;

	@Context
	private SecurityContext secContext;

	@Autowired
	private UserDelegateService userService;

	@Autowired
	private PrivacyDelegateService privacyDelegateService;

	@Autowired
	private PasswordUtils passwordUtils;

	@Autowired
	private MailDelegateService mailDelegateService;

	private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getLoggedFirstResponderDetails")
	@ApiOperation(value = "APPLICATION_JSON", notes = "Metodo che consente di ottenere le informazionoi relative al First Responder Loggato")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = FirstResponder.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	@Secured
	public Response getLoggedFirstResponderDetails(@Context HttpServletRequest httpServletRequest) {
		try {

			Utente u = (Utente) secContext.getUserPrincipal();

			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderDeepDepthRule.NAME,
					u.getId());

			fr = processFR(httpServletRequest, fr);
			return Response.ok(fr).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getLoggedFirstResponderDetails", e);
			return Response.serverError().build();

		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("addNewFirstResponder")
	@ApiOperation(value = "addNewFirstResponder", notes = "Metodo che consente di registrare un nuovo First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = FirstResponder.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response addNewFirstResponder(@Context HttpServletRequest httpServletRequest,
			@ApiParam(value = "First Responder") FirstResponder firstResponder) {

		try {

			// Mauro - controllo se l'utente è già presente su DB
			FirstResponderFilter filter = new FirstResponderFilter();
			filter.setLogon(firstResponder.getEmail());
			List<FirstResponder> list = frDelegateService.searchFirstResponderByFilter(filter);
			if (list != null && !list.isEmpty()) {
				GenericResponse ndr = new GenericResponse();
				ndr.setError(true);
				ndr.setMessage("Email già utilizzata");
				return Response.ok(ndr).build();
			}

			// controllo la password inviata dall'app
			if (passwordUtils.checkStrength(firstResponder.getPassword())) {
				FirstResponder newFirstResponder = frDelegateService.saveFirstResponder(firstResponder, null);
				FirstResponder fr = processFR(httpServletRequest, newFirstResponder);

				return Response.ok(fr).build();
			} else {
				GenericResponse ndr = new GenericResponse();
				ndr.setError(true);
				ndr.setMessage(
						"La password deve avere lunghezza minima 8 caratteri e contenere numeri, lettere o caratteri speciali (%,&,$,£)");
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
	@Path("updateImageProfilo")
	@Secured
	@ApiOperation(value = "updateImageProfilo", notes = "Metodo che consente di aggiornare l'immagine profilo  un First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = FirstResponder.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response updateImageProfilo(@Context HttpServletRequest httpServletRequest,
			@ApiParam(value = "First Responder") FirstResponderImageUpload profilo) {

		try {
			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderDeepDepthRule.NAME,
					u.getId());
			fr = frDelegateService.updateImmagineProfilo(fr.getId(), profilo);
			fr = processFR(httpServletRequest, fr);
			return Response.ok(fr).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING updateImageProfilo", e);
			return Response.serverError().build();

		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("updateImageCertificato")
	@Secured
	@ApiOperation(value = "updateImageCertificato", notes = "Metodo che consente di aggiornare l'immagine del certificato  un First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = FirstResponder.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response updateImageCertificato(@Context HttpServletRequest httpServletRequest,
			@ApiParam(value = "First Responder") FirstResponderImageUpload profilo) {

		try {
			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderDeepDepthRule.NAME,
					u.getId());
			fr = frDelegateService.updateImmagineCertificato(fr.getId(), profilo);

			fr = processFR(httpServletRequest, fr);
			return Response.ok(fr).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING updateImageProfilo", e);
			return Response.serverError().build();

		}
	}

	protected FirstResponder processFR(HttpServletRequest httpServletRequest, FirstResponder fr) {
		Image img = new Image();
		img.setData("");
		String path = httpServletRequest.getContextPath() + "/rest/frservice/getFRImg/" + fr.getId();
		img.setUrl(path);
		fr.setImmagine(img);
		/*
		 * if (fr.getCertificatoFr() != null) { Image img1 = new Image();
		 * img1.setData(""); path = httpServletRequest.getContextPath() +
		 * "/rest/frservice/getFRCertImg/" + fr.getId(); img1.setUrl(path);
		 * fr.getCertificatoFr().setImmagine(img1); }
		 */
		return fr;
	}

	protected List<FirstResponder> processFRList(HttpServletRequest httpServletRequest, List<FirstResponder> frs) {
		List<FirstResponder> retFrs = new ArrayList<>();
		for (FirstResponder fr : frs) {
			retFrs.add(processFR(httpServletRequest, fr));
		}
		return retFrs;

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getCurrentPrivacyAgreement")
	@ApiOperation(value = "getCurrentPrivacyAgreement", notes = "Metodo che consente di ottenere il disclaimer attuale")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = PrivacyAgreement.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getCurrentPrivacyAgreement() {
		PrivacyAgreement pa = privacyDelegateService.getCurrentPrivacyAgreement();
		return Response.ok(pa).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("acceptPrivacyAgreement")
	@Secured
	@ApiOperation(value = "acceptPrivacyAgreement", notes = "Metodo che consente di accettare il disclaimer")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = PrivacyAgreement.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response acceptPrivacyAgreement() {
		try {
			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderDeepDepthRule.NAME,
					u.getId());

			GenericResponse gr = privacyDelegateService.acceptPrivacyAgreementForCurrentUser(fr);

			return Response.ok(gr).build();
		} catch (Exception e) {
			GenericResponse gr = new GenericResponse();
			gr.setMessage("Errore durante esecuzione acceptPrivacyAgreement");
			gr.setResponse(false);
			return Response.ok(gr).build();

		}
	}

	@GET
	@Path("/getFRImg/{frID}")
	@Produces("image/png")
	public Response getFullImageFR(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse, @PathParam("frID") String frID,
			@DefaultValue("MEDIUM") @QueryParam("size") Size size) {
		try {
			FirstResponder localFR = frDelegateService.getFirstResponderById(FirstResponderImageRule.NAME, frID);
			if (localFR != null && localFR.getImmagine() != null
					&& !StringUtils.isEmpty(localFR.getImmagine().getData())) {
				BufferedImage retImg = ImageUtils.processImage(httpServletRequest, localFR.getImmagine(), frID, size);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(retImg, "png", baos);
				byte[] imageData = baos.toByteArray();
				return Response.ok(imageData).build();
			} else {
				Image img = userService.getDefaultImmagine();
				BufferedImage retImg = ImageUtils.processImage(httpServletRequest, img, frID, size);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(retImg, "png", baos);
				byte[] imageData = baos.toByteArray();
				return Response.ok(imageData).build();
			}
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getFullImageFR", e);
			return Response.serverError().build();
		}

	}

	@GET
	@Path("/getFRCertImg/{frID}")
	@Produces("image/png")
	public Response getFullImageCertificatoFR(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse, @PathParam("frID") String frID,
			@DefaultValue("MEDIUM") @QueryParam("size") Size size) {

		FirstResponder localFR = frDelegateService.getFirstResponderById(FirstResponderCertificatoImageRule.NAME, frID);
		if (localFR != null && localFR.getCertificatoFr() != null && localFR.getCertificatoFr().getImmagine() != null
				&& !StringUtils.isEmpty(localFR.getCertificatoFr().getImmagine().getData())) {
			try {
				BufferedImage retImg = ImageUtils.processImage(httpServletRequest,
						localFR.getCertificatoFr().getImmagine(), frID, size);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(retImg, "png", baos);
				byte[] imageData = baos.toByteArray();
				return Response.ok(imageData).build();
			} catch (Exception e) {
				logger.error("ERROR WHILE EXECUTING getFullImageCertificatoFR", e);
				return Response.serverError().build();
			}
		}
		return Response.serverError().build();

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("updateAvailabilityFR")
	@Secured
	@ApiOperation(value = "updateAvailabilityFR", notes = "Metodo che consente di modificare la disponibilità di un First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = UpdateAvailabilityResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response updateAvailabilityFR(
			@ApiParam(value = "Bean di aggiornamento disponibilità") UpdateAvailabilityBean updateBean) {
		try {
			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderBareRule.NAME, u.getId());
			frDelegateService.updateAvailability(fr.getId(), updateBean.isAvailable());
			UpdateAvailabilityResponse resp = new UpdateAvailabilityResponse();
			resp.setSuccess(true);
			return Response.ok(resp).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING updateAvailabilityFR", e);
			UpdateAvailabilityResponse resp = new UpdateAvailabilityResponse();
			resp.setSuccess(false);
			return Response.ok(resp).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("updateFRLocation")
	@Secured
	@ApiOperation(value = "updateFRLocation", notes = "Metodo che consente di modificare la disponibilità di un First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = GenericResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response updateFRLocation(@ApiParam(value = "Bean di aggiornamento posizione") FRLocation location) {
		try {
			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderBareRule.NAME, u.getId());
			executor.submit(new UpdateFRLocationCallable(fr.getId(), location, frDelegateService));
			GenericResponse resp = new GenericResponse();
			resp.setMessage("OK");
			resp.setResponse(true);
			return Response.ok(resp).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING updateFRLocation", e);
			GenericResponse resp = new GenericResponse();
			resp.setMessage("FAIL");
			resp.setResponse(false);
			return Response.ok(resp).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("setDoNotDisturb")
	@Secured
	@ApiOperation(value = "setDoNotDisturb", notes = "Metodo che consente di modificare la disponibilità oraria di un First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = GenericResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response setDoNotDisturb(@ApiParam(value = "Bean di aggiornamento orario") AvailabilityTimeRequest request) {

		Utente u = (Utente) secContext.getUserPrincipal();
		FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderBareRule.NAME, u.getId());

		boolean result = frDelegateService.setDoNotDisturb(fr.getId(), request.getFrom(), request.getTo());

		GenericResponse resp = new GenericResponse();
		resp.setMessage("OK");
		resp.setResponse(result);
		return Response.ok(resp).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("removeDoNotDisturb")
	@Secured
	@ApiOperation(value = "removeDoNotDisturb", notes = "Metodo che consente di eliminare la disponibilità oraria di un First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = GenericResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response removeDoNotDisturb() {
		Utente u = (Utente) secContext.getUserPrincipal();
		FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderBareRule.NAME, u.getId());

		boolean result = frDelegateService.removeDoNotDisturb(fr.getId());

		GenericResponse resp = new GenericResponse();
		resp.setMessage("OK");
		resp.setResponse(result);
		return Response.ok(resp).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("setSilent")
	@Secured
	@ApiOperation(value = "setSilent", notes = "Metodo che consente impostare il silenzioso ")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = GenericResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response setSilent(@ApiParam(value = "Bean di aggiornamento orario") AvailabilityTimeRequest request) {
		GenericResponse resp = new GenericResponse();
		try {
			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderBareRule.NAME, u.getId());

			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			Date from = new Date(System.currentTimeMillis() - 60000);

			boolean result = frDelegateService.setSilent(fr.getId(), format.format(from), request.getTo());

			resp.setMessage("OK");
			resp.setResponse(result);

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING setSilent", e);
			resp.setMessage("Server Error");
			resp.setError(true);
		}
		return Response.ok(resp).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("removeSilent")
	@Secured
	@ApiOperation(value = "removeSilent", notes = "Metodo che consente di eliminare il silenzioso")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = GenericResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response removeSilent() {
		Utente u = (Utente) secContext.getUserPrincipal();
		FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderBareRule.NAME, u.getId());

		boolean result = frDelegateService.removeSilent(fr.getId());

		GenericResponse resp = new GenericResponse();
		resp.setMessage("OK");
		resp.setResponse(result);
		return Response.ok(resp).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sendPositionToCO")
	@Secured
	@ApiOperation(value = "sendPositionToCO", notes = "Metodo che consente di inviare la propria posizione alla Centrale Operativa")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = GenericResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response sendPositionToCO(@ApiParam(value = "Bean di aggiornamento posizione") GPSLocation request) {
		Boolean result = true;
		GenericResponse resp = new GenericResponse();
		logger.info("sendPositionToCO " + request.getLatitudine() + " - " + request.getLongitudine());
		try {
			// salvo la posizione sul db
			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderBareRule.NAME, u.getId());

			frDelegateService.saveFRPositionToCO(fr, request);
			resp.setMessage("OK");
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING sendPositionToCO", e);
			resp.setMessage("Server Error");
			result = false;
		}

		resp.setResponse(result);
		return Response.ok(resp).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("saveProfile")
	@ApiOperation(value = "APPLICATION_JSON", notes = "Metodo che consente di salvare i dati dell'utente loggato")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Utente.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	@Secured
	public Response saveProfile(@Context HttpServletRequest httpServletRequest,
			@ApiParam(value = "Bean contenente i dati del First Responder") FirstResponder fr) {
		try {
			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder frLogged = frDelegateService.getFirstResponderByUserId(FirstResponderBareRule.NAME,
					u.getId());

			fr.setId(frLogged.getId());
			// salvo l'utente
			fr = frDelegateService.updateFRProfile(fr);
			// sostituisco i dati dell'immagine
			fr = processFR(httpServletRequest, fr);
			// restituisco i dati al client
			return Response.ok(fr).build();

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING saveProfile", e);
			ErrorMessage message = new ErrorMessage();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build();
		}
	}

	@GET
	@Produces("text/html; charset=UTF-8")
	@Path("activateProfile/{frID}")
	public Response activateProfile(@PathParam("frID") String frID) {
		String template;
		try {
			FirstResponder user = frDelegateService.activateProfile(frID);
			// se tutto è andato a buon fine
			if (user != null) {
				template = mailDelegateService.prepareMailText(user, MailTemplateEnum.FR_ACTIVATED_HTML_TEMPLATE);
			} else {
				template = mailDelegateService.prepareMailText(null, MailTemplateEnum.FR_NOT_ACTIVATED_HTML_TEMPLATE);
			}
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING logout", e);
			template = mailDelegateService.prepareMailText(null, MailTemplateEnum.FR_ACTIVATION_ERROR_HTML_TEMPLATE);
		}
		return Response.ok("<html><body>" + template + "</body></html>").build();
	}
}
