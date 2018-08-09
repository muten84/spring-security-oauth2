/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest.authentication;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import it.esel.parsley.lang.StringUtils;

/**
 * @author Bifulco Luigi
 *
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
// Insipred by
// http://stackoverflow.com/questions/26777083/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey
public class AuthenticationFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Get the HTTP Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		// Check if the HTTP Authorization header is present and formatted correctly
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}

		// Extract the token from the HTTP Authorization header
		String token = authorizationHeader.substring("Bearer".length()).trim();

		try {

			// Validate the token
			validateToken(token);

		} catch (Exception e) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}

		requestContext.setSecurityContext(new SecurityContext() {

			@Override
			public Principal getUserPrincipal() {

				return new Principal() {

					@Override
					public String getName() {
						return Base64.decodeAsString(token).split(":")[0];
					}
				};
			}

			@Override
			public boolean isUserInRole(String role) {
				return true;
			}

			@Override
			public boolean isSecure() {
				return false;
			}

			@Override
			public String getAuthenticationScheme() {
				return null;
			}
		});

	}

	private void validateToken(String token) throws Exception {

		String decoded = Base64.decodeAsString(token);
		// decode try
		String valid = Base64.decodeAsString(token).split(":")[0];
		// token structure user:8randomchar+hash to ignore
		if (StringUtils.isEmpty(decoded)) {
			throw new RuntimeException("Token not valid");
		}
	}
}
