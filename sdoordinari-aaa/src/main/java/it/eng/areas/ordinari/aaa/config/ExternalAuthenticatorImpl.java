/**
 * 
 */
package it.eng.areas.ordinari.aaa.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import it.eng.areas.ordinari.aaa.model.AuthenticationWithTokenAndSession;

/**
 * @author Bifulco Luigi
 *
 */
public class ExternalAuthenticatorImpl implements ExternalAuthenticator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ordinari.aaa.config.ExternalServiceAuthenticator#
	 * authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public AuthenticationWithTokenAndSession authenticate(String username, String password) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return null;

	}

}
