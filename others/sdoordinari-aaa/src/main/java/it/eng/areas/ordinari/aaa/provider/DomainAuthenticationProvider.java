/**
 * 
 */
package it.eng.areas.ordinari.aaa.provider;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import it.eng.areas.ordinari.aaa.business.AAAModuleTransactionalService;
import it.eng.areas.ordinari.aaa.model.AuthenticationWithTokenAndSession;
import it.eng.areas.ordinari.aaa.model.LoggedUser;
import it.eng.areas.ordinari.aaa.model.TokenSession;
import it.eng.areas.ordinari.aaa.token.CacheTokenService;
import it.eng.care.platform.authentication.api.model.Session;

/**
 * @author Bifulco Luigi
 *
 */
public class DomainAuthenticationProvider implements AuthenticationProvider {

	// @Autowired
	// private CacheTokenService tokenService;

	@Autowired
	private AAAModuleTransactionalService platformAAAService;

	public DomainAuthenticationProvider(CacheTokenService tokenService) {
		// this.tokenService = tokenService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Optional<String> username = (Optional) authentication.getPrincipal();
		Optional<String> password = (Optional) authentication.getCredentials();

		if (!username.isPresent() || !password.isPresent()) {
			throw new BadCredentialsException("Invalid Domain User Credentials");
		}

		// TODO: decommentare Session session =
		Session session = platformAAAService.authenticate(username.get(), password.get());
		// Session session = new Session();
		// session.setId(UUID.randomUUID().toString());

		if (session != null && !(StringUtils.isEmpty(session.getId()))) {
			TokenSession tokenSession = new TokenSession();
			// String newToken = tokenService.generateNewToken();
			// tokenSession.setToken(newToken);
			tokenSession.setSession(session);

			LoggedUser user = new LoggedUser(username.get());
			user.setUsername(username.get());
			// user.setToken(newToken);

			AuthenticationWithTokenAndSession resultOfAuthentication = new AuthenticationWithTokenAndSession(user,
					authentication.getCredentials());
			resultOfAuthentication.setAuthenticated(true);
			resultOfAuthentication.setToken(tokenSession);
			// tokenService.store(newToken, resultOfAuthentication);
			return resultOfAuthentication;
		} else {
			AuthenticationWithTokenAndSession resultOfAuthentication = new AuthenticationWithTokenAndSession(
					authentication, authentication.getCredentials());
			resultOfAuthentication.setAuthenticated(false);
			resultOfAuthentication.setToken(null);
			return resultOfAuthentication;
		}

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}