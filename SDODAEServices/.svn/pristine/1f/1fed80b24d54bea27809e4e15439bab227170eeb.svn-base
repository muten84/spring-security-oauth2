/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.war;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Import({ ApplicationConfiguration.class })
@EnableScheduling
@ComponentScan(basePackages = { "it.eng.areas.ems.sdodaeservices.rest.service.app" })
@PropertySources(//
		value = { //
				@PropertySource("${confPath}/cfg-jpa.properties"), //
				@PropertySource("${confPath}/cfg-docservice.properties"), //
				@PropertySource("${confPath}/cfg-common.properties"), //
				@PropertySource("${confPath}/cfg-dto.properties"), //
				@PropertySource("${confPath}/cfg-mail.properties"), //
				@PropertySource("${confPath}/cfg-job.properties"), //
				@PropertySource("${confPath}/cfg-pushwoosh.properties"), //
				@PropertySource("${confPath}/cfg-user.properties"), //
				@PropertySource("${confPath}/cfg-application.properties"), //
				// configurazioni opzionali al di fuori dell'app
				@PropertySource(value = "${confPathAbsolute}/cfg-jpa.properties", ignoreResourceNotFound = true), //
				@PropertySource(value = "${confPathAbsolute}/cfg-docservice.properties", ignoreResourceNotFound = true), //
				@PropertySource(value = "${confPathAbsolute}/cfg-common.properties", ignoreResourceNotFound = true), //
				@PropertySource(value = "${confPathAbsolute}/cfg-dto.properties", ignoreResourceNotFound = true), //
				@PropertySource(value = "${confPathAbsolute}/cfg-mail.properties", ignoreResourceNotFound = true), //
				@PropertySource(value = "${confPathAbsolute}/cfg-user.properties", ignoreResourceNotFound = true), //
				@PropertySource(value = "${confPathAbsolute}/cfg-pushwoosh.properties", ignoreResourceNotFound = true), //
				@PropertySource(value = "${confPathAbsolute}/cfg-job.properties", ignoreResourceNotFound = true), //
				@PropertySource(value = "${confPathAbsolute}/cfg-application.properties", ignoreResourceNotFound = true), })

public class WebAppConfiguration {

	@Bean
	public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
		// conf.
		conf.setIgnoreUnresolvablePlaceholders(true);
		return conf;
	}
}
