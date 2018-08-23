/**
 * 
 */
package it.eng.areas.ordinari.aaa.token.jwt;

import org.springframework.security.core.Authentication;

import com.nimbusds.jwt.JWTClaimsSet;

/**
 * @author Bifulco Luigi
 *
 */
public interface AuthenticationJwtClaimsSetTransformer {

	JWTClaimsSet getClaimsSet(Authentication auth);

	Authentication getAuthentication(JWTClaimsSet claimSet);

}
