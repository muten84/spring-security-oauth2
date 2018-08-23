/**
 * 
 */
package it.eng.areas.ordinari.aaa.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.eng.areas.ordinari.aaa.model.AAARequest;

/**
 * @author Bifulco Luigi
 *
 */

@RestController
@RequestMapping("/auth")
public class LoginController {

	@RequestMapping(path = "/login", //
			method = RequestMethod.POST, //
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, //
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String authenticate(@RequestBody AAARequest request) {
		return "OK";
	}

}
