/**
 * 
 */
package it.eng.areas.ordinari.aaa.token;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Bifulco Luigi
 *
 */
public class TokenResponse {

	@JsonProperty
	private String token;

	public TokenResponse() {
	}

	public TokenResponse(String token) {
		this.token = token;
	}
}
