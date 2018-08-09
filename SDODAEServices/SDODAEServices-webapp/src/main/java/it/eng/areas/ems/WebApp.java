/**
 *
 */
package it.eng.areas.ems;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.TaskScheduler;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.SpringLockableTaskSchedulerFactory;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Bifulco Luigi
 *
 */
@PropertySources(//
		value = { // file obbligatori:
				@PropertySource("classpath:/conf/cfg-jpa.properties"),
				@PropertySource("classpath:/conf/cfg-docservice.properties"),
				@PropertySource("classpath:/conf/cfg-common.properties"),
				@PropertySource("classpath:/conf/cfg-dto.properties"),
				@PropertySource("classpath:/conf/cfg-mail.properties"),
				@PropertySource("classpath:/conf/cfg-user.properties"),
				@PropertySource("classpath:/conf/cfg-job.properties"),
				@PropertySource("classpath:/conf/cfg-application.properties"),
				// proprietà in override, se le trova sovrascrive le proprietà
				// definite nei file di sopra
				@PropertySource(value = "${confPathAbsolute}/cfg-jpa.properties", ignoreResourceNotFound = true),
				@PropertySource(value = "${confPathAbsolute}/cfg-docservice.properties", ignoreResourceNotFound = true),
				@PropertySource(value = "${confPathAbsolute}/cfg-common.properties", ignoreResourceNotFound = true),
				@PropertySource(value = "${confPathAbsolute}/cfg-dto.properties", ignoreResourceNotFound = true),
				@PropertySource(value = "${confPathAbsolute}/cfg-mail.properties", ignoreResourceNotFound = true),
				@PropertySource(value = "${confPathAbsolute}/cfg-user.properties", ignoreResourceNotFound = true),
				@PropertySource(value = "${confPathAbsolute}/cfg-job.properties", ignoreResourceNotFound = true),
				@PropertySource(value = "${confPathAbsolute}/cfg-application.properties", ignoreResourceNotFound = true), })

@SpringBootApplication
@EnableAutoConfiguration
@EnableSwagger2
public class WebApp extends SpringBootServletInitializer {

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("it.eng.areas.ems.sdodaeservices.rest"))
				.paths(PathSelectors.any()).build().pathMapping("/");
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebApp.class, args);
	}

	@Bean
	public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
		// conf.
		conf.setIgnoreUnresolvablePlaceholders(true);
		return conf;
	}

	/**
	 * Utilizzato per permettere la coesistenza di più job su più istanze
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean
	public LockProvider lockProvider(DataSource dataSource) {
		return new JdbcTemplateLockProvider(dataSource, "dae_job_lock");
	}

	@Bean
	public TaskScheduler taskScheduler(LockProvider lockProvider) {
		int poolSize = 10;
		return SpringLockableTaskSchedulerFactory.newLockableTaskScheduler(poolSize, lockProvider);
	}

}
