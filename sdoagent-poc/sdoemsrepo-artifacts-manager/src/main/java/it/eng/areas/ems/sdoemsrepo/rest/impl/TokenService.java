/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.rest.impl;

import java.util.Date;

import javax.servlet.ServletException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Bifulco Luigi
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rest/auth")
public class TokenService {

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login() throws ServletException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String jwtToken = "";

		// if (login.getEmail() == null || login.getPassword() == null) {
		// throw new ServletException("Please fill in username and password");
		// }
		//
		// String email = login.getEmail();
		// String password = login.getPassword();
		//
		// User user = userService.findByEmail(email);
		//
		// if (user == null) {
		// throw new ServletException("User email not found.");
		// }
		//
		// String pwd = user.getPassword();
		//
		// if (!password.equals(pwd)) {
		// throw new ServletException("Invalid login. Please check your name and
		// password.");
		// }
		if (!auth.isAuthenticated()) {
			throw new ServletException("Basic auth not found");
		} else {

			jwtToken = Jwts.builder().setSubject(auth.getName()).claim("roles", "user").setIssuedAt(new Date())
					.signWith(SignatureAlgorithm.HS256, "secretkey").compact();
		}

		return jwtToken;
	}

}
