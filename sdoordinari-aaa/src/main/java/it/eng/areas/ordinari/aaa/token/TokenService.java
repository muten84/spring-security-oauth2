/**
 * 
 */
package it.eng.areas.ordinari.aaa.token;

import java.util.Optional;

import org.springframework.security.core.Authentication;

/**
 * @author Bifulco Luigi
 *
 */
public interface TokenService {
	
	String generateToken(Authentication authentication);

	  Optional<Authentication> verifyToken(Optional<String> token);


}
