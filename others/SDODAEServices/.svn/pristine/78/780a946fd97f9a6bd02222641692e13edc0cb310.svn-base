package it.eng.areas.ems.sdodaeservices.rest.security;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtenteLoginDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.UserDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.entity.DaeMobileTokenDO;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;
import it.eng.areas.ems.sdodaeservices.service.AuthenticationService;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	private static Logger logger = Logger.getLogger(AuthenticationFilter.class);

	@Autowired
	private AuthenticationService authService;

	@Autowired
	private UserDelegateService userService;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		// Check if the HTTP Authorization header is present and formatted
		// correctly
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}
		// Extract the token from the HTTP Authorization header
		String token = authorizationHeader.substring("Bearer".length()).trim();
		try {
			// Validate the token
			DaeMobileTokenDO tokenDO = authService.getToken(token);
			if (tokenDO != null && tokenDO.getExpiredTimeStamp() == null) {
				Utente fr = userService.getUserById(UtenteLoginDepthRule.NAME, tokenDO.getUtenteId());
				requestContext.setSecurityContext(new DAEMobileAppSecurityContext(fr));
				authService.updateToken(token);

				logger.info("User " + fr.getLogon());

			} else {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING filter", e);
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}

	}

}