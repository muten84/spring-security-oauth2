/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author Bifulco Luigi
 *
 */

@Configuration
@EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true,
// mode = AdviceMode.PROXY)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${security.user.name}")
	private String basicAuthUser;

	@Value("${security.user.password}")
	private String basicAuthPassword;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().antMatchers("/api/rest/**").authenticated().and().httpBasic()
				.authenticationEntryPoint(new BasicAuthenticationEntryPoint() {

					@Override
					public void commence(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException authEx) throws IOException, ServletException {
						response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						PrintWriter writer = response.getWriter();
						writer.println("HTTP Status 401 - " + authEx.getMessage());

					}

					@Override
					public void afterPropertiesSet() throws Exception {
						setRealmName("Emsmobile");
						super.afterPropertiesSet();
					}
				});

		http.addFilterAfter(new Customfilter(), BasicAuthenticationFilter.class);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(basicAuthUser).password(basicAuthPassword).roles("ADMIN");
	}

	static class Customfilter extends GenericFilterBean {

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
		 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
		 */
		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
				throws IOException, ServletException {
			chain.doFilter(request, response);

		}

	}

}
