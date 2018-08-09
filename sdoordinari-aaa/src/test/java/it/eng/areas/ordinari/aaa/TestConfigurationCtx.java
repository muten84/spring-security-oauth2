/**
 * 
 */
/**
 * 
 */
package it.eng.areas.ordinari.aaa;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import it.eng.areas.ordinari.aaa.config.WebMvcConfig;

/**
 * @author Bifulco Luigi
 *
 */
@Configuration
// @EnableAutoConfiguration
@Import({ AAAConfiguration.class, WebMvcConfig.class })
@EnableWebMvc
@ComponentScan
@EnableConfigurationProperties
@PropertySource(value = { "classpath:sdoordinari-core-web/cfg-oracle-jpa.properties" })
@PropertySource(value = { "classpath:sdoordinari-core-web/cfg-security.properties" })
@ImportResource("classpath*:/META-INF/subordinaryDAO.xml")
public class TestConfigurationCtx {

	@Bean
	public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
		// conf.

		conf.setIgnoreUnresolvablePlaceholders(true);
		return conf;
	}

	@Bean
	public JsonParser JsonParser() {
		return new JacksonJsonParser();
	}
}
