/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest.service;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Bifulco Luigi
 *
 */
@ApiModel(value = "Credentials", description = "Request to authenticate user")
public class Credentials implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7000238374922813706L;
	
	@ApiModelProperty(dataType = "String", value = "The username", required = true, name = "username")
	private String username;
	@ApiModelProperty(dataType = "String", value = "password", required = true, name = "password")
	private String password;

	public Credentials() {

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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
