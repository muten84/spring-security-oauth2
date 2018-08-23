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
import it.eng.areas.ems.sdodaeservices.dao.ComuneDAO;
import it.eng.areas.ems.sdodaeservices.dao.ConfigDAO;
import it.eng.areas.ems.sdodaeservices.dao.DaeDAO;
import it.eng.areas.ems.sdodaeservices.dao.DaeFaultDAO;
import it.eng.areas.ems.sdodaeservices.dao.DaeHistoryDAO;
import it.eng.areas.ems.sdodaeservices.dao.DashboardPositionDAO;
import it.eng.areas.ems.sdodaeservices.dao.DisponibilitaDAO;
import it.eng.areas.ems.sdodaeservices.dao.DispositiviDAO;
import it.eng.areas.ems.sdodaeservices.dao.EnteCertificatoreDAO;
import it.eng.areas.ems.sdodaeservices.dao.EventDAO;
import it.eng.areas.ems.sdodaeservices.dao.FRPositionToCODAO;
import it.eng.areas.ems.sdodaeservices.dao.FirstResponderDAO;
import it.eng.areas.ems.sdodaeservices.dao.GruppoDAO;
import it.eng.areas.ems.sdodaeservices.dao.ImageDAO;
import it.eng.areas.ems.sdodaeservices.dao.InterventoCoordDAO;
import it.eng.areas.ems.sdodaeservices.dao.InterventoDAO;
import it.eng.areas.ems.sdodaeservices.dao.LocalitaDAO;
import it.eng.areas.ems.sdodaeservices.dao.MailTemplateDAO;
import it.eng.areas.ems.sdodaeservices.dao.MailTraceDAO;
import it.eng.areas.ems.sdodaeservices.dao.MessaggioDAO;
import it.eng.areas.ems.sdodaeservices.dao.MobileTokenDAO;
import it.eng.areas.ems.sdodaeservices.dao.NotificheEventoDAO;
import it.eng.areas.ems.sdodaeservices.dao.PasswordHistoryDAO;
import it.eng.areas.ems.sdodaeservices.dao.ProfessioneDAO;
import it.eng.areas.ems.sdodaeservices.dao.ProvinciaDAO;
import it.eng.areas.ems.sdodaeservices.dao.QuestionarioDAO;
import it.eng.areas.ems.sdodaeservices.dao.ResponsabileProvinciaDAO;
import it.eng.areas.ems.sdodaeservices.dao.RuoloDAO;
import it.eng.areas.ems.sdodaeservices.dao.StaticDataDAO;
import it.eng.areas.ems.sdodaeservices.dao.StatoDAEDAO;
import it.eng.areas.ems.sdodaeservices.dao.StatoDAO;
import it.eng.areas.ems.sdodaeservices.dao.StradeDAO;
import it.eng.areas.ems.sdodaeservices.dao.TipoStrutturaDAO;
import it.eng.areas.ems.sdodaeservices.dao.UtenteDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.CategoriaFrDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ComuneDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ConfigDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.DaeDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.DaeFaultDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.DaeHistoryDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.DashboardPositionDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.DisponibilitaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.DispositiviDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.EnteCertificatoreDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.EventDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.FRPositionToCODAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.FirstResponderDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.GruppoDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ImageDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.InterventoCoordDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.InterventoDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.LocalitaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.MailTemplateDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.MailTraceDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.MessaggioDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.MobileTokenDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.NotificheEventoDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.PasswordHistoryDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ProfessioneDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ProvinciaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.QuestionarioDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ResponsabileProvinciaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.RuoloDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StaticDataDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StatoDAEDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StatoDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StradeDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.TipoStrutturaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.UtenteDAOImpl;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.EventDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MessagesDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.PushNotificationService;
import it.eng.areas.ems.sdodaeservices.delegate.impl.AnagraficheDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.EventDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.FirstResponderDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.MailDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.MessagesDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.PushWooshNotificationService;
import it.eng.areas.ems.sdodaeservices.gis.dao.VCTDaeDAO;
import it.eng.areas.ems.sdodaeservices.gis.dao.impl.VctDaeDAOImpl;
import it.eng.areas.ems.sdodaeservices.service.AnagraficheTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.DaeTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.EventTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.FirstResponderTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.MailTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.MessagesTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.UserTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.impl.AnagraficheTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.DaeTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.EventTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.FirstResponderTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.MailTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.MessagesTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.UserTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.transaction.DAETransactionManagementConfiguration;
import it.eng.areas.ems.sdodaeservices.utils.DAEUtils;

@PropertySources(//
		value = { //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "classpath:META-INF/cfg-oracle-jpa.properties"),
		// @PropertySource(ignoreResourceNotFound = false, //
		// value = "classpath:META-INF/cfg-dto.properties")
		})
@Configuration
@Import({ DTOConfiguration.class, DAETransactionManagementConfiguration.class })
public class FirstResponderDelegateTestCtx {

	@Bean
	public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer conf = new PropertySourcesPlaceholderConfigurer();
		// conf.
		conf.setIgnoreUnresolvablePlaceholders(true);
		return conf;
	}

	@Bean
	public DAEUtils getDAEUtils() {
		return new DAEUtils();
	}

	@Bean
	public DaeHistoryDAO getDaeHistoryDAO() {
		return new DaeHistoryDAOImpl();
	}

	@Bean
	public MobileTokenDAO getMobileTokenDAO() {
		return new MobileTokenDAOImpl();
	}

	@Bean
	public DashboardPositionDAO getDashboardPositionDAO() {
		return new DashboardPositionDAOImpl();
	}

	@Bean
	public ResponsabileProvinciaDAO getResponsabileProvinciaDAO() {
		return new ResponsabileProvinciaDAOImpl();
	}

	@Bean
	public StaticDataDAO getStaticDataDAO() {
		return new StaticDataDAOImpl();
	}

	@Bean
	public LocalitaDAO getLocalitaDAO() {
		return new LocalitaDAOImpl();
	}

	@Bean
	public FRPositionToCODAO getFRPositionToCODAO() {
		return new FRPositionToCODAOImpl();
	}

	@Bean
	public MailDelegateService getMailDelegateService() {
		return new MailDelegateServiceImpl();
	}

	@Bean
	public MailTransactionalService getMailTransactionalService() {
		return new MailTransactionalServiceImpl();
	}

	@Bean
	public UserTransactionalService getUserTransactionalService() {
		return new UserTransactionalServiceImpl();
	}

	@Bean
	public DaeFaultDAO getDaeFaultDAO() {
		return new DaeFaultDAOImpl();
	}

	@Bean
	public PasswordHistoryDAO getPasswordHistoryDAO() {
		return new PasswordHistoryDAOImpl();
	}

	@Bean
	public GruppoDAO getGruppoDAO() {
		return new GruppoDAOImpl();
	}

	@Bean
	public MailTraceDAO getMailTraceDAO() {
		return new MailTraceDAOImpl();
	}

	@Bean
	public EventDAO getEventDao() {
		return new EventDAOImpl();
	}

	@Bean
	public InterventoCoordDAO getInterventoCoordDAO() {
		return new InterventoCoordDAOImpl();
	}

	@Bean
	public DispositiviDAO getDispositiviDAO() {
		return new DispositiviDAOImpl();
	}

	@Bean
	public InterventoDAO getInterventoDao() {
		return new InterventoDAOImpl();
	}

	@Bean
	public RuoloDAO getRuoloDao() {
		return new RuoloDAOImpl();
	}

	@Bean
	public PushNotificationService getPushNotificatioNService() {
		return new PushWooshNotificationService();
	}

	@Bean
	public EventTransactionalService getEventService() {
		return new EventTransactionalServiceImpl();
	}

	@Bean
	public QuestionarioDAO getQuestionarioDao() {
		return new QuestionarioDAOImpl();
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
	public MailTemplateDAO getMailTemplateDAO() {
		return new MailTemplateDAOImpl();
	}

	@Bean
	public DisponibilitaDAO getDisponiblitaDAO() {
		return new DisponibilitaDAOImpl();
	}

	@Bean
	public NotificheEventoDAO getNotificheEventoDAO() {
		return new NotificheEventoDAOImpl();
	}

	@Bean
	public CategoriaFrDAO getCategoriaDAO() {
		return new CategoriaFrDAOImpl();
	}

	@Bean
	public MessagesDelegateService getMessageDelegateService() {
		return new MessagesDelegateServiceImpl();
	}

	@Bean
	public MessagesTransactionalService getMessagesTransactionalService() {
		return new MessagesTransactionalServiceImpl();
	}

	@Bean
	public MessaggioDAO getMessaggioDAO() {
		return new MessaggioDAOImpl();
	}

	@Bean
	public DaeTransactionalService getDaeTransactionalService() {
		return new DaeTransactionalServiceImpl();
	}

	@Bean
	public EventDelegateService getEventDelegateService() {
		return new EventDelegateServiceImpl();
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
	public ProvinciaDAO getProvinciaDao() {
		return new ProvinciaDAOImpl();
	}

	@Bean
	public TipoStrutturaDAO getTipoStrutturaDao() {
		return new TipoStrutturaDAOImpl();
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
	public DaeDAO getDAEDAO() {
		return new DaeDAOImpl();
	}

	@Bean
	public StatoDAO getStatoDAO() {
		return new StatoDAOImpl();
	}

	@Bean
	public AnagraficheTransactionalService getAnagraficheTransationalService() {
		return new AnagraficheTransactionalServiceImpl();
	}

	@Bean
	public ConfigDAO getConfigDAO() {
		return new ConfigDAOImpl();
	}

	@Bean
	public AnagraficheDelegateService getAnagraficheDelegateService() {
		return new AnagraficheDelegateServiceImpl();
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
	public FirstResponderTransactionalService getFirstResponderService() {
		return new FirstResponderTransactionalServiceImpl();
	}

	@Bean
	public FirstResponderDelegateService getFirstResponderDelegateService() {
		return new FirstResponderDelegateServiceImpl();
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

}
