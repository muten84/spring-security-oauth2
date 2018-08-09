/**
 * 
 */
package it.eng.areas.ordinari.aaa.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * @author Bifulco Luigi
 *
 */
public class AuthenticationWithTokenAndSession extends PreAuthenticatedAuthenticationToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7627962915164413176L;

	public AuthenticationWithTokenAndSession(Object aPrincipal, Object aCredentials) {
		super(aPrincipal, aCredentials);
	}

	public AuthenticationWithTokenAndSession(Object aPrincipal, Object aCredentials,
			Collection<? extends GrantedAuthority> anAuthorities) {
		super(aPrincipal, aCredentials, anAuthorities);
	}

	public void setToken(TokenSession token) {
		setDetails(token);
	}

	public TokenSession getToken() {
		return (TokenSession) getDetails();
	}
}