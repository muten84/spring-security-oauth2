package it.eng.areas.ems.sdodaeservices.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import it.eng.area118.sdocommon.persistence.configuration.TransactionManagementConfiguration;
import it.eng.area118.sdocommon.persistence.configuration.TwoDbTransactionManagerConfiguration;
import it.eng.areas.ems.sdodaeservices.dao.ExampleDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.ExampleDAOImpl;
import it.eng.areas.ems.sdodaeservices.service.ExampleTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.impl.ExampleTransactionalServiceImpl;

@PropertySources(//
		value = { //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "classpath:it/eng/areas/ems/sdodaeservices/test-context/cfg-hsqldb-jpa.properties") //
		})
@Import(TransactionManagementConfiguration.class)
@Configuration
public class ExamplePersistenceTestCtx {

	@Bean
	ExampleTransactionalService getService() {
		return new ExampleTransactionalServiceImpl();
	}

	@Bean
	ExampleDAO getExampleDAO() {
		return new ExampleDAOImpl();
	}

}