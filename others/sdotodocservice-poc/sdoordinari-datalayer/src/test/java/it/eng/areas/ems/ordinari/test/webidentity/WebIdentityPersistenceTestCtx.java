package it.eng.areas.ems.ordinari.test.webidentity;

import it.eng.area118.sdocommon.persistence.configuration.TransactionManagementConfiguration;
import it.eng.areas.ems.ordinari.dao.ToBookingDAO;
import it.eng.areas.ems.ordinari.dao.WebIdentityDAO;
import it.eng.areas.ems.ordinari.dao.WebSessionDAO;
import it.eng.areas.ems.ordinari.dao.impl.ToBookingDAOImpl;
import it.eng.areas.ems.ordinari.dao.impl.WebIdentityDAOImpl;
import it.eng.areas.ems.ordinari.dao.impl.WebSessionDAOImpl;
import it.eng.areas.ems.ordinari.service.BookingTransactionalService;
import it.eng.areas.ems.ordinari.service.WebIdentityService;
import it.eng.areas.ems.ordinari.service.impl.BookingTransactionalServiceImpl;
import it.eng.areas.ems.ordinari.service.impl.WebIdentityServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


@Import(TransactionManagementConfiguration.class)
@Configuration
@Profile("hsqldb")
@PropertySources(//
		value = { //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "classpath:it.eng.areas.ems.ordinari.test/cfg-hsqldb-webidentity.properties") //
		})
public class WebIdentityPersistenceTestCtx {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		configurer.setIgnoreResourceNotFound(true);
		configurer.setIgnoreUnresolvablePlaceholders(true);
		return configurer;
	}
	
	@Bean
	BookingTransactionalService bookingTransactionalService() {
		return new BookingTransactionalServiceImpl();
	}

	@Bean
	ToBookingDAO toBookingDAO() {
		return new ToBookingDAOImpl();
	}

	@Bean
	WebIdentityDAO getIdentityDAO() {
		return new WebIdentityDAOImpl();
	}

	@Bean
	WebSessionDAO getSessionDAO() {
		return new WebSessionDAOImpl();
	}

	@Bean
	WebIdentityService getWebIdentityService() {
		return new WebIdentityServiceImpl();
	}

}