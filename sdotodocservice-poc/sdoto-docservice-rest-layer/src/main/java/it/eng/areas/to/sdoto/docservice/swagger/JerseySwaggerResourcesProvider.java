/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.swagger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * @author Bifulco Luigi
 *
 */
//@Component
//@Primary
public class JerseySwaggerResourcesProvider implements SwaggerResourcesProvider {

	@Resource
	private InMemorySwaggerResourcesProvider inMemorySwaggerResourcesProvider;
	
	@PostConstruct
	public void ok(){
		System.out.println(">>>>>>>>JerseySwaggerResourcesProvider>>>>>>>>>>>>>>>>");
	}

	@Override
	public List<SwaggerResource> get() {

		SwaggerResource jerseySwaggerResource = new SwaggerResource();
		jerseySwaggerResource.setLocation("/swagger.json");
		jerseySwaggerResource.setSwaggerVersion("2.0");
		jerseySwaggerResource.setName("Jersey");

		return Stream.concat(Stream.of(jerseySwaggerResource), inMemorySwaggerResourcesProvider.get().stream()).collect(Collectors.toList());
	}

}