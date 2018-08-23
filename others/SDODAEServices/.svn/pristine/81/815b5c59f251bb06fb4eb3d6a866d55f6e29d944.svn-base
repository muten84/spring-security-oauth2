package it.eng.areas.ems.sdodaeservices.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import it.eng.area118.sdocommon.persistence.configuration.TransactionManagementConfiguration;
import it.eng.areas.ems.sdodaeservices.dao.DaeDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.DaeDAOImpl;
import it.eng.areas.ems.sdodaeservices.gis.dao.VCTDaeDAO;
import it.eng.areas.ems.sdodaeservices.gis.dao.impl.VctDaeDAOImpl;
import it.eng.areas.ems.sdodaeservices.service.DaeTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.impl.DaeTransactionalServiceImpl;

@PropertySources(//
		value = { //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "classpath:META-INF/test/cfg-oracle-jpa.properties") //
		})
@Import(TransactionManagementConfiguration.class)
@Configuration
public class DaePersistenceTestCtx {

	@Bean
	DaeTransactionalService getService() {
		return new DaeTransactionalServiceImpl();
	}

	@Bean
	DaeDAO getDaeDAO() {
		return new DaeDAOImpl();
	}
	
	@Bean
	VCTDaeDAO getVctDaeDAO() {
		return new VctDaeDAOImpl();
	}

}