package it.eng.areas.ems.sdodaeservices.rest.service.portal;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdodaeservices.delegate.model.AppSession;
import it.eng.areas.ems.sdodaeservices.delegate.model.CredentialsBean;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.service.AuthenticationDelegateService;
import it.eng.areas.ems.sdodaeservices.rest.exception.AppException;
import it.eng.areas.ems.sdodaeservices.rest.model.SidebarMenu;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;
import it.eng.areas.ems.sdodaeservices.rest.service.app.AuthenticationServiceREST;

@Component
@Path("/portal/authenticationService")
@Api(value = "/portal/authenticationService", protocols = "http", description = "Servizio di autenticazione utente")
public class AuthenticationPortalServiceREST extends AuthenticationServiceREST {

	@Autowired
	private AuthenticationDelegateService authDeleService;

	@Value("${sidebarConfigFile}")
	private String sidebarConfigFile;

	@Context
	private SecurityContext secContext;

	private Logger logger = LoggerFactory.getLogger(AuthenticationDelegateService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdodaeservices.rest.service.portal.
	 * AuthenticationPortalServiceREST#authenticateUserPortal(it.eng.areas.ems.
	 * sdodaeservices.delegate.model.CredentialsBean)
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("authenticateUserPortal")
	@ApiOperation(value = "authenticateUserPortal", notes = "Metodo di autenticazione utente per portale gestionale. Restituisce il token da utilizzare per le comunicazioni sicure")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Utente loggato correttamente", response = AppSession.class),
			@ApiResponse(code = 500, message = "Errore durante l'autenticazione") })
	public Response authenticateUserPortal(
			@ApiParam(value = "Bean con credenziali utente") CredentialsBean credentials) {

		try {
			AppSession aS = authDeleService.authenticateUserPortal(credentials);
			if (aS != null) {
				if (aS.getToken() != null) {
					logger.info("User " + credentials.getUsername() + " authenticated");
				} else {
					logger.info("User " + credentials.getUsername() + " NOT authenticated");
				}
				return Response.ok(aS).build();
			} else {
				logger.info("User " + credentials.getUsername() + " NOT authenticated");
				return Response.serverError().build();

			}
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING authenticateUser", e);
			AppSession aS = new AppSession();
			aS.setError(true);
			aS.setMessage("Errore durante il processo di autenticazione");
			return Response.ok(aS).build();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdodaeservices.rest.service.portal.
	 * AuthenticationPortalServiceREST#logoutPortal(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("logoutPortal")
	@Secured
	@ApiOperation(value = "logoutPortal", notes = "Metodo per distruggere il token di autenticazione")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Logout Utente avvenuto correttamente", response = AppSession.class),
			@ApiResponse(code = 500, message = "Errore durante il logout") })
	public Response logoutPortal(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse) throws AppException {
		try { // Get the HTTP Authorization header from the request

			Utente user = (Utente) secContext.getUserPrincipal();
			logger.info("LOGOUT FOR USER: " + user.getId());

			String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
			// Extract the token from the HTTP Authorization header
			String token = authorizationHeader.substring("Bearer".length()).trim();

			authDeleService.expireToken(token);
			GenericResponse gr = new GenericResponse();
			gr.setResponse(true);
			gr.setMessage("Token expired successfully");
			return Response.ok().build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING logout", e);
			GenericResponse gr = new GenericResponse();
			gr.setResponse(false);
			gr.setMessage("Errore durante la logout");
			return Response.ok().build();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdodaeservices.rest.service.portal.
	 * AuthenticationPortalServiceREST#getSidebarJSON()
	 */

	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getSidebarJSON")
	@ApiOperation(value = "getSidebarJSON", notes = "Metodo di controllo della validità del token di autenticazione")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Utente loggato correttamente", response = SidebarMenu.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Errore durante l'autenticazione") })
	public Response getSidebarJSON() {

		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(sidebarConfigFile);

		Gson gs = new Gson();

		@SuppressWarnings("serial")
		List<SidebarMenu> menus = gs.fromJson(new InputStreamReader(stream), new TypeToken<List<SidebarMenu>>() {
		}.getType());

		List<SidebarMenu> toRet = new ArrayList<>();
		menus.forEach(m -> {
			if (secContext.isUserInRole(m.getRole())) {
				// creo un unovo oggetto menù a cui copio i dati di quello
				// caricato
				SidebarMenu newM = new SidebarMenu();
				newM.setIconClass(m.getIconClass());
				newM.setTarget(m.getTarget());
				newM.setTitle(m.getTitle());
				// carico solo i figli che quell'utente può vedere
				m.getChilds().forEach(c -> {
					if (secContext.isUserInRole(c.getRole())) {
						newM.getChilds().add(c);
					}
				});
				// aggiungo l'elemento alla lista
				toRet.add(newM);
			}
		});

		return Response.ok(toRet).build();
	}

}
