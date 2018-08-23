/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.persistence.graph;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import it.eng.area118.sdocommon.persistence.configuration.SingleDataSourceConfiguration;
import it.eng.areas.ems.sdodaeservices.dao.DaeDAO;
import it.eng.areas.ems.sdodaeservices.dao.EventDAO;
import it.eng.areas.ems.sdodaeservices.dao.FirstResponderDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.DaeDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.EventDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.FirstResponderDAOImpl;
import it.eng.areas.ems.sdodaeservices.service.GraphTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.impl.GraphTransactionalServiceImpl;

/**
 * @author Bifulco Luigi
 *
 */
@PropertySources(//
		value = { //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "classpath:META-INF/test/cfg-oracle-jpa.properties") //
		})
@Import(SingleDataSourceConfiguration.class)
@Configuration
public class GraphServiceTestContext {

	@Bean
	public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
		// conf.
		conf.setIgnoreUnresolvablePlaceholders(true);
		return conf;
	}

	@Bean
	public GraphTransactionalService graphTransactionalService() {
		return new GraphTransactionalServiceImpl();
	}

	@Bean
	public FirstResponderDAO frDAO() {
		return new FirstResponderDAOImpl();
	}

	@Bean
	public DaeDAO daeDAO() {
		return new DaeDAOImpl();
	}

	@Bean
	public EventDAO eventDAO() {
		return new EventDAOImpl();
	}
}
