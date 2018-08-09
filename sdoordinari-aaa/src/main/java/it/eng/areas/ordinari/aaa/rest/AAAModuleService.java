/**
 * 
 */
package it.eng.areas.ordinari.aaa.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.eng.areas.ordinari.aaa.model.AuthenticatedUser;
import it.eng.areas.ordinari.aaa.model.LoggedUser;
import it.eng.areas.ordinari.aaa.token.CacheTokenService;

//@RestController
//@RequestMapping("/safe/api")
public class AAAModuleService {

//	@Autowired
//	private CacheTokenService tokenService;
//
//	@RequestMapping(path = "/getCurrentUser", method = RequestMethod.POST, //
//			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, //
//			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public LoggedUser getCurrentUser(@AuthenticatedUser LoggedUser user) {
//		return user;
//	}
//
//	@RequestMapping(path = "/renewToken", method = RequestMethod.POST, //
//			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, //
//			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public LoggedUser renewToken(@AuthenticatedUser LoggedUser user) {
//		String token = user.getToken();
//		tokenService.renew(token);
//		return user;
//	}
//
//	@RequestMapping(path = "/logout", method = RequestMethod.POST, //
//			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, //
//			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public boolean logout(@AuthenticatedUser LoggedUser user) {
//		return tokenService.close(user.getToken());
//	}
}