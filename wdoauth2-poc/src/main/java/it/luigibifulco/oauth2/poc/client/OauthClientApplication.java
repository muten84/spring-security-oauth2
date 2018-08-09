/**
 * 
 */
package it.luigibifulco.oauth2.poc.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;

/**
 * @author Bifulco Luigi
 *
 */

@SpringBootApplication
public class OauthClientApplication extends SpringBootServletInitializer {

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	public static void main(String[] args) {
		SpringApplication.run(OauthClientApplication.class, args);
	}
}
