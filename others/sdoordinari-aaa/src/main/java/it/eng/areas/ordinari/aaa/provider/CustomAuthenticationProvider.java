/**
 * 
 */
package it.eng.areas.ordinari.aaa.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import it.eng.areas.ordinari.aaa.business.AAAModuleTransactionalService;
import it.eng.areas.ordinari.aaa.model.LoggedUser;
import it.eng.care.platform.authentication.api.model.Role;
import it.eng.care.platform.authentication.api.model.Session;

/**
 * @author Bifulco Luigi
 *
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private AAAModuleTransactionalService platformAAAService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Optional<Object> username = Optional.of(authentication.getPrincipal());
		Optional<Object> password = Optional.of(authentication.getCredentials());

		if (!username.isPresent() || !password.isPresent()) {
			throw new BadCredentialsException("Invalid Domain User Credentials");
		}

		// TODO: decommentare Session session =
		Session session = platformAAAService.authenticate(username.get().toString(), password.get().toString());

		if (session != null && !(StringUtils.isEmpty(session.getId()))) {
			// TODO caricare i ruoli da DB

			List<Role> roles = platformAAAService.getUserRoles(username.get().toString());
			List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
			roles.forEach((r) -> {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(r.getDescription());

				updatedAuthorities.add(authority);
			});

			// SimpleGrantedAuthority authority = new
			// SimpleGrantedAuthority("ROLE_USER");
			// List<SimpleGrantedAuthority> updatedAuthorities = new
			// ArrayList<SimpleGrantedAuthority>();
			// updatedAuthorities.add(authority);

			LoggedUser user = new LoggedUser(username.get().toString());
			user.setUsername(username.get().toString());
			UsernamePasswordAuthenticationToken tok = new UsernamePasswordAuthenticationToken(user, password.get(),
					updatedAuthorities);

			tok.setDetails(user);
			return tok;
		}

		return null;
		// tok.setAuthenticated(true);

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
