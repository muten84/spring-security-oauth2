/**
 * 
 */
package it.eng.areas.ordinari.aaa.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.eng.areas.ordinari.aaa.annotations.CurrentUser;
import it.eng.areas.ordinari.aaa.model.AAAResponse;
import it.eng.areas.ordinari.aaa.model.LoggedUser;

/**
 * @author Bifulco Luigi
 *
 */
@RestController
@RequestMapping("/api/unsecure")
public class UnsecureController {

	@RequestMapping(value = "/currentUser", method = RequestMethod.POST)
	// @Secured(value = { "ROLE_USER" })
	// @PreAuthorize ("hasRole('ROLE_USER')")
	public AAAResponse getCurrentUser(@CurrentUser LoggedUser currentUser) {
		AAAResponse response = new AAAResponse();
		if (currentUser != null) {
			response.setResult(currentUser.getUsername());
		} else {
			response.setResult("OK NO AUTH");
		}
		return response;

	}

}
