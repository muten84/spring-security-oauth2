/**
 * 
 */
package it.eng.areas.ordinari.aaa.caching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.eng.areas.ordinari.aaa.model.LoggedUser;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @author Bifulco Luigi
 *
 */
@Component
public class PreAuthenticatedUserCache {

	private static final Logger logger = LoggerFactory.getLogger(PreAuthenticatedUserCache.class);
	private static final Cache userCache = CacheManager.getInstance().getCache("preauthenticatedUserCache");
	public static final int FIXED_RATE = (60 * 8) * 60 * 1000;
	//public static final int FIXED_RATE = 30 * 1000;
	
	@Value("${security.token.filter.token-duration-in-minutes:0}")
	private int tokenDurationInMinutes;

	@Scheduled(fixedRate = FIXED_RATE)
	public void evictExpiredTokens() {
		logger.info("Evicting expired tokens: " + userCache.getSize());
		userCache.evictExpiredElements();
		logger.info("Evicted expired tokens: " + userCache.getSize());
	}

	public void storeUser(String token, LoggedUser user) {
		logger.info("storeUser: " + user.getUsername());
		Element e = new Element(token, user);
		e.setTimeToLive(tokenDurationInMinutes * 60);
		userCache.put(e);
	}

	public boolean contains(String token) {
		logger.info("contains token: " + token);
		return userCache.get(token) != null;
	}

	public boolean removeUser(String token) {
		logger.info("removeUser: " + token);
		return userCache.remove(token);
	}

	public LoggedUser retrieveUser(String token) {
		logger.info("retrieveUser: " + token);
		 Element e = userCache.get(token);
		 LoggedUser user = (LoggedUser)e.getObjectValue();
		return user;
	}

}
