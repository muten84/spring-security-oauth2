/**
 * 
 */
package it.eng.areas.ordinari.aaa.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ordinari.aaa.annotations.CurrentUser;
import it.eng.areas.ordinari.aaa.caching.PreAuthenticatedUserCache;
import it.eng.areas.ordinari.aaa.model.LoggedUser;
import it.eng.care.platform.authentication.api.model.Session;
import it.eng.care.platform.authentication.api.service.AuthenticationService;

/**
 * @author Bifulco Luigi
 *
 */
@RestController
// @PreAuthorize(value = "hasRole('ADMIN')")
// @Secured("hasRole('ADMIN')")
@RequestMapping("/api/secure")
@Api(value = "AAAController")
public class AAAController {

	@Autowired
	private AuthenticationService authService;

	@Autowired
	private PreAuthenticatedUserCache cache;

	@RequestMapping(value = "/currentUser", method = RequestMethod.POST)
	// @Secured(value = { "ROLE_USER" })
	// @PreAuthorize ("hasRole('ROLE_USER')")
	@ApiOperation(value = "getCurrentUser")
	@ApiResponses(value = { @ApiResponse(code = 200, message = " ", response = LoggedUser.class) })
	public LoggedUser getCurrentUser(@CurrentUser LoggedUser currentUser) {
		return currentUser;
	}

	@RequestMapping(value = "/adminUser", method = RequestMethod.POST)
	// @Secured(value = { "ROLE_ADMIN" })
	// @PreAuthorize ("hasRole('ROLE_ADMIN')")
	public LoggedUser getAdminUser(@CurrentUser LoggedUser currentUser) {
		return currentUser;
	}

	@RequestMapping(value = "/checkAuth", method = RequestMethod.POST)
	// @PreAuthorize ("hasRole('ROLE_ADMIN')")
	public LoggedUser checkAuth(@CurrentUser LoggedUser currentUser) {
		return currentUser;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	// @PreAuthorize ("hasRole('ROLE_USER')")
	public boolean logout(@CurrentUser LoggedUser currentUser) {
		Session s = authService.getSessionLastByUsername(currentUser.getUsername());
		Boolean done = authService.closeSession(s.getId());
		if (done) {
			cache.removeUser(currentUser.getUsername());
		}
		return true;
	}
}