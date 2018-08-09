/**
 * 
 */
package it.eng.areas.ordinari.aaa.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.annotations.ApiParam;

/**
 * @author Bifulco Luigi
 *
 */
public abstract class AbstractLoggedUser implements UserDetails {
	
	@ApiParam(hidden = true)
	protected boolean nonExpired; // nonExpired

	@ApiParam(hidden = true)
	protected boolean credentialsNonExpired; // nonExpired
	
	@ApiParam(hidden = true)
	protected boolean nonLocked; // nonExpired
	
	@ApiParam(hidden = true)
	protected boolean enabled; // nonExpired
	
	@ApiParam(hidden = true)
	protected List<? extends GrantedAuthority> authorities;

	// /**
	// * @param nonExpired
	// * @param credentialsNonExpired
	// * @param nonLocked
	// * @param enabled
	// */
	// public AbstractLoggedUser(boolean nonExpired, boolean
	// credentialsNonExpired, boolean nonLocked, boolean enabled) {
	// super();
	// this.nonExpired = nonExpired;
	// this.credentialsNonExpired = credentialsNonExpired;
	// this.nonLocked = nonLocked;
	// this.enabled = enabled;
	// }

	public AbstractLoggedUser(boolean nonExpired, boolean credentialsNonExpired, boolean nonLocked, boolean enabled, List<? extends GrantedAuthority> auths) {
		super();
		this.nonExpired = nonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.nonLocked = nonLocked;
		this.enabled = enabled;
		this.authorities = auths;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails# isAccountNonExpired()
	 */
	@Override
	public boolean isAccountNonExpired() {
		return nonExpired;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails# isCredentialsNonExpired()
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails# isAccountNonLocked()
	 */
	@Override
	public boolean isAccountNonLocked() {
		return nonLocked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return "***";
	}

}
