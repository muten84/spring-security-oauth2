/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.war;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.TaskScheduler;

import it.eng.areas.ems.common.sdo.dto.configuration.DTOConfiguration;
import it.eng.areas.ems.common.sdo.mail.MailService;
import it.eng.areas.ems.common.sdo.mail.configuration.SendMailConfiguration;
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
import it.eng.areas.ems.sdodaeservices.dao.ProgrammaManutenzioneDAO;
import it.eng.areas.ems.sdodaeservices.dao.ProgrammaManutenzioneHistoryDAO;
import it.eng.areas.ems.sdodaeservices.dao.ProvinciaDAO;
import it.eng.areas.ems.sdodaeservices.dao.QuestionarioDAO;
import it.eng.areas.ems.sdodaeservices.dao.ResetPasswordDAO;
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
import it.eng.areas.ems.sdodaeservices.dao.impl.ProgrammaManutenzioneDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ProgrammaManutenzioneHistoryDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ProvinciaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.QuestionarioDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ResetPasswordDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.ResponsabileProvinciaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.RuoloDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StaticDataDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StatoDAEDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StatoDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.StradeDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.TipoStrutturaDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.UtenteDAOImpl;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.DaeDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.EventDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.GraphServiceDelegate;
import it.eng.areas.ems.sdodaeservices.delegate.GruppoDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MessagesDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.PushNotificationService;
import it.eng.areas.ems.sdodaeservices.delegate.UserDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.impl.AnagraficheDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.AuthenticationDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.DaeDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.EventDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.FirstResponderDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.GraphServiceDelegateImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.GruppoDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.MailDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.MessagesDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.PrivacyDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.impl.PushWooshNotificationService;
import it.eng.areas.ems.sdodaeservices.delegate.impl.UserDelegateServiceImpl;
import it.eng.areas.ems.sdodaeservices.delegate.job.DoNotDisturbJob;
import it.eng.areas.ems.sdodaeservices.delegate.job.FRCleanJob;
import it.eng.areas.ems.sdodaeservices.delegate.job.ScadenzeJob;
import it.eng.areas.ems.sdodaeservices.delegate.service.AuthenticationDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.service.PrivacyDelegateService;
import it.eng.areas.ems.sdodaeservices.gis.dao.VCTDaeDAO;
import it.eng.areas.ems.sdodaeservices.gis.dao.impl.VctDaeDAOImpl;
import it.eng.areas.ems.sdodaeservices.rest.service.GeocodeService;
import it.eng.areas.ems.sdodaeservices.rest.utils.FilterUtils;
import it.eng.areas.ems.sdodaeservices.service.AnagraficheTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.AuthenticationService;
import it.eng.areas.ems.sdodaeservices.service.DaeTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.EventTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.FirstResponderTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.GraphTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.GruppoTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.MailTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.MessagesTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.UserTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.impl.AnagraficheTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.AuthenticationServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.DaeTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.EventTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.FirstResponderTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.GraphTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.GruppoTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.MailTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.MessagesTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.service.impl.UserTransactionalServiceImpl;
import it.eng.areas.ems.sdodaeservices.transaction.DAETransactionManagementConfiguration;
import it.eng.areas.ems.sdodaeservices.utils.DAEUtils;
import it.eng.areas.ems.sdodaeservices.utils.PasswordUtils;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.SpringLockableTaskSchedulerFactory;

@Configuration
@Import({ RestLayerConfiguration.class, DTOConfiguration.class, DAETransactionManagementConfiguration.class, SendMailConfiguration.class })
public class ApplicationConfiguration {

	@Bean
	public DoNotDisturbJob getDoNotDisturbJob() {
		return new DoNotDisturbJob();
	}

	@Bean
	public PasswordUtils getPasswordUtils() {
		// imposto a true il bypass dato che sono i servizi per l'app mobile
		return new PasswordUtils();
	}

	@Bean
	public FilterUtils getFilterUtils() {
		// imposto a true il bypass dato che sono i servizi per l'app mobile
		return new FilterUtils(true);
	}

	@Bean
	public GeocodeService getGeocodeService() {
		return new GeocodeService();
	}

	@Bean
	public FRCleanJob getFRCleanJob() {
		return new FRCleanJob();
	}

	/**
	 * Utilizzato per permettere la coesistenza dei job su più istanze
	 * 
	 * @param dataSource
	 * @return
	 * 
	 */
	@Bean
	public LockProvider lockProvider(DataSource dataSource) {
		return new JdbcTemplateLockProvider(dataSource, "dae_job_lock");
	}

	@Bean
	public DashboardPositionDAO getDashboardPositionDAO() {
		return new DashboardPositionDAOImpl();
	}

	@Bean
	public LocalitaDAO getLocalitaDAO() {
		return new LocalitaDAOImpl();
	}

	@Bean
	public DaeHistoryDAO getDaeHistoryDAO() {
		return new DaeHistoryDAOImpl();
	}

	@Bean
	public ProgrammaManutenzioneDAO getProgrammaManutenzioneDAO() {
		return new ProgrammaManutenzioneDAOImpl();
	}

	@Bean
	public ProgrammaManutenzioneHistoryDAO getProgrammaManutenzioneHistDAO() {
		return new ProgrammaManutenzioneHistoryDAOImpl();
	}

	@Bean
	public DAEUtils getDAEUtils() {
		return new DAEUtils();
	}

	@Bean
	public TaskScheduler taskScheduler(LockProvider lockProvider) {
		int poolSize = 10;
		return SpringLockableTaskSchedulerFactory.newLockableTaskScheduler(poolSize, lockProvider);
	}

	@Bean
	public StaticDataDAO getStaticDataDAO() {
		return new StaticDataDAOImpl();
	}

	@Bean
	public ResponsabileProvinciaDAO getResponsabileProvinciaDAO() {
		return new ResponsabileProvinciaDAOImpl();
	}

	@Bean
	public FRPositionToCODAO getFRPositionToCODAO() {
		return new FRPositionToCODAOImpl();
	}

	@Bean
	public ScadenzeJob scadenzeJob() {
		return new ScadenzeJob();
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
	public MailDelegateService mailDelegateService() {
		return new MailDelegateServiceImpl();
	}

	@Bean
	public MailTransactionalService mailTransactionalService() {
		return new MailTransactionalServiceImpl();
	}

	@Bean
	public DispositiviDAO dispositiviDAO() {
		return new DispositiviDAOImpl();
	}

	@Bean
	public PasswordHistoryDAO passwordHistoryDAO() {
		return new PasswordHistoryDAOImpl();
	}

	@Bean
	public MailTraceDAO mailTraceDAO() {
		return new MailTraceDAOImpl();
	}

	@Bean
	public DaeFaultDAO daeFaultDAO() {
		return new DaeFaultDAOImpl();
	}

	@Bean
	public MailTemplateDAO mailTemplateDAO() {
		return new MailTemplateDAOImpl();
	}

	@Bean
	public DaeDelegateService getDaeDelegateService() {
		return new DaeDelegateServiceImpl();
	}

	@Bean
	public CategoriaFrDAO getCategoriaDAO() {
		return new CategoriaFrDAOImpl();
	}

	public MessagesDelegateService getMessageDelegateService() {
		return new MessagesDelegateServiceImpl();
	}

	@Bean
	public DaeTransactionalService getDaeTransactionalService() {
		return new DaeTransactionalServiceImpl();
	}

	@Bean
	public GraphTransactionalService graphTransactionalService() {
		return new GraphTransactionalServiceImpl();
	}

	@Bean
	public GraphServiceDelegate graphServiceDelegate() {
		return new GraphServiceDelegateImpl();
	}

	@Bean
	public PrivacyDelegateService getPrivacyDelegateService() {
		return new PrivacyDelegateServiceImpl();
	}

	@Bean
	public MessagesDelegateService getMessagesDelegateService() {
		return new MessagesDelegateServiceImpl();
	}

	@Bean
	public MessagesTransactionalService getMessagesTransactionalService() {
		return new MessagesTransactionalServiceImpl();
	}

	@Bean
	public MailTemplateDAO getMailTemplateDAO() {
		return new MailTemplateDAOImpl();
	}

	@Bean
	public InterventoCoordDAO getnterventoCoordDAO() {
		return new InterventoCoordDAOImpl();
	}

	@Bean
	public MessaggioDAO getMessaggioDAO() {
		return new MessaggioDAOImpl();
	}

	@Bean
	public DaeDAO getDaeDao() {
		return new DaeDAOImpl();
	}

	@Bean
	public VCTDaeDAO getVctDaeDao() {
		return new VctDaeDAOImpl();
	}

	@Bean
	public AuthenticationService getAuthService() {
		return new AuthenticationServiceImpl();
	}

	@Bean
	public UtenteDAO getUtenteDAO() {
		return new UtenteDAOImpl();
	}

	@Bean
	public DisponibilitaDAO getDisponibilitaDAO() {
		return new DisponibilitaDAOImpl();
	}

	@Bean
	public ImageDAO getImageDAO() {
		return new ImageDAOImpl();
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
	public InterventoDAO getInterventionDAOImpl() {
		return new InterventoDAOImpl();
	}

	@Bean
	public PushNotificationService getPushNotificatioNService() {
		return new PushWooshNotificationService();
	}

	@Bean
	public FirstResponderTransactionalService getFirstResponderTransactionalService() {
		return new FirstResponderTransactionalServiceImpl();
	}

	@Bean
	public FirstResponderDAO getFirstResponderDAO() {
		return new FirstResponderDAOImpl();
	}

	@Bean
	public QuestionarioDAO getQuestionarioDAO() {
		return new QuestionarioDAOImpl();
	}

	@Bean
	public RuoloDAO getRuoloDAO() {
		return new RuoloDAOImpl();
	}

	@Bean
	public ResetPasswordDAO getResetPasswordDAO() {
		return new ResetPasswordDAOImpl();
	}

	@Bean
	public EventTransactionalService getEventTransService() {
		return new EventTransactionalServiceImpl();
	}

	@Bean
	public EventDAO getEventDAO() {
		return new EventDAOImpl();
	}

	@Bean
	public NotificheEventoDAO getNotificheEventoDao() {
		return new NotificheEventoDAOImpl();
	}

	@Bean
	public EventDelegateService getEventDelegateService() {
		return new EventDelegateServiceImpl();
	}

	@Bean
	public GruppoDelegateService getGruppoDelegateService() {
		return new GruppoDelegateServiceImpl();
	}

	@Bean
	public FirstResponderDelegateService getFirstResponderDelegateService() {
		return new FirstResponderDelegateServiceImpl();
	}

	@Bean
	public AuthenticationDelegateService getAuthDelegateService() {
		return new AuthenticationDelegateServiceImpl();
	}

	@Bean
	public MobileTokenDAO getMobileTokenDAO() {
		return new MobileTokenDAOImpl();
	}

	@Bean
	public ComuneDAO getComuneDAO() {
		return new ComuneDAOImpl();

	}

	@Bean
	public ProvinciaDAO getProvinceDAO() {
		return new ProvinciaDAOImpl();
	}

	@Bean
	public GruppoTransactionalService getGruppoTransactionalService() {
		return new GruppoTransactionalServiceImpl();
	}

	@Bean
	public TipoStrutturaDAO getTipoStrutturaDAO() {
		return new TipoStrutturaDAOImpl();
	}

	@Bean
	public AnagraficheTransactionalService getAnagTransService() {
		return new AnagraficheTransactionalServiceImpl();
	}

	@Bean
	public AnagraficheDelegateService getAnagDeleService() {
		return new AnagraficheDelegateServiceImpl();
	}

	@Bean
	public StradeDAO getStradeDAO() {
		return new StradeDAOImpl();
	}

	@Bean
	public ConfigDAO getConfigDAO() {
		return new ConfigDAOImpl();
	}

	@Bean
	public GruppoDAO getGruppoDAO() {
		return new GruppoDAOImpl();
	}

}