package it.luigibifulco.oauth2.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.context.request.RequestContextListener;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableResourceServer
@EnableSwagger2
@SpringBootApplication
//https://github.com/Baeldung/spring-security-oauth/tree/master/oauth-authorization-server
public class Sdooauth2PocApplication extends SpringBootServletInitializer {

	private static final String SECURITY_SCHEMA_OAUTH2 = "oauth2";

	public static void main(String[] args) {
		SpringApplication.run(Sdooauth2PocApplication.class, args);
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

}
