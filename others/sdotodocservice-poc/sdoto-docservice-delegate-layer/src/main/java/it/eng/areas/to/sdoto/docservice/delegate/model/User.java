/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.delegate.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bifulco Luigi
 *
 */
public class User implements Serializable {

	private String username;

	private String token;

	private Map<String, String> details;

	public User() {

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

	/**
	 * @return the details
	 */
	public Map<String, String> getDetails() {
		return details;
	}

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDetails(Map<String, String> details) {
		this.details = details;
	}

	public void addDetail(String key, String value) {
		if (getDetails() == null) {
			setDetails(new HashMap<String, String>());
		}
		getDetails().put(key, value);

	}

}
