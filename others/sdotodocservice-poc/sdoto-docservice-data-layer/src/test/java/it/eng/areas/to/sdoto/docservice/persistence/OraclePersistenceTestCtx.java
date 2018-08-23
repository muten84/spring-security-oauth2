package it.eng.areas.to.sdoto.docservice.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import it.eng.areas.to.sdoto.docservice.DataLayerDocServiceConfiguration;
import it.eng.areas.to.sdoto.docservice.dao.ExampleDAO;
import it.eng.areas.to.sdoto.docservice.dao.impl.ExampleDAOImpl;
import it.eng.areas.to.sdoto.docservice.service.ExampleTransactionalService;
import it.eng.areas.to.sdoto.docservice.service.impl.ExampleTransactionalServiceImpl;

@PropertySources(//
		value = { //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "classpath:it/eng/areas/to/sdoto/docservice/test-context/cfg-oracle-jpa.properties") //
		})
@Import(DataLayerDocServiceConfiguration.class)
@Configuration
@Profile("oracle")
public class OraclePersistenceTestCtx {

	@Bean
	ExampleTransactionalService getService() {
		return new ExampleTransactionalServiceImpl();
	}

	@Bean
	ExampleDAO getExampleDAO() {
		return new ExampleDAOImpl();
	}

}