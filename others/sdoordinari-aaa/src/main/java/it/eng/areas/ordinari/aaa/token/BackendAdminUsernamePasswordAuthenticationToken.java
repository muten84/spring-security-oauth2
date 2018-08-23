/**
 * 
 */
package it.eng.areas.ordinari.aaa.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author Bifulco Luigi
 *
 */
public class BackendAdminUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
	public BackendAdminUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}
}
