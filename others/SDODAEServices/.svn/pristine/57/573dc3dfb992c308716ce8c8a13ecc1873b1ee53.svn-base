/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import it.eng.areas.ems.common.sdo.dto.configuration.DTOConfiguration;
import it.eng.areas.ems.sdodaeservices.dao.CategoriaFrDAO;
import it.eng.areas.ems.sdodaeservices.dao.ColoreDAO;
import it.eng.areas.ems.sdodaeservices.dao.ConfigDAO;
import it.eng.areas.ems.sdodaeservices.dao.DisponibilitaDAO;
import it.eng.areas.ems.sdodaeservices.dao.EnteCertificatoreDAO;
import it.eng.areas.ems.sdodaeservices.dao.FirstResponderDAO;
import it.eng.areas.ems.sdodaeservices.dao.ImageDAO;
import it.eng.areas.ems.sdodaeservices.dao.MailTemplateDAO;
import it.eng.areas.ems.sdodaeservices.dao.ProfessioneDAO;
import it.eng.areas.ems.sdodaeservices.dao.StatoDAEDAO;
import it.eng.areas.ems.sdodaeservices.dao.StatoDAO;
import it.eng.areas.ems.sdodaeservices.dao.UtenteDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.CategoriaFrDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ColoreDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ConfigDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.DisponibilitaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.EnteCertificatoreDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.FirstResponderDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ImageDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.MailTemplateDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ProfessioneDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StatoDAEDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StatoDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.UtenteDAOImpl;
import it.eng.areas.ems.sdodaeservices.delegate.CategoriaFrDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.impl.CategoriaFrDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.gis.dao.VCTDaeDAO;
import it.eng.areas.ems.sdodaeservices.gis.dao.impl.VctDaeDAOImpl;
import it.eng.areas.ems.sdodaeservices.service.CategoriaFrTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.impl.CategoriaFrTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.transaction.DAETransactionManagementConfiguration;

@PropertySources(//
		value = { //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "classpath:META-INF/cfg-oracle-jpa.properties"),
//						@PropertySource(ignoreResourceNotFound = false, //
//						value = "classpath:META-INF/cfg-dto.properties")		
		})
@Configuration
@Import({ DTOConfiguration.class,DAETransactionManagementConfiguration.class})
public class CategoriaFrDelegateTestCtx {
	
	@Bean
	public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
		// conf.
		conf.setIgnoreUnresolvablePlaceholders(true);
		return conf;
	}
	
	@Bean
	public MailTemplateDAO getMailTemplateDAO()
	{
		return new MailTemplateDAOImpl();
	}

	@Bean
	public CategoriaFrDAO getCategoriaFrDao() {
		return new CategoriaFrDAOImpl();
	}
	
	@Bean
	public ConfigDAO getConfigDAO() {
		return new ConfigDAOImpl();
	}


	@Bean
	public ColoreDAO getColoreDao() {
		return new ColoreDAOImpl();
	}
	
	
	@Bean
	public UtenteDAO getUtenteDAO() {
		return new UtenteDAOImpl();
	}
	
	@Bean
	public ImageDAO getImageDAO()
	{
		return new ImageDAOImpl();
	}

	
	@Bean
	public DisponibilitaDAO getDisponiblitaDAO()
	{
		return new DisponibilitaDAOImpl();
	}

	@Bean
	public FirstResponderDAO getFirstResponderDao() {
		return new FirstResponderDAOImpl();
	}
	
	@Bean
	public VCTDaeDAO getVctDaeDao() {
		return new VctDaeDAOImpl();
	}

	@Bean
	public ProfessioneDAO getProfessioneDao() {
		return new ProfessioneDAOImpl();
	}
	@Bean
	public EnteCertificatoreDAO getEnteCertificatoreDao() {
		return new EnteCertificatoreDAOImpl();
	}
	
	@Bean
	public StatoDAEDAO getStatoDAEDAO() {
		return new StatoDAEDAOImpl();
	}
		
	@Bean
	public StatoDAO getStatoDAO() {
		return new StatoDAOImpl();
	}
	
	@Bean
	public CategoriaFrTransactionalService getCategoriaFrService() {
	return new CategoriaFrTransactionalServiceImpl();
	}

		
	@Bean
	public CategoriaFrDelegateService getCategoriaFrDelegateService() {
		return new CategoriaFrDelegateServiceImpl();
	}
}
