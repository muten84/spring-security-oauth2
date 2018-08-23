/**
 * 
 */
package it.eng.areas.ems.ordinari.service;

import it.eng.areas.ems.ordinari.entity.WebIdentityDO;
import it.eng.areas.ems.ordinari.entity.WebSessionDO;

/**
 * @author Bifulco Luigi
 *
 */
public interface WebIdentityService {

	/**
	 * 
	 * @param username
	 * @return
	 */
	public WebIdentityDO getUser(String username);

	/**
	 * 
	 * @param username
	 * @return
	 */
	public String getUserSession(String username);

	/**
	 * 
	 * @param userId
	 * @param token
	 * @return
	 */
	public WebSessionDO createSession(String userId, String token);

	/**
	 * @param username
	 * @return
	 */
	public boolean destroySession(String username);
	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public WebIdentityDO needToRenewPassword(String username, long days);
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public WebIdentityDO changePassword(String username, String password);
}
