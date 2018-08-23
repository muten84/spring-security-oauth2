/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.SwaggerConfigLocator;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import it.eng.areas.to.sdoto.docservice.rest.filter.CorsFilter;

/**
 * @author Bifulco Luigi
 *
 */
@Component
@Configuration
@ApplicationPath("/rest")
public class JerseyConfig extends ResourceConfig {

	// private final static Logger LOGGER =
	// org.slf4j.LoggerFactory.getLogger(JerseyConfig.class);

	public JerseyConfig() {
		// register(RequestContextFilter.class);
		// packages("it.eng.areas.to.sdoto.docservice.rest");
		// LoggingFeature logging = new LoggingFeature(LOGGER,
		// LoggingFeature.Verbosity.PAYLOAD_ANY);
		// register(logging);
		// register(JacksonFeature.class);
		// BeanConfig swaggerConfig = new BeanConfig();
		// swaggerConfig.setBasePath("/rest");
		// SwaggerConfigLocator.getInstance().putConfig(SwaggerContextService.CONFIG_ID_DEFAULT,
		// swaggerConfig);
		//
		// packages(getClass().getPackage().getName(),
		// ApiListingResource.class.getPackage().getName());

		register(RequestContextFilter.class);
		register(CorsFilter.class);
		packages("it.eng.areas.to.sdoto.docservice.rest");
		register(LoggingFeature.class);
		register(JacksonFeature.class);
		// BeanConfig swaggerConfig = new BeanConfig();
		// swaggerConfig.setBasePath("/rest");
		// SwaggerConfigLocator.getInstance().putConfig(SwaggerContextService.CONFIG_ID_DEFAULT,
		// swaggerConfig);
		// //
		// packages(getClass().getPackage().getName(),
		// ApiListingResource.class.getPackage().getName());
		configureSwagger();
	}

	private void configureSwagger() {
		// Available at localhost:port/swagger.json
		this.register(ApiListingResource.class);
		this.register(SwaggerSerializers.class);

		BeanConfig config = new BeanConfig();
		config.setConfigId("springboot-jersey-swagger-docker-example");
		config.setTitle("Spring Boot + Jersey + Swagger + Docker Example");
		// config.setVersion("v1");
		config.setContact("Luigi Bifulco");
		config.setSchemes(new String[] { "http", "https" });
		config.setBasePath("rest");
		config.setResourcePackage("it.eng.areas.to.sdoto.docservice.rest");
		config.setPrettyPrint(true);
		config.setScan(true);
		SwaggerConfigLocator.getInstance().putConfig(SwaggerContextService.CONFIG_ID_DEFAULT, config);
		packages(getClass().getPackage().getName(), ApiListingResource.class.getPackage().getName());
	}
}
