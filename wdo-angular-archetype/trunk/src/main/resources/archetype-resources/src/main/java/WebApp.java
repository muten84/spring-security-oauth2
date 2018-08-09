#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/**
 * 
 */
package ${package};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.ventiv.webjars.requirejs.EnableWebJarsRequireJs;

/**
 * @author Bifulco Luigi
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableWebJarsRequireJs
public class WebApp extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WebApp.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WebApp.class, args);
	}

	
}
