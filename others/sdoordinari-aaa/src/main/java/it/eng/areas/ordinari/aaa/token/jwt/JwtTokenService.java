/**
 * 
 */
package it.eng.areas.ordinari.aaa.token.jwt;

import java.text.ParseException;
import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import it.eng.areas.ordinari.aaa.token.TokenService;

/**
 * @author Bifulco Luigi
 *
 */

public class JwtTokenService implements TokenService {

	private AuthenticationJwtClaimsSetTransformer transformer;
	private JWSSigner signer;
	private JWSVerifier verifier;

	public JwtTokenService() {

	}

	public JwtTokenService(AuthenticationJwtClaimsSetTransformer transformer, String secret) throws JOSEException {
		this.signer = new MACSigner(secret);
		this.verifier = new MACVerifier(secret);
		this.transformer = transformer;
	}

	public JwtTokenService(AuthenticationJwtClaimsSetTransformer transformer, JWSSigner signer, JWSVerifier verifier)
			throws JOSEException {
		this.signer = signer;
		this.verifier = verifier;
		this.transformer = transformer;
	}

	@Override
	public String generateToken(Authentication authentication) {
		JWTClaimsSet claimsSet = transformer.getClaimsSet(authentication);
		SignedJWT signedJwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
		try {
			signedJwt.sign(signer);
		} catch (JOSEException e) {
			throw new RuntimeException("Error while signing token.", e);
		}
		return signedJwt.serialize();
	}

	@Override
	public Optional<Authentication> verifyToken(Optional<String> token) {
		if (!token.isPresent()) {
			return Optional.empty();
		}

		SignedJWT signedJwt;
		JWTClaimsSet claimSet;
		try {
			signedJwt = SignedJWT.parse(token.get());
			claimSet = signedJwt.getJWTClaimsSet();

			if (!signedJwt.verify(verifier)) {
				throw new BadCredentialsException("Invalid token");
			}
		} catch (ParseException | JOSEException e) {
			throw new IllegalArgumentException("Error while parsing and verifying token.", e);
		}

		if (claimSet.getExpirationTime().getTime() < System.currentTimeMillis()) {
			throw new BadCredentialsException("Token is expired");
		}

		return Optional.of(transformer.getAuthentication(claimSet));
	}

}