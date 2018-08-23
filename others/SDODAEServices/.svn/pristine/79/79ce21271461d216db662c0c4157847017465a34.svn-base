/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.persistence.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import it.eng.area118.sdocommon.persistence.configuration.SingleDataSourceConfiguration;
import it.eng.areas.ems.sdodaeservices.dao.GruppoDAO;
import it.eng.areas.ems.sdodaeservices.dao.ImageDAO;
import it.eng.areas.ems.sdodaeservices.dao.PasswordHistoryDAO;
import it.eng.areas.ems.sdodaeservices.dao.RuoloDAO;
import it.eng.areas.ems.sdodaeservices.dao.UtenteDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.GruppoDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ImageDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.PasswordHistoryDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.RuoloDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.UtenteDAOImpl;
import it.eng.areas.ems.sdodaeservices.service.UserTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.impl.UserTransactionalServiceImpl;

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
public class UserServiceCtx {

	@Bean
	public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
		// conf.
		conf.setIgnoreUnresolvablePlaceholders(true);
		return conf;
	}

	@Bean
	public UserTransactionalService userTransactionalService() {
		return new UserTransactionalServiceImpl();
	}

	@Bean
	public UtenteDAO utenteDAO() {
		return new UtenteDAOImpl();
	}

	@Bean
	public ImageDAO imageDAO() {
		return new ImageDAOImpl();
	}

	@Bean
	public RuoloDAO ruoloDAO() {
		return new RuoloDAOImpl();
	}

	@Bean
	public GruppoDAO gruppoDAO() {
		return new GruppoDAOImpl();
	}

	@Bean
	public PasswordHistoryDAO pwHistoryDAO() {
		return new PasswordHistoryDAOImpl();
	}

}
