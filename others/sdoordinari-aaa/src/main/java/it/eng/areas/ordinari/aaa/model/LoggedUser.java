/**
 * 
 */
package it.eng.areas.ordinari.aaa.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import io.swagger.annotations.ApiParam;

/**
 * @author Bifulco Luigi
 *
 */
public class LoggedUser extends AbstractLoggedUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8935189376585967671L;

	@ApiParam(hidden = true)
	private String username;

	@ApiParam(hidden = true)
	private String token;

	public LoggedUser(boolean nonExpired, boolean credentialsNonExpired, boolean nonLocked, boolean enabled, List<? extends GrantedAuthority> auths) {
		super(nonExpired, credentialsNonExpired, nonLocked, enabled, auths);
	}

	public LoggedUser() {
		super(true, true, true, true, null);
		this.username = "not valid user";
	}

	/**
	 * @param username
	 */
	public LoggedUser(String username) {
		super(true, true, true, true, new ArrayList<>());
		this.username = username;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ordinari.aaa.model.AbstractLoggedUser#getAuthorities()
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Collection<? extends GrantedAuthority> auths =
		// super.getAuthorities();
		// if (auths == null) {
		// SimpleGrantedAuthority authority = new
		// SimpleGrantedAuthority("ROLE_USER");
		// List<SimpleGrantedAuthority> updatedAuthorities = new
		// ArrayList<SimpleGrantedAuthority>();
		// updatedAuthorities.add(authority);
		// return updatedAuthorities;
		// }
		// return auths;
		return super.getAuthorities();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities( )
	 */

}
