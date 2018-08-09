/**
 * 
 */
package it.eng.areas.ordinari.aaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import it.eng.areas.ordinari.aaa.config.WebMvcConfig;

/**
 * @author Bifulco Luigi
 *
 */
@Configuration
@Import({ AAAConfiguration.class, WebMvcConfig.class })
@SpringBootApplication
@EnableWebMvc
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
// @PropertySource(value = {
// "classpath:sdoordinari-core-web/cfg-oracle-jpa.properties" })
// @PropertySource(value = {
// "classpath:sdoordinari-core-web/cfg-security.properties" })
@ImportResource("classpath*:/META-INF/subordinaryDAO.xml")
public class AAAModuleApplication {

	// @Value("${security.token.filter.secret}")
	// String secret;
	//
	// @Value("${security.token.filter.role-prefix:ROLE_}")
	// String rolePrefix;
	//
	// @Value("${security.token.filter.token-duration-in-minutes:0}")
	// int tokenDurationInMinutes;
	//
	// @Value("${security.token.filter.token-duration-in-hours:8}")
	// int tokenDurationInHours;

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

	public static void main(String[] args) {
		// System.out.println(DigestUtils.md5DigestAsHex("ADMIN".getBytes()));
		SpringApplication.run(AAAModuleApplication.class, args);
	}
}