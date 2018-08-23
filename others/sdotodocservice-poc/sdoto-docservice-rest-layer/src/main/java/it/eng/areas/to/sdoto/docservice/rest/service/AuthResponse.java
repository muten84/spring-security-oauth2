/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest.service;

import java.io.Serializable;
import java.util.Map;

import io.swagger.annotations.ApiModel;

/**
 * @author Bifulco Luigi
 *
 */
@ApiModel(value = "AuthResponse", description = "Response authentication info")
public class AuthResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7390443880426820045L;

	private String username;

	private boolean authenticated;

	private String reason;

	private String token;

	private Map<String, String> details;

	public AuthResponse() {

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
	 * @return the authenticated
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}

	/**
	 * @param authenticated
	 *            the authenticated to set
	 */
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
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

}
