package it.eng.areas.ems.sdodaeservices.rest.service.app;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.AppSession;
import it.eng.areas.ems.sdodaeservices.delegate.model.CredentialsBean;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplateEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.OperationResult;
import it.eng.areas.ems.sdodaeservices.delegate.model.ResetPasswordBean;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.FirstResponderFilter;
import it.eng.areas.ems.sdodaeservices.delegate.service.AuthenticationDelegateService;
import it.eng.areas.ems.sdodaeservices.entity.FRStatoProfiloEnum;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.rest.exception.AppException;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;

@Component
@Path("/authenticationService")
@Api(value = "/authenticationService", protocols = "http", description = "Servizio di autenticazione utente")
public class AuthenticationServiceREST {

	@Autowired
	private AuthenticationDelegateService authDeleService;

	@Autowired
	private FirstResponderDelegateService frDelegateService;

	@Autowired
	private MailDelegateService mailDelegateService;

	@Value("${sidebarConfigFile}")
	private String sidebarConfigFile;

	@Context
	private SecurityContext secContext;

	private Logger logger = LoggerFactory.getLogger(AuthenticationDelegateService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdodaeservices.rest.service.app.
	 * IAuthenticationServiceREST#
	 * authenticateUser(it.eng.areas.ems.sdodaeservices.delegate.model.
	 * CredentialsBean)
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("authenticateUser")
	@ApiOperation(value = "authenticateUser", notes = "Metodo di autenticazione utente. Restituisce il token da utilizzare per le comunicazioni sicure")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Utente loggato correttamente", response = AppSession.class),
			@ApiResponse(code = 500, message = "Errore durante l'autenticazione") })
	public Response authenticateUser(@ApiParam(value = "Bean con credenziali utente") CredentialsBean credentials) {
		try {
			// mauro - controllo se l'utente è ancora da attivare
			FirstResponderFilter firstResponderFilter = new FirstResponderFilter();
			firstResponderFilter.setLogon(credentials.getUsername());
			List<FirstResponder> list = frDelegateService.searchFirstResponderByFilter(firstResponderFilter);

			if (list != null && list.size() > 0
					&& list.get(0).getStatoProfilo() == FRStatoProfiloEnum.IN_ATTESA_DI_ATTIVAZIONE) {
				AppSession aS = new AppSession();
				aS.setError(true);
				aS.setMessage("Utente non abilitato, terminare la procedura tramite mail");
				return Response.ok(aS).build();
			}

			AppSession aS = authDeleService.authenticateUser(credentials);
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
	 * @see it.eng.areas.ems.sdodaeservices.rest.service.app.
	 * IAuthenticationServiceREST#
	 * resetPassword(it.eng.areas.ems.sdodaeservices.delegate.model.
	 * ResetPasswordBean, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("resetPassword")
	@ApiOperation(value = "resetPassword", notes = "Metodo per il reset della password dell'utente (First Responder)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Password resettata correttamente", response = GenericResponse.class),
			@ApiResponse(code = 500, message = "Errore durante il reset della password") })
	public Response resetPassword(@ApiParam(value = "Bean contenente la mail dell'utente") ResetPasswordBean resetBean,
			@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) {
		GenericResponse gr = new GenericResponse();
		try {

			OperationResult res = authDeleService.resetPassword(resetBean, httpServletRequest.getRemoteAddr());
			gr.setResponse(res.isResponse());
			gr.setMessage(res.getMessage());
			return Response.ok(gr).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING resetPassword", e);
			gr.setResponse(false);
			gr.setMessage("Errore generico durante il reset della password");
			return Response.ok(gr).build();
		}
	}

	// faccio scattare il filtro sull'autenticazione
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdodaeservices.rest.service.app.
	 * IAuthenticationServiceREST# isAuthenticated()
	 */

	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("isAuthenticated")
	@ApiOperation(value = "isAuthenticated", notes = "Metodo di controllo della validità del token di autenticazione")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Utente loggato correttamente", response = AppSession.class),
			@ApiResponse(code = 500, message = "Errore durante l'autenticazione") })
	public Response isAuthenticated() {
		return Response.ok().build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdodaeservices.rest.service.app.
	 * IAuthenticationServiceREST# logout(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("logout")
	@Secured
	@ApiOperation(value = "logout", notes = "Metodo per distruggere il token di autenticazione")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Logout Utente avvenuto correttamente", response = AppSession.class),
			@ApiResponse(code = 500, message = "Errore durante il logout") })
	public Response logout(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse) throws AppException {
		try { // Get the HTTP Authorization header from the request

			Utente user = (Utente) secContext.getUserPrincipal();
			logger.info("LOGOUT FOR USER: " + user.getId());

			String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
			// Extract the token from the HTTP Authorization header
			String token = authorizationHeader.substring("Bearer".length()).trim();
			Utente u = (Utente) secContext.getUserPrincipal();
			FirstResponder fr = frDelegateService.getFirstResponderByUserId(FirstResponderDeepDepthRule.NAME,
					u.getId());
			// frDelegateService.updateAvailability(fr.getId(), false);
			// pulisco il dispositivo
			frDelegateService.cleanDispositivo(fr.getDispositivo().getId());

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
	 * @see it.eng.areas.ems.sdodaeservices.rest.service.app.
	 * IAuthenticationServiceREST# allowReset(java.lang.String)
	 */

	@GET
	@Produces("text/html; charset=UTF-8")
	@Path("allowReset/{token}")
	@ApiOperation(value = "allowReset", notes = "Metodo usato dai client per autorizzare il reset della password")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione avvenuta correttamente"),
			@ApiResponse(code = 500, message = "Errore durante l'operazione") })
	public Response allowReset(@PathParam("token") String token) throws AppException {
		String template;
		try {
			UtenteDO user = authDeleService.allowResetPassword(token);
			// se tutto è andato a buon fine
			if (user != null) {
				template = mailDelegateService.prepareMailText(user, MailTemplateEnum.PASSWORD_CHANGED_HTML_TEMPLATE);
			} else {
				template = mailDelegateService.prepareMailText(null,
						MailTemplateEnum.PASSWORD_CHANGED_ERROR_HTML_TEMPLATE);
			}

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING logout", e);
			template = mailDelegateService.prepareMailText(null, MailTemplateEnum.PASSWORD_CHANGED_ERROR_HTML_TEMPLATE);
		}
		return Response.ok("<html><body>" + template + "</body></html>").build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdodaeservices.rest.service.app.
	 * IAuthenticationServiceREST# denyReset(java.lang.String)
	 */

	@GET
	@Produces("text/html; charset=UTF-8")
	@Path("denyReset/{token}")
	@ApiOperation(value = "denyReset", notes = "Metodo usato dai client per annullare il reset della password")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione avvenuta correttamente"),
			@ApiResponse(code = 500, message = "Errore durante l'operazione") })
	public Response denyReset(@PathParam("token") String token) throws AppException {

		String template;
		try {
			UtenteDO user = authDeleService.denyResetPassword(token);

			if (user != null) {
				// se tutto è andato a buon fine
				template = mailDelegateService.prepareMailText(user,
						MailTemplateEnum.PASSWORD_NOT_CHANGED_HTML_TEMPLATE);
			} else {
				// in caso di errore
				template = mailDelegateService.prepareMailText(null,
						MailTemplateEnum.PASSWORD_CHANGED_ERROR_HTML_TEMPLATE);
			}
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING logout", e);
			// in caso di errore
			template = mailDelegateService.prepareMailText(null, MailTemplateEnum.PASSWORD_CHANGED_ERROR_HTML_TEMPLATE);
		}
		return Response.ok("<html><body>" + template + "</body></html>").build();
	}

}
