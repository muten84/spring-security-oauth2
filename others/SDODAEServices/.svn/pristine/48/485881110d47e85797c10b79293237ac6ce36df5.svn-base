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
import it.eng.areas.ems.sdodaeservices.dao.ComuneDAO;
import it.eng.areas.ems.sdodaeservices.dao.ConfigDAO;
import it.eng.areas.ems.sdodaeservices.dao.DisponibilitaDAO;
import it.eng.areas.ems.sdodaeservices.dao.EnteCertificatoreDAO;
import it.eng.areas.ems.sdodaeservices.dao.ImageDAO;
import it.eng.areas.ems.sdodaeservices.dao.MailTemplateDAO;
import it.eng.areas.ems.sdodaeservices.dao.ProfessioneDAO;
import it.eng.areas.ems.sdodaeservices.dao.ProvinciaDAO;
import it.eng.areas.ems.sdodaeservices.dao.RuoloDAO;
import it.eng.areas.ems.sdodaeservices.dao.StatoDAEDAO;
import it.eng.areas.ems.sdodaeservices.dao.StatoDAO;
import it.eng.areas.ems.sdodaeservices.dao.StradeDAO;
import it.eng.areas.ems.sdodaeservices.dao.TipoStrutturaDAO;
import it.eng.areas.ems.sdodaeservices.dao.UtenteDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.CategoriaFrDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ComuneDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ConfigDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.DisponibilitaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.EnteCertificatoreDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ImageDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.MailTemplateDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ProfessioneDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ProvinciaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.RuoloDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StatoDAEDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StatoDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StradeDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.TipoStrutturaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.UtenteDAOImpl;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.impl.AnagraficheDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.MailDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.AnagraficheTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.impl.AnagraficheTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.transaction.DAETransactionManagementConfiguration;

@PropertySources(//
		value = { //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "classpath:META-INF/cfg-oracle-jpa.properties"),
		// @PropertySource(ignoreResourceNotFound = false, //
		// value = "classpath:META-INF/cfg-dto.properties")
		})
@Configuration
@Import({ DTOConfiguration.class, DAETransactionManagementConfiguration.class })
public class MailDelegateTestCtx {

	@Bean
	public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
		// conf.
		conf.setIgnoreUnresolvablePlaceholders(true);
		return conf;
	}

	@Bean
	public MailDelegateService mailDelegateService() {
		return new MailDelegateServiceImpl();
	}

	@Bean
	public ComuneDAO getComuneDao() {
		return new ComuneDAOImpl();
	}

	@Bean
	public StradeDAO getStradeDao() {
		return new StradeDAOImpl();
	}

	@Bean
	public ConfigDAO getConfigDAO() {
		return new ConfigDAOImpl();
	}

	@Bean
	public ProvinciaDAO getProvinciaDao() {
		return new ProvinciaDAOImpl();
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
	public MailTemplateDAO getMailTemplateDAO() {
		return new MailTemplateDAOImpl();
	}

	@Bean
	public CategoriaFrDAO getCategoriaDAO() {
		return new CategoriaFrDAOImpl();
	}

	@Bean
	public RuoloDAO getRuoloDao() {
		return new RuoloDAOImpl();
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
	public TipoStrutturaDAO getTipoStrutturaDao() {
		return new TipoStrutturaDAOImpl();
	}

	@Bean
	public AnagraficheDelegateService getAnagraficheDelegateService() {
		return new AnagraficheDelegateServiceImpl();
	}

	@Bean
	public AnagraficheTransactionalService getAnagraficheTransactionalService() {
		return new AnagraficheTransactionalServiceImpl();
	}

}
