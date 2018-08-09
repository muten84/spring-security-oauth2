/**
 * 
 */
package it.eng.areas.ordinari.aaa.business;

import java.util.List;

import it.eng.care.platform.authentication.api.model.Role;
import it.eng.care.platform.authentication.api.model.Session;
import it.eng.care.platform.authentication.api.model.User;

public interface AAAModuleTransactionalService {

	/**
	 * @param username
	 * @param pwd
	 * @return
	 */
	Session authenticate(String username, String pwd);

	/**
	 * @param sessionId
	 * @return
	 */
	boolean closeSession(String sessionId);

	/**
	 * @param username
	 * @return
	 */
	Session getCurrentSession(String username);

	/**
	 * 
	 * @param username
	 * @return
	 */
	Session getCurrentValidSession(String username);

	/**
	 * @param username
	 * @return
	 */
	User getUser(String username);

	/**
	 * @param username
	 * @return
	 */
	List<Role> getUserRoles(String username);

}