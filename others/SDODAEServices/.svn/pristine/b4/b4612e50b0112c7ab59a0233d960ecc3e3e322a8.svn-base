/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.stereotype.Component;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.SwaggerConfigLocator;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import it.eng.areas.ems.sdodaeservices.rest.exception.JsonMappingExceptionMapper;
import it.eng.areas.ems.sdodaeservices.rest.exception.JsonParseExceptionMapper;
import it.eng.areas.ems.sdodaeservices.rest.exception.UmbrellaExceptionMapper;
import it.eng.areas.ems.sdodaeservices.rest.exception.UnrecognizedPropertyExceptionMapper;
import it.eng.areas.ems.sdodaeservices.rest.listener.DAEApplicationEventListener;
import it.eng.areas.ems.sdodaeservices.rest.listener.DAERequestEventListener;

/**
 * @author Bifulco Luigi
 *
 */
@Component
@ApplicationPath("/rest")
public class RestApplication extends ResourceConfig {

	public RestApplication() {
		register(RequestContextFilter.class);
		register(DAEApplicationEventListener.class);
		register(DAERequestEventListener.class);
		packages("it.eng.areas.ems.sdodaeservices.rest", "it.eng.areas.ems.sdodaeservices.rest.exception");
		register(LoggingFeature.class);
		register(com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider.class);

		register(JsonParseExceptionMapper.class);
		register(JsonMappingExceptionMapper.class);
		register(UnrecognizedPropertyExceptionMapper.class);
		register(UmbrellaExceptionMapper.class);

		// configureSwagger();
	}

	private void configureSwagger() {
		// Available at localhost:port/swagger.json
		this.register(ApiListingResource.class);
		this.register(SwaggerSerializers.class);

		BeanConfig config = new BeanConfig();
		config.setConfigId("springboot-jersey-swagger-docker-DAE");
		config.setTitle("Spring Boot + Jersey + Swagger + DAE Services");
		// config.setVersion("v1");
		config.setContact("Luigi Bifulco");
		config.setSchemes(new String[] { "http", "https" });
		config.setBasePath("SDODAEServices-webapp/rest");
		config.setResourcePackage("it.eng.areas.ems.sdodaeservices.rest");
		config.setPrettyPrint(true);
		config.setScan(true);
		SwaggerConfigLocator.getInstance().putConfig(SwaggerContextService.CONFIG_ID_DEFAULT, config);
		packages(getClass().getPackage().getName(), ApiListingResource.class.getPackage().getName());
	}
}
