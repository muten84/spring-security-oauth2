package it.eng.areas.ems.sdodaeservices.rest.service.portal;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.imageio.ImageIO;
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtenteDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtenteImageDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Gruppo;
import it.eng.areas.ems.sdodaeservices.delegate.model.Image;
import it.eng.areas.ems.sdodaeservices.delegate.model.Ruolo;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.exception.UserEmailAlreadyPresentException;
import it.eng.areas.ems.sdodaeservices.delegate.model.exception.UserLogonAlreadyPresentException;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.UtenteFilter;
import it.eng.areas.ems.sdodaeservices.rest.exception.AppException;
import it.eng.areas.ems.sdodaeservices.rest.exception.ErrorMessage;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;
import it.eng.areas.ems.sdodaeservices.rest.service.app.UserServiceREST;
import it.eng.areas.ems.sdodaeservices.rest.utils.FilterUtils;
import it.eng.areas.ems.sdodaeservices.rest.utils.ImageUtils;
import it.eng.areas.ems.sdodaeservices.rest.utils.ImageUtils.Size;
import it.eng.areas.ems.sdodaeservices.utils.PasswordUtils;

@Component
@Path("/portal/userService")
@Api(value = "/portal/userService", protocols = "http", description = "Servizio dedicato alla gestione degli utenti")
@RolesAllowed("USER")
public class UserPortalServiceREST extends UserServiceREST {

	private Logger logger = LoggerFactory.getLogger(UserPortalServiceREST.class);

	@Autowired
	private FilterUtils filterUtils;

	@Autowired
	private PasswordUtils passwordUtils;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("saveUtenteProfilo")
	@ApiOperation(value = "APPLICATION_JSON", notes = "Metodo che consente di salvare i dati dell'utente loggato")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Utente.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	@Secured
	public Response saveUtenteProfilo(@Context HttpServletRequest httpServletRequest, Utente utente) {
		try {
			Utente logged = (Utente) secContext.getUserPrincipal();

			utente.setId(logged.getId());
			// salvo l'utente
			utente = userDelegateService.updateUtente(utente);
			// sostituisco i dati dell'immagine
			utente = processUser(httpServletRequest, utente);
			// restituisco i dati al client
			return Response.ok(utente).build();

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING saveUtenteProfilo", e);
			ErrorMessage message = new ErrorMessage();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("saveUtente")
	@ApiOperation(value = "APPLICATION_JSON", notes = "Metodo che consente di salvare i dati dell'utente")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Utente.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	@Secured
	@RolesAllowed("LOCAL_ADMIN")
	public Response saveUtente(@Context HttpServletRequest httpServletRequest, Utente utente) {
		try {
			Utente logged = (Utente) secContext.getUserPrincipal();
			if (filterUtils.isEnabledToSave(logged, utente)) { // salvo l'utente
				if (utente.getPassword() == null || passwordUtils.checkStrength(utente.getPassword())) {
					utente = userDelegateService.saveUtente(utente);
					// sostituisco i dati dell'immagine
					utente = processUser(httpServletRequest, utente);
					// restituisco i dati al client
					return Response.ok(utente).build();
				} else {
					GenericResponse ndr = new GenericResponse();
					ndr.setError(true);
					ndr.setMessage(
							"La password deve avere lunghezza minima 8 caratteri e contenere numeri, lettere o caratteri speciali (%,&,$,£)");
					return Response.ok(ndr).build();
				}
			} else {
				GenericResponse ndr = new GenericResponse();
				ndr.setError(true);
				ndr.setMessage("Utente non autrizzato al salvataggio dell'utente in quel Comune");
				return Response.ok(ndr).build();
			}

		} catch (UserEmailAlreadyPresentException e) {
			logger.warn("Email " + utente.getEmail() + " already present on DB");
			ErrorMessage message = new ErrorMessage("Email " + utente.getEmail() + " già presente sul DB");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build();
		} catch (UserLogonAlreadyPresentException e) {
			logger.warn("Utente " + utente.getLogon() + " already present on DB");
			ErrorMessage message = new ErrorMessage("Utente " + utente.getLogon() + " già presente sul DB");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING saveUtente", e);
			return Response.serverError().build();

		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllRuoli")
	@ApiOperation(value = "APPLICATION_JSON", notes = "Metodo che consente di caricare la lista dei Ruoli")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Ruolo.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	@Secured
	@RolesAllowed("LOCAL_ADMIN")
	public Response getAllRuoli() {
		try {
			List<Ruolo> ruoli = userDelegateService.getAllRuoli();

			// filtro i ruoli in base all'utente
			List<Ruolo> ruoliToRet = new ArrayList<>();
			ruoli.forEach(r -> {
				// se l'utente ha quel ruolo può creare utenti con lo stesso
				// livello
				if (secContext.isUserInRole(r.getNome())) {
					ruoliToRet.add(r);
				}
			});

			return Response.ok(ruoliToRet).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllRuoli", e);
			return Response.serverError().build();

		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllGruppi")
	@ApiOperation(value = "APPLICATION_JSON", notes = "Metodo che consente di caricare la lista dei Gruppi")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Ruolo.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	@Secured
	@RolesAllowed("LOCAL_ADMIN")
	public Response getAllGruppi() {
		try {
			List<Gruppo> gruppi = userDelegateService.getAllGruppi(filterUtils.getProvinces(secContext),
					filterUtils.getMunicipalities(secContext));
			return Response.ok(gruppi).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllGruppi", e);
			return Response.serverError().build();

		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("saveGruppo")
	@ApiOperation(value = "APPLICATION_JSON", notes = "Metodo che consente di salvare i dati di un gruppo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Gruppo.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	@Secured
	@RolesAllowed("REGIONAL_ADMIN")
	public Response saveGruppo(Gruppo gruppo) {
		try {
			// salvo l'utente
			gruppo = userDelegateService.saveGruppo(gruppo);

			// restituisco i dati al client
			return Response.ok(gruppo).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING saveGruppo", e);
			return Response.serverError().build();

		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getLoggedUtenteDetails")
	@ApiOperation(value = "APPLICATION_JSON", notes = "Metodo che consente di ottenere le informazionoi relative all'utente Loggato")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Utente.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	@Secured
	public Response getLoggedUtenteDetails(@Context HttpServletRequest httpServletRequest) {
		try {
			// prendo l'utente loggato
			Utente u = (Utente) secContext.getUserPrincipal();
			// carico gli altri dati dal db
			u = userDelegateService.getUserById(UtenteDeepDepthRule.NAME, u.getId());
			// processo l'immagine
			u = processUser(httpServletRequest, u);
			// restituisco i dati al client
			return Response.ok(u).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getLoggedUtenteDetails", e);
			return Response.serverError().build();

		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("searchUtenteByFilter")
	@ApiOperation(value = "APPLICATION_JSON", notes = "Metodo che consente di cercare gli utenti tramite filtro")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Utente.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	@Secured
	public Response searchUtenteByFilter(@Context HttpServletRequest httpServletRequest, UtenteFilter filter) {
		try {

			filterUtils.addProvinceToFilter(secContext, filter);

			filter.setFetchRule(UtenteDeepDepthRule.NAME);
			filter.setEmailLike(true);

			List<Utente> users = userDelegateService.searchUtenteByFilter(filter);

			filterUtils.setReadOnly(secContext, users);

			return Response.ok(processUserList(httpServletRequest, users)).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchUtenteByFilter", e);
			return Response.serverError().build();

		}
	}

	@GET
	@Path("/getUserImg/{userID}")
	@Produces("image/png")
	public Response getFullImageUtente(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse, @PathParam("userID") String userID,
			@DefaultValue("MEDIUM") @QueryParam("size") Size size) {
		try {
			Utente localFR = userDelegateService.getUserById(UtenteImageDepthRule.NAME, userID);
			if (localFR != null && localFR.getImmagine() != null
					&& !StringUtils.isEmpty(localFR.getImmagine().getData())) {
				BufferedImage retImg = ImageUtils.processImage(httpServletRequest, localFR.getImmagine(), userID, size);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(retImg, "png", baos);
				byte[] imageData = baos.toByteArray();
				return Response.ok(imageData).build();
			} else {
				Image img = userDelegateService.getDefaultImmagine();
				BufferedImage retImg = ImageUtils.processImage(httpServletRequest, img, userID, size);
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

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("uploadImmagine/{id}")
	@Secured
	public Response uploadImmagine(@PathParam(value = "id") String id, @Context HttpServletRequest request,
			@Context HttpServletResponse httpServletResponse) throws AppException {
		try {
			String encode = ImageUtils.extractAndEncode(request, 1000, 1000);

			userDelegateService.updateImmagineUtente(id, encode);
			ImageUtils.cleanImage(request, id);

			return Response.ok().build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING uploadImmagine", e);
			return Response.serverError().build();
		}
	}

	private List<Utente> processUserList(HttpServletRequest httpServletRequest, List<Utente> users) {
		List<Utente> result = new ArrayList<Utente>();
		for (Utente u : users) {
			result.add(processUser(httpServletRequest, u));
		}
		return result;
	}

	private Utente processUser(HttpServletRequest httpServletRequest, Utente user) {
		Image img = new Image();
		img.setData("");
		String path = httpServletRequest.getContextPath() + "/rest/portal/userService/getUserImg/" + user.getId();
		img.setUrl(path);
		user.setImmagine(img);

		return user;
	}

}
