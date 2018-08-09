/**
 * 
 */
package it.luigibifulco.oauth2.poc.server.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Bifulco Luigi
 *
 */

@RestController	
public class UserController {
	
	 @RequestMapping("/user/me")
	    public Principal user(Principal principal) {
	        System.out.println(principal);
	        return principal;
	    }

}
