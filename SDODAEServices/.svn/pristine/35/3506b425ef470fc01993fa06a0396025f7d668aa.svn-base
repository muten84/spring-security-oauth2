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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import it.eng.areas.ems.common.sdo.dto.configuration.DTOConfiguration;
import it.eng.areas.ems.common.sdo.mail.MailService;
import it.eng.areas.ems.common.sdo.mail.impl.MailServiceImpl;
import it.eng.areas.ems.sdodaeservices.dao.CategoriaFrDAO;
import it.eng.areas.ems.sdodaeservices.dao.ConfigDAO;
import it.eng.areas.ems.sdodaeservices.dao.DisponibilitaDAO;
import it.eng.areas.ems.sdodaeservices.dao.EnteCertificatoreDAO;
import it.eng.areas.ems.sdodaeservices.dao.FirstResponderDAO;
import it.eng.areas.ems.sdodaeservices.dao.GruppoDAO;
import it.eng.areas.ems.sdodaeservices.dao.ImageDAO;
import it.eng.areas.ems.sdodaeservices.dao.MailTemplateDAO;
import it.eng.areas.ems.sdodaeservices.dao.ProfessioneDAO;
import it.eng.areas.ems.sdodaeservices.dao.RuoloDAO;
import it.eng.areas.ems.sdodaeservices.dao.UtenteDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.CategoriaFrDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ConfigDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.DisponibilitaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.EnteCertificatoreDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.FirstResponderDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.GruppoDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ImageDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.MailTemplateDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ProfessioneDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.RuoloDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.UtenteDAOImpl;
import it.eng.areas.ems.sdodaeservices.delegate.GruppoDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.UserDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.impl.GruppoDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.UserDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.gis.dao.VCTDaeDAO;
import it.eng.areas.ems.sdodaeservices.gis.dao.impl.VctDaeDAOImpl;
import it.eng.areas.ems.sdodaeservices.service.GruppoTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.UserTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.impl.GruppoTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.UserTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.transaction.DAETransactionManagementConfiguration;

@PropertySources(value = {
		@PropertySource(ignoreResourceNotFound = false, value = "classpath:META-INF/cfg-oracle-jpa.properties") })
@Configuration
@Import({ DTOConfiguration.class, DAETransactionManagementConfiguration.class })
public class UserDelegateTestCtx {

	@Bean
	public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
		// conf.
		conf.setIgnoreUnresolvablePlaceholders(true);
		return conf;
	}

	@Bean
	public UserDelegateService userDelegateService() {
		return new UserDelegateServiceImpl();
	}

	@Bean
	public UserTransactionalService userTransactionalService() {
		return new UserTransactionalServiceImpl();
	}

	@Bean
	public GruppoDAO getGruppoDao() {
		return new GruppoDAOImpl();
	}

	@Bean
	public ConfigDAO getConfigDAO() {
		return new ConfigDAOImpl();
	}

	@Bean
	public FirstResponderDAO getFirstResponderDao() {
		return new FirstResponderDAOImpl();
	}

	@Bean
	public MailTemplateDAO getMailTemplateDAO() {
		return new MailTemplateDAOImpl();
	}

	@Bean
	public RuoloDAO getRuoloDao() {
		return new RuoloDAOImpl();
	}

	@Bean
	public VCTDaeDAO getVctDaeDao() {
		return new VctDaeDAOImpl();
	}

	@Bean
	public CategoriaFrDAO getCategoriaDAO() {
		return new CategoriaFrDAOImpl();
	}

	@Bean
	public UtenteDAO getUtenteDAO() {
		return new UtenteDAOImpl();
	}

	@Bean
	public ImageDAO getImageDAO() {
		return new ImageDAOImpl();
	}

	@Bean
	public DisponibilitaDAO getDisponiblitaDAO() {
		return new DisponibilitaDAOImpl();
	}

	@Bean
	public ProfessioneDAO getProfessioneDao() {
		return new ProfessioneDAOImpl();
	}

	@Bean
	public MailService getMailService() {
		return new MailServiceImpl();
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		return new JavaMailSenderImpl();
	}

	@Bean
	public SimpleMailMessage getSimpleMailMessage() {
		return new SimpleMailMessage();
	}

	@Bean
	public EnteCertificatoreDAO getEnteCertificatoreDao() {
		return new EnteCertificatoreDAOImpl();
	}

	@Bean
	public GruppoTransactionalService getGruppoService() {
		return new GruppoTransactionalServiceImpl();
	}

	@Bean
	public GruppoDelegateService getGruppoDelegateService() {
		return new GruppoDelegateServiceImpl();
	}
}
