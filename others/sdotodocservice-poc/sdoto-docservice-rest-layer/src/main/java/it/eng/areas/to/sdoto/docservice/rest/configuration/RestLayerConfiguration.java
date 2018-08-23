/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Bifulco Luigi
 *
 */
@Configuration
@ComponentScan(basePackages = "it.eng.areas.to.sdoto.docservice.rest")
//@EnableSwagger2
public class RestLayerConfiguration {

	// @Bean
	// public ApiListingResource apiListingResource() {
	// return new ApiListingResource();
	// }
	//
	// @Bean
	// public SwaggerSerializers swaggerSerializers() {
	// return new SwaggerSerializers();
	// }
	//
	// @Bean
	// public BeanConfig beanConfig() {
	// BeanConfig config = new BeanConfig();
	// config.setSchemes(new String[]{"http", "https"});
	// config.setResourcePackage("it.eng.areas.to.sdoto.docservice.rest.service");
	// config.setVersion("0.0.1-SNAPSHOT");
	// config.setHost("127.0.0.1:8088");
	// config.setBasePath("/rest");
	// //config.setScan(false);
	// config.setDescription("Docuemnt Rest Service");
	// return config;
	// }

	// @Override
	// public void addResourceHandlers(ResourceHandlerRegistry registry) {
	// registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
	//
	// registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	// }
}
