/**
 * 
 */

package it.eng.areas.ordinari.aaa.business.impl;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.areas.ordinari.aaa.business.AAAModuleTransactionalService;
import it.eng.care.platform.authentication.api.model.Role;
import it.eng.care.platform.authentication.api.model.Session;
import it.eng.care.platform.authentication.api.model.User;
import it.eng.care.platform.authentication.api.model.filter.UserFilter;
import it.eng.care.platform.authentication.api.service.AuthenticationService;
import it.eng.care.platform.authentication.api.service.AuthorizationService;
import it.eng.care.platform.authentication.api.service.RoleService;
import it.eng.care.platform.authentication.api.service.UserService;

@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class AAAModuleTransactionalServiceImpl implements AAAModuleTransactionalService {

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Override
	public Session authenticate(String username, String pwd) {		
		Session session = authenticationService.authenticate(username, pwd);
		return session;
	}

	@Override
	public boolean closeSession(String sessionId) {
		return authenticationService.closeSession(sessionId);
	}

	@Override
	public Session getCurrentValidSession(String username) {
		Session s = authenticationService.getSessionLastByUsername(username);

		if (s == null) {
			throw new BadCredentialsException("Session for user: " + username + " is no more exists");
		}
		Date end = s.getSessionEnd();
		
		if (end==null || end.toInstant().isBefore(Instant.now())) {
			throw new BadCredentialsException("Session for user: " + username + " is expired");
		}

		return s;
	}

	@Override
	public Session getCurrentSession(String username) {
		Session s = authenticationService.getSessionLastByUsername(username);

		return s;
	}

	@Override
	public User getUser(String username) {
		UserFilter filter = new UserFilter();
		filter.setUsername(username);
		List<User> users = userService.searchUserByFilter(filter);
		return users.get(0);
	}

	@Override
	public List<Role> getUserRoles(String username) {
		User u = new User();
		u.setUsername(username);
		return roleService.getUserRoles(u);
	}

}