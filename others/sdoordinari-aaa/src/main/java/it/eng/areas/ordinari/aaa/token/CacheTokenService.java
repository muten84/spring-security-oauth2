/**
 * 
 */
package it.eng.areas.ordinari.aaa.token;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;

import it.eng.areas.ordinari.aaa.model.AuthenticationWithTokenAndSession;
import it.eng.areas.ordinari.aaa.model.TokenSession;
import it.eng.care.platform.authentication.api.model.Session;
import it.eng.care.platform.authentication.api.service.AuthenticationService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @author Bifulco Luigi
 *
 */
public class CacheTokenService {

//	@Autowired
//	private AuthenticationService authService;
//
//	private static final Logger logger = LoggerFactory.getLogger(CacheTokenService.class);
//	private static final Cache restApiAuthTokenCache = CacheManager.getInstance().getCache("restApiAuthTokenCache");
//	public static final int HALF_AN_HOUR_IN_MILLISECONDS = 30 * 60 * 1000;
//
//	@Scheduled(fixedRate = HALF_AN_HOUR_IN_MILLISECONDS)
//	public void evictExpiredTokens() {
//		logger.info("Evicting expired tokens");
//		restApiAuthTokenCache.evictExpiredElements();
//	}
//
//	public String generateNewToken() {
//		return UUID.randomUUID().toString();
//	}
//
//	public void store(String token, Authentication authentication) {
//		restApiAuthTokenCache.put(new Element(token, authentication));
//	}
//
//	public boolean contains(String token) {
//		return restApiAuthTokenCache.get(token) != null;
//	}
//
//	public Authentication retrieve(String token) {
//		AuthenticationWithTokenAndSession auth = (AuthenticationWithTokenAndSession) restApiAuthTokenCache.get(token)
//				.getObjectValue();
//		return auth;
//	}
//
//	public Authentication remove(String token) {
//		AuthenticationWithTokenAndSession auth = (AuthenticationWithTokenAndSession) retrieve(token);
//		restApiAuthTokenCache.remove(token);
//		// authService.closeSession(auth.getToken().getSession().getId());
//		return auth;
//	}
//
//	public void renew(String token) {
//		TokenSession ts = getTokenSession(token);
//		Session s = authService.renewSession(ts.getSession().getId());
//		AuthenticationWithTokenAndSession a = (AuthenticationWithTokenAndSession) remove(token);
//		ts.setSession(s);
//		a.setToken(ts);
//		store(token, a);
//	}
//
//	public boolean close(String token) {
//		AuthenticationWithTokenAndSession auth = (AuthenticationWithTokenAndSession) remove(token);
//		return authService.closeSession(auth.getToken().getSession().getId());
//	}
//
//	private TokenSession getTokenSession(String token) {
//		AuthenticationWithTokenAndSession auth = (AuthenticationWithTokenAndSession) retrieve(token);
//		TokenSession ts = auth.getToken();
//		ts.getSession().getId();
//		return ts;
//	}
}
