/**
 * 
 */
package it.eng.areas.ordinari.aaa.config;

/**
 * @author Bifulco Luigi
 *
 */
// @Import(AuthenticationDBConfig.class)
// @Configuration
// @EnableWebSecurity
// @EnableScheduling
// @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
// @ComponentScan("it.eng.areas.ordinari.aaa")
public class OldSecurityConfig {
	// extends WebSecurityConfigurerAdapter {

	// @Value("${secure.login:/auth/login}")
	// private String secureLogin;
	//
	// @Value("${secure.domain:/safe/*}")
	// private String secureDomain;
	//
	// @Autowired
	// private WebAuthenticationEntryPoint restAuthenticationEntryPoint;
	//
	// @Autowired
	// private AuthenticationHandler authenticationSuccessHandler;
	//
	// // @Override
	// // protected void configure(AuthenticationManagerBuilder auth) throws
	// // Exception {
	// //
	// //
	// auth.inMemoryAuthentication().withUser("temporary").password("temporary").roles("ADMIN").and().withUser("user")
	// // .password("userPass").roles("USER");
	// // }
	//
	// @Bean
	// public AuthenticationProvider tokenAuthenticationProvider() {
	// return new TokenAuthenticationProvider(tokenService());
	// }
	//
	// // @Bean
	// // public AuthenticationProvider
	// // domainUsernamePasswordAuthenticationProvider() {
	// // return new
	// DomainUsernamePasswordAuthenticationProvider(tokenService(),
	// // someExternalServiceAuthenticator());
	// // }
	//
	// @Bean
	// public ExternalAuthenticator someExternalServiceAuthenticator() {
	// return new ExternalAuthenticatorImpl();
	// }
	//
	// @Bean
	// public AuthenticationProvider
	// backendAdminUsernamePasswordAuthenticationProvider() {
	// return new SystemUserAuthenticationProvider();
	// }
	//
	// @Bean
	// public AuthenticationProvider
	// domainUsernamePasswordAuthenticationProvider() {
	// return new DomainAuthenticationProvider(tokenService());
	// }
	//
	// @Override
	// protected void configure(AuthenticationManagerBuilder auth) throws
	// Exception {
	// auth.authenticationProvider(domainUsernamePasswordAuthenticationProvider())
	// .authenticationProvider(backendAdminUsernamePasswordAuthenticationProvider())
	// .authenticationProvider(tokenAuthenticationProvider());
	// }
	//
	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http.csrf().disable()//
	// .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
	// .and()//
	// .exceptionHandling()//
	// .authenticationEntryPoint(restAuthenticationEntryPoint)//
	// .and()//
	// .authorizeRequests()//
	// .antMatchers(secureDomain)//
	// .authenticated()//
	// .and()//
	// .anonymous().disable()//
	// .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
	// // .formLogin().successHandler(authenticationSuccessHandler)
	// // .failureHandler(new
	// // SimpleUrlAuthenticationFailureHandler()).and().logout();
	// AuthenticationFilter filter = new
	// AuthenticationFilter(authenticationManager());
	// filter.setSecureLogin(secureLogin);
	// http.addFilterBefore(filter, BasicAuthenticationFilter.class);
	// }
	//
	// @Bean
	// public CacheTokenService tokenService() {
	// return new CacheTokenService();
	// }
	//
	// // private String[] actuatorEndpoints() {
	// // return new String[] { ApiController.AUTOCONFIG_ENDPOINT,
	// // ApiController.BEANS_ENDPOINT,
	// // ApiController.CONFIGPROPS_ENDPOINT, ApiController.ENV_ENDPOINT,
	// // ApiController.MAPPINGS_ENDPOINT,
	// // ApiController.METRICS_ENDPOINT, ApiController.SHUTDOWN_ENDPOINT };
	// // }
	//
	// @Bean
	// public AuthenticationHandler mySuccessHandler() {
	// return new AuthenticationHandler();
	// }
	//
	// @Bean
	// public SimpleUrlAuthenticationFailureHandler myFailureHandler() {
	// return new SimpleUrlAuthenticationFailureHandler();
	// }
	//
	// @Bean
	// public AuthenticationEntryPoint unauthorizedEntryPoint() {
	// return (request, response, authException) ->
	// response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	// }
}
