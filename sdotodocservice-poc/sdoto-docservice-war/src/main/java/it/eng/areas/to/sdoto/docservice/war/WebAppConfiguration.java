/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.war;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import it.eng.areas.to.sdoto.docservice.delegate.configuration.DocServiceDelegateConfiguration;
import it.eng.areas.to.sdoto.docservice.delegate.configuration.MailSenderConfiguration;
import it.eng.areas.to.sdoto.docservice.rest.configuration.RestLayerConfiguration;
import it.eng.areas.to.sdoto.docservice.swagger.JerseySwaggerResourcesProvider;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Bifulco Luigi
 *
 */
@Configuration
@PropertySources(//
		value = { //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "${confPath}/cfg-jpa.properties"), //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "${confPath}/cfg-docservice.properties"), //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "${confPath}/cfg-common.properties"), //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "${confPath}/cfg-mail.properties") })
@Import(ApplicationConfiguration.class)
public class WebAppConfiguration {

	@Bean
	public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
		// conf.
		conf.setIgnoreUnresolvablePlaceholders(true);
		return conf;
	}
	// @Bean
	// public RestSwaggerResourcesProvider swaggerJsonProvider() {
	// return new RestSwaggerResourcesProvider();
	// }

}
