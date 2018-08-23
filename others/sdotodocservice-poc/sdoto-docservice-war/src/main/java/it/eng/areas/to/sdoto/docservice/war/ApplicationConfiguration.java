/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.war;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import it.eng.areas.to.sdoto.docservice.delegate.configuration.DocServiceDelegateConfiguration;
import it.eng.areas.to.sdoto.docservice.delegate.configuration.MailSenderConfiguration;
import it.eng.areas.to.sdoto.docservice.rest.configuration.RestLayerConfiguration;
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
@ComponentScan(basePackages = "it.eng.areas.to.sdoto.docservice.swagger")
@EnableSwagger2
@EnableWebMvc
@Import({ RestLayerConfiguration.class, DocServiceDelegateConfiguration.class, MailSenderConfiguration.class })
public class ApplicationConfiguration extends WebMvcConfigurerAdapter {

	@Bean
	public Docket documentation() {
		System.out.println("=========================================== Initializing Swagger");
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any())//
				.build()//
				.pathMapping("/rest/*")//
				.apiInfo(metadata());
	}

	@Bean
	public UiConfiguration uiConfig() {
		return UiConfiguration.DEFAULT;
	}

	private ApiInfo metadata() {
		return new ApiInfoBuilder().title("My awesome API").description("Some description").version("1.0").contact("my-email@domain.org").build();
	}

	// @Bean
	// JerseySwaggerResourcesProvider jerseySwaggerResourcesProvider() {
	// return new JerseySwaggerResourcesProvider();
	// }

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
