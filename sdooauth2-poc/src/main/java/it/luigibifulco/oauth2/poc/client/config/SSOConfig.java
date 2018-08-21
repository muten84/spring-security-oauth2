package it.luigibifulco.oauth2.poc.client.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@EnableOAuth2Sso
@Configuration
@Order(1)
public class SSOConfig {

}
