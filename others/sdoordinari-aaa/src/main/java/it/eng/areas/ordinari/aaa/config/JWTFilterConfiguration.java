/**
 * 
 */
package it.eng.areas.ordinari.aaa.config;

/**
 * @author Bifulco Luigi
 *
 */
// @Configuration
// @Order(1)
public class JWTFilterConfiguration {

//	@Value("${security.token.filter.secret}")
//	String secret;
//
//	@Value("${security.token.filter.role-prefix:ROLE_}")
//	String rolePrefix;
//
//	@Value("${security.token.filter.token-duration-in-minutes:0}")
//	int tokenDurationInMinutes;
//
//	@Value("${security.token.filter.token-duration-in-hours:8}")
//	int tokenDurationInHours;

	// @Bean
	// public TokenService tokenService() throws JOSEException {
	// return new JwtTokenService(claimsSetTransformer(), secret);
	// }
	//
	// @Bean
	// public AuthenticationJwtClaimsSetTransformer claimsSetTransformer() {
	// Optional<String> prefix = Optional.empty();
	//
	// if (!"".equals(rolePrefix)) {
	// prefix = Optional.of(rolePrefix);
	// }
	//
	// long tokenDuration = 0;
	//
	// if (tokenDurationInHours != 0) {
	// tokenDuration = TimeUnit.HOURS.toMillis(tokenDurationInHours);
	// }
	//
	// if (tokenDurationInMinutes != 0) {
	// tokenDuration = TimeUnit.MINUTES.toMillis(tokenDurationInMinutes);
	// }
	//
	// return new
	// UsernamePasswordAuthenticationTokenJwtClaimsSetTransformer(tokenDuration,
	// prefix);
	// }

}
