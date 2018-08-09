/**
 * 
 */
package it.eng.areas.ordinari.aaa.business.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import it.eng.areas.ordinari.aaa.business.AAAModuleTransactionalService;
import it.eng.areas.ordinari.aaa.caching.PreAuthenticatedUserCache;
import it.eng.areas.ordinari.aaa.model.LoggedUser;
import it.eng.care.platform.authentication.api.model.Role;
import it.eng.care.platform.authentication.api.model.Session;
import it.eng.care.platform.authentication.api.model.User;
import it.eng.care.platform.authentication.api.model.filter.UserFilter;

/**
 * @author Bifulco Luigi
 *
 */
// @Component
public class TokenAuthUserDetailsService
		implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

	private int passwordExpiresIn;

	// private AuthenticationService authService;
	//
	// private UserService userService;
	//
	// private RoleService roleService;

	private AAAModuleTransactionalService dbService;

	private PreAuthenticatedUserCache cache;

	public TokenAuthUserDetailsService(int passwordExpireInDays, AAAModuleTransactionalService dbService,
			PreAuthenticatedUserCache cache) {
		// this.authService = authService;
		this.passwordExpiresIn = passwordExpireInDays;
		this.dbService = dbService;
		this.cache = cache;
		// this.userService = userService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.
	 * AuthenticationUserDetailsService#loadUserDetails(org.springframework.
	 * security.core.Authentication)
	 */
	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		String username = token.getPrincipal().toString();
		if (cache.contains(username)) {
			return cache.retrieveUser(username);
		}

		// Session s = authService.getSessionLastByUsername(username);
		Session s = null;
		boolean sessionOk = true;
		try {
			s = dbService.getCurrentValidSession(username);
		} catch (BadCredentialsException e) {
			sessionOk = false;
		}

		if (s == null) {
			sessionOk = false;
		}

		UserFilter filter = new UserFilter();
		filter.setUsername(username);
		// User u = userService.searchUserByFilter(filter).get(0);
		User u = dbService.getUser(username);

		boolean enabled = u.getEnabled();

		/* check password scaduta */
		boolean credentialsOk = true;
		Date d = u.getPasswordChangeTimestamp();
		LocalDateTime time_init = LocalDateTime.now();

		LocalDate today = LocalDate.now();
		Calendar c = Calendar.getInstance();
		c.setTime(d);

		Instant lastChanged = d.toInstant();
		// Duration oneDay = Duration.between(today, yesterday); // throws an
		// exception

		ZonedDateTime zdt = lastChanged.atZone(ZoneId.systemDefault());
		LocalDate date = zdt.toLocalDate();
		long days = Duration.between(date.atTime(0, 0), today.atTime(0, 0)).toDays(); // another
																						// option
		if (days > passwordExpiresIn) {
			credentialsOk = false;
		}
		if (!sessionOk) {
			credentialsOk = false;
		}

		// u.getPasswordChangeTimestamp();
		// userService.getUser(userId)
		List<Role> roles = dbService.getUserRoles(username);
		List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		roles.forEach((r) -> {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(r.getDescription());

			updatedAuthorities.add(authority);
		});

		LoggedUser user = new LoggedUser(true, credentialsOk, enabled, enabled, updatedAuthorities);
		user.setUsername(username);
//		if (!cache.contains(username)) {
//			cache.storeUser(username, user);
//		}
		return user;
	}

}
