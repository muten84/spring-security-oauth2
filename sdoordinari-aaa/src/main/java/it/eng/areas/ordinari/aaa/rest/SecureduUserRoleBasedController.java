/**
 * 
 */
package it.eng.areas.ordinari.aaa.rest;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.eng.areas.ordinari.aaa.annotations.CurrentUser;
import it.eng.areas.ordinari.aaa.model.AAARequest;
import it.eng.areas.ordinari.aaa.model.AAAResponse;
import it.eng.areas.ordinari.aaa.model.LoggedUser;

/**
 * @author Bifulco Luigi
 *
 */
@RestController
@RequestMapping("/api/secure")
@PreAuthorize(value = "hasRole('USER')")
public class SecureduUserRoleBasedController {

	@RequestMapping(value = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	// @Secured(value = { "ROLE_USER" })
	// @PreAuthorize ("hasRole('ROLE_USER')")
	public AAAResponse getCurrentUser(@RequestBody AAARequest input, @CurrentUser LoggedUser currentUser) {
		AAAResponse response = new AAAResponse();
		if (currentUser != null) {
			response.setResult(currentUser.getUsername());
		} else {
			response.setResult("OK NO AUTH");
		}
		return response;
	}

}
