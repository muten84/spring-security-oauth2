/**
 * 
 */
package it.eng.areas.ordinari.aaa;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.nimbusds.jose.JOSEException;

import it.eng.areas.ordinari.aaa.config.SecurityConfig;
import it.eng.areas.ordinari.aaa.config.WebMvcConfig;
import it.eng.areas.ordinari.aaa.token.TokenService;
import it.eng.areas.ordinari.aaa.token.jwt.AuthenticationJwtClaimsSetTransformer;
import it.eng.areas.ordinari.aaa.token.jwt.JwtTokenService;
import it.eng.areas.ordinari.aaa.token.jwt.UsernamePasswordAuthenticationTokenJwtClaimsSetTransformer;

/**
 * @author Bifulco Luigi
 *
 */
@Configuration
@Import({ SecurityConfig.class, WebMvcConfig.class })
@EnableWebMvc
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
public class AAAConfiguration {

	@Value("${security.token.filter.secret}")
	String secret;

	@Value("${security.token.filter.role-prefix:ROLE_}")
	String rolePrefix;

	@Value("${security.token.filter.token-duration-in-minutes:0}")
	int tokenDurationInMinutes;

	@Value("${security.token.filter.token-duration-in-hours:8}")
	int tokenDurationInHours;
	

	// @Bean
	// public TokenAuthUserDetailsService TokenAuthUserDetailsService() {
	// return new TokenAuthUserDetailsService();
	// }

	@Bean
	public TokenService tokenService() throws JOSEException {
		return new JwtTokenService(claimsSetTransformer(), secret);
	}

	@Bean
	public AuthenticationJwtClaimsSetTransformer claimsSetTransformer() {
		Optional<String> prefix = Optional.empty();

		if (!"".equals(rolePrefix)) {
			prefix = Optional.of(rolePrefix);
		}

		long tokenDuration = 0;

		if (tokenDurationInHours != 0) {
			tokenDuration = TimeUnit.HOURS.toMillis(tokenDurationInHours);
		}

		if (tokenDurationInMinutes != 0) {
			tokenDuration = TimeUnit.MINUTES.toMillis(tokenDurationInMinutes);
		}

		return new UsernamePasswordAuthenticationTokenJwtClaimsSetTransformer(tokenDuration, prefix);
	}
}
