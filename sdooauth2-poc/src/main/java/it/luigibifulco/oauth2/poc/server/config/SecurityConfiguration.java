/**
 * 
 */
package it.luigibifulco.oauth2.poc.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Bifulco Luigi
 *
 */

@Configuration
@Order(1)
// @EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	// @Autowired
	// DataSource dataSource;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception { // @formatter:off
		http.addFilterBefore(corsFilter(), ChannelProcessingFilter.class);
		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.csrf().disable().requestMatchers()

				.antMatchers("/h2-console/**", "/index.html", "/login", "/oauth/authorize", "/v2/api-docs",
						"/swagger-resources/configuration/ui", "/swagger-resources/**", "/configuration/security",
						"/swagger-ui.html", "/webjars/**")
				.and().authorizeRequests().anyRequest().authenticated().and().formLogin().permitAll();

		/*
		 * .antMatchers("/oauth/token/revokeById/**").permitAll()
		 * .antMatchers("/tokens/**").permitAll();
		 */
	} // @formatter:on

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

//	@Autowired
//	public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
//		// @formatter:off
//		auth.inMemoryAuthentication().withUser("john").password(passwordEncoder.encode("123")).roles("USER").and()
//				.withUser("tom").password(passwordEncoder.encode("111")).roles("ADMIN").and().withUser("user1")
//				.password(passwordEncoder.encode("pass")).roles("USER").and().withUser("admin")
//				.password(passwordEncoder.encode("nimda")).roles("ADMIN");
//	}// @formatter:on

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception { // @formatter:off
		// JDBC AUTH
		// auth.jdbcAuthentication().dataSource(dataSource)
		// .usersByUsernameQuery(
		// "select username,password, enabled from users where username=?")
		// .authoritiesByUsernameQuery(
		// "select username, role from user_roles where username=?");

		auth.inMemoryAuthentication().withUser("john").password(passwordEncoder.encode("123")).roles("USER").and()
				.withUser("tom").password(passwordEncoder.encode("111")).roles("ADMIN").and().withUser("user1")
				.password(passwordEncoder.encode("pass")).roles("USER").and().withUser("admin")
				.password(passwordEncoder.encode("nimda")).roles("ADMIN");

	} // @formatter:on

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
