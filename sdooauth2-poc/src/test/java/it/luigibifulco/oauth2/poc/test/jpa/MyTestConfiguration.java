package it.luigibifulco.oauth2.poc.test.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * questa classe mi serve solamente per configurare i bean che devono partire
 * sui test transazionali e dove non mi serbe ad esempio l'autenticazione oauth
 * e altre cose dei ret controller che devono essere fatte in un altro layer e
 * permettere solo in quel layer a spring di avviare eventualmente dei test di
 * integrazione con docker
 * 
 * @author luigib
 *
 */
@Configuration
@EntityScan(basePackages = "it.luigibifulco.oauth2.poc.jpa.domain")
@EnableJpaRepositories(basePackages = "it.luigibifulco.oauth2.poc.jpa.service")
public class MyTestConfiguration {

}
