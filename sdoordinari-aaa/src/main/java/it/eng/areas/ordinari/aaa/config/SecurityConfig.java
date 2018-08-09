/**
 * 
 */
package it.eng.areas.ordinari.aaa.config;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import it.eng.areas.ordinari.aaa.business.AAAModuleTransactionalService;
import it.eng.areas.ordinari.aaa.business.impl.TokenAuthUserDetailsService;
import it.eng.areas.ordinari.aaa.caching.PreAuthenticatedUserCache;
import it.eng.areas.ordinari.aaa.model.LoggedUser;
import it.eng.areas.ordinari.aaa.provider.CustomAuthenticationProvider;
import it.eng.areas.ordinari.aaa.token.TokenService;
import it.eng.areas.ordinari.aaa.web.filter.PreAuthFilter;
import it.eng.areas.ordinari.aaa.web.filter.UserPassFilter;
import it.eng.care.platform.authentication.impl.config.AuthenticationDBConfig;

/**
 * @author Bifulco Luigi
 *
 */

@Import({ AuthenticationDBConfig.class })
// @Order(99)
@Configuration
@EnableWebSecurity
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, mode = AdviceMode.PROXY)
// @PropertySource(value = {
// "classpath:sdoordinari-core-web/cfg-oracle-jpa.properties" })
// @PropertySource(value = {
// "classpath:sdoordinari-core-web/cfg-security.properties" })
@ComponentScan("it.eng.areas.ordinari.aaa")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityConfig.class);

	@Value("${backend.admin.role}")
	private String backendAdminRole;

	@Value("${secure.login:/auth/login}")
	private String secureLogin;

	@Value("${secure.domain:/safe/*}")
	private String secureDomain;
	
	@Value("${security.password-expires-in-days:30}")
	private int passwordExpiresInDays;

	// @Autowired
	// private TokenAuthenticationFilter tokenAuthenticationFilter;

	// @Autowired
	// private PreAuthFilter preauthfilter;
	//
	// @Autowired
	// private UserPassFilter userPassFilter;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private CustomAuthenticationProvider myAuthProvider;

	@Autowired
	private PreAuthenticatedAuthenticationProvider tokenAuthenticationProvider;

	@Autowired
	private AAAModuleTransactionalService aaaDbService;

	@Autowired
	private PreAuthenticatedUserCache cache;

	@PostConstruct
	public void securityInitialized() {

		TokenAuthUserDetailsService service = new TokenAuthUserDetailsService(passwordExpiresInDays, aaaDbService, cache);
		tokenAuthenticationProvider.setPreAuthenticatedUserDetailsService(service);

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	
		http.addFilterBefore(corsFilter(), ChannelProcessingFilter.class);
		http.headers().defaultsDisabled().cacheControl();	
		http.addFilterBefore(PreAuthFilter(), BasicAuthenticationFilter.class);
		http.addFilterAfter(UsernamePasswordAuthenticationFilter(), PreAuthFilter.class);

		http.authorizeRequests()
				.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
						"/swagger-ui.html", "/webjars/**")
				.permitAll()
				// Authenticate endpoint can be access by anyone
				.antMatchers("/api/login").anonymous().antMatchers(secureDomain).authenticated();
	}
	
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

	// private String[] actuatorEndpoints() {
	// return new String[] { ApiController.AUTOCONFIG_ENDPOINT,
	// ApiController.BEANS_ENDPOINT,
	// ApiController.CONFIGPROPS_ENDPOINT, ApiController.ENV_ENDPOINT,
	// ApiController.MAPPINGS_ENDPOINT,
	// ApiController.METRICS_ENDPOINT, ApiController.SHUTDOWN_ENDPOINT };
	// }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.authenticationProvider(tokenAuthenticationProvider);
		auth.authenticationProvider(myAuthProvider);
		// auth.authenticationProvider(domainUsernamePasswordAuthenticationProvider())
		// .authenticationProvider(backendAdminUsernamePasswordAuthenticationProvider())
		// .authenticationProvider(tokenAuthenticationProvider());
	}

	// @Bean
	// @ConditionalOnMissingBean(UserPassFilter.class)
	public UserPassFilter UsernamePasswordAuthenticationFilter() throws Exception {
		UserPassFilter filter = new UserPassFilter();
		filter.setAuthenticationManager(authenticationManager());
		filter.setAllowSessionCreation(false);
		filter.setContinueChainBeforeSuccessfulAuthentication(false);
		filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {

			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				// TODO Auto-generated method stub
				
				logger.info("user is authenticated generating token: " + authentication.getPrincipal());
				LoggedUser user = (LoggedUser) authentication.getPrincipal();
				if (!cache.contains(user.getUsername())) {
					cache.storeUser(user.getUsername(), user);
				}
				String token = tokenService.generateToken(authentication);
				response.addHeader("X-Auth-Token", token);
				response.setHeader("Content-Type", "application/json");
				response.getWriter().println("{ \"token\" :" + "\"" + token + "\"" + "}");
				// cache.storeUser(authentication.getPrincipal().toString(),
				// authentication);

			}
		});
		return filter;
	}

	// @Bean
	// @ConditionalOnMissingBean(PreAuthFilter.class)
	public PreAuthFilter PreAuthFilter() throws Exception {
		PreAuthFilter filter = new PreAuthFilter(tokenService);
		filter.setAuthenticationManager(authenticationManager());
		filter.setContinueFilterChainOnUnsuccessfulAuthentication(true);
		filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {

			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				logger.info("onAuthenticationFailure: " + exception.getMessage());
				// if (cache.contains(user.getUsername())) {
				// cache.storeUser(user.getUsername(), user);
				// }
			}
		});
		filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {

			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				logger.debug("user is alive: " + authentication.getPrincipal());
				LoggedUser user = (LoggedUser) authentication.getPrincipal();
				if (!cache.contains(user.getUsername())) {
					cache.storeUser(user.getUsername(), user);
				}

			}
		});
		// filter.setAuthenticationDetailsSource(new
		// AuthenticationDetailsSource<HttpServletRequest, T>() {
		//
		// @Override
		// public T buildDetails(HttpServletRequest context) {
		// // TODO Auto-generated method stub
		// return null;
		// }
		// });
		return filter;
	}

	@Bean
	public PreAuthenticatedAuthenticationProvider preAuthProvider() {
		PreAuthenticatedAuthenticationProvider p = new PreAuthenticatedAuthenticationProvider();
		p.setThrowExceptionWhenTokenRejected(false);
		/*
		 * importante lo user detail service è stato impostato sul postconstruct
		 */

		p.setPreAuthenticatedUserDetailsService(
				new AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>() {

					@Override
					public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token)
							throws UsernameNotFoundException {
						return new LoggedUser(token.getPrincipal().toString());
					}
				});
		// TokenAuthUserDetailsService tokenUserDetailService = new
		// TokenAuthUserDetailsService(1, authenticationService,
		// userService);
		// p.setPreAuthenticatedUserDetailsService(tokenUserDetailService);
		// p.setUserDetailsChecker(userDetailsChecker);
		return p;
	}

	// @Bean
	// public CacheTokenService tokenService() {
	// return new CacheTokenService();
	// }

	// @Bean
	// public ExternalServiceAuthenticator someExternalServiceAuthenticator() {
	// return new SomeExternalServiceAuthenticator();
	// }
	//
	// @Bean
	// public AuthenticationProvider
	// domainUsernamePasswordAuthenticationProvider() {
	// return new DomainUsernamePasswordAuthenticationProvider(tokenService(),
	// someExternalServiceAuthenticator());
	// }
	//
	// @Bean
	// public AuthenticationProvider
	// backendAdminUsernamePasswordAuthenticationProvider() {
	// return new BackendAdminUsernamePasswordAuthenticationProvider();
	// }
	//
	// @Bean
	// public AuthenticationProvider tokenAuthenticationProvider() {
	// return new TokenAuthenticationProvider(tokenService());
	// }

	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

}
