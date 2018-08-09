package it.eng.areas.ems.sdodaeservices.delegate.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.DaeDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.Dae;
import it.eng.areas.ems.sdodaeservices.delegate.model.EntityType;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplateEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.ManutenzioneEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.DaeFilter;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.entity.TipoManutenzioneEnum;
import it.esel.parsley.lang.StringUtils;
import net.javacrumbs.shedlock.core.SchedulerLock;

/**
 * 
 * @author Miranda Mauro
 * @see <a href= "https://github.com/lukas-krecan/ShedLock">https://github.com/
 *      lukas-krecan/ShedLock</a>
 */
@Component
public class ScadenzeJob {

	private Logger logger = LoggerFactory.getLogger(ScadenzeJob.class);

	@Autowired
	private MailDelegateService mailService;

	@Autowired
	private AnagraficheDelegateService anaService;

	@Autowired
	private DaeDelegateService daeDelegateService;

	@Autowired
	private DTOService dtoService;

	SimpleDateFormat format = new SimpleDateFormat("dd/MM");

	/**
	 * Task per l'invio della mail semestrale
	 */
	@Scheduled(cron = "${scadenze.semestrale.cron}")
	@SchedulerLock(name = "invioEmailSemestraleTask")
	public void invioEmailSemestraleTask() {
		try {
			logger.info("Scadenza semestrale job start");
			// controllo che la data attuale sia una di quelle presenti nella
			// configurazione
			String giornoAttuale = format.format(new Date());
			String giorniScadenza = anaService.getParameter(ParametersEnum.DATE_MAIL_ALERT_PERIODICO.name(), "");

			if (giorniScadenza.contains(giornoAttuale)) {
				logger.info("Scadenza semestrale " + giornoAttuale);
				int count = 0;
				List<Dae> daes = daeDelegateService.getAllDAE(DaeDeepDepthRule.NAME);
				for (Dae d : daes) {
					if (d.getResponsabile() != null && !StringUtils.isEmpty(d.getResponsabile().getEmail())) {
						mailService.sendMail(d.getResponsabile().getEmail(), d,
								MailTemplateEnum.SCADENZA_SEMESTRALE_MAIL_TEMPLATE, EntityType.DAE, d.getId());
						count++;
					}
				}
				logger.info("Scadenza semestrale - allertati" + count + " responsabili");
			}
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING invioEmailSemestraleTask", e);
		}
	}

	/**
	 * Task per l'invio della mail semestrale
	 */
	@Scheduled(cron = "${scadenze.manutenzione.cron}")
	@SchedulerLock(name = "invioEmailScadenzaManutenzioneTask")
	public void invioEmailScadenzaManutenzioneTask() {
		try {
			logger.info("Scadenza manutenzione dae job start");

			// cerco i dae in scadenza a due mesi ancora operativi
			DaeFilter daeFilter = new DaeFilter();
			daeFilter.setScadenzaManutenzioneDa(getData(2, 0, 0, 0));
			daeFilter.setScadenzaManutenzioneA(getData(2, 23, 59, 59));
			daeFilter.setFetchRule(DaeDeepDepthRule.NAME);

			// TODO mettere pure batteria

			daeFilter.setTipoManutenzioneList(TipoManutenzioneEnum.MANUTENZIONE_ELETTRODI,
					TipoManutenzioneEnum.MANUTENZIONE_BATTERIE);
			daeFilter.setOperativo(true);

			List<Dae> daes = daeDelegateService.searchDaeByFilter(daeFilter);
			if (daes != null && daes.size() > 0) {
				invioMail(daes, MailTemplateEnum.SCADENZA_MANUTENZIONE_MAIL_TEMPLATE, "2");
				for (Dae d : daes) {
					// d.setOperativo(false);
					// i dae selezionati dovrebbero avere un solo programma
					// mautenzoine filtrato per data
					d.getProgrammiManutenzione().get(0).setMailAlert(ManutenzioneEnum.PRIMA_ALERT);
					daeDelegateService.insertDae(d, null, false);
				}
			}
			logger.info("Scadenza manutenzione - trovati " + daes.size() + " in scadenza 2 mesi");

			// cerco i dae in scadenza a un mese ancora operativi
			daeFilter = new DaeFilter();
			daeFilter.setScadenzaManutenzioneDa(getData(1, 0, 0, 0));
			daeFilter.setScadenzaManutenzioneA(getData(1, 23, 59, 59));
			daeFilter.setFetchRule(DaeDeepDepthRule.NAME);
			// TODO mettere pure batteria

			daeFilter.setTipoManutenzioneList(TipoManutenzioneEnum.MANUTENZIONE_ELETTRODI,
					TipoManutenzioneEnum.MANUTENZIONE_BATTERIE);
			daeFilter.setOperativo(true);

			daes = daeDelegateService.searchDaeByFilter(daeFilter);
			if (daes != null && daes.size() > 0) {
				invioMail(daes, MailTemplateEnum.SCADENZA_MANUTENZIONE_MAIL_TEMPLATE, "1");
				for (Dae d : daes) {
					// d.setOperativo(false);
					// i dae selezionati dovrebbero avere un solo programma
					// mautenzoine filtrato per data
					d.getProgrammiManutenzione().get(0).setMailAlert(ManutenzioneEnum.SECONDA_ALERT);
					daeDelegateService.insertDae(d, null, false);
				}
			}
			logger.info("Scadenza manutenzione - trovati " + daes.size() + " in scadenza 1 mese");
			// cerco i dae scaduti ma ancora operativi
			daeFilter = new DaeFilter();
			daeFilter.setScadenzaManutenzioneDa(getData(-12, 0, 0, 0));
			daeFilter.setScadenzaManutenzioneA(getData(0, 23, 59, 59));
			daeFilter.setFetchRule(DaeDeepDepthRule.NAME);
			// TODO mettere pure batteria
			daeFilter.setTipoManutenzioneList(TipoManutenzioneEnum.MANUTENZIONE_ELETTRODI,
					TipoManutenzioneEnum.MANUTENZIONE_BATTERIE);
			daeFilter.setOperativo(true);

			daes = daeDelegateService.searchDaeByFilter(daeFilter);

			if (daes != null && daes.size() > 0) {
				// invio la mail
				invioMail(daes, MailTemplateEnum.MANUTENZIONE_SCADUTA_MAIL_TEMPLATE, "");
				// disattivo il dae
				for (Dae d : daes) {
					d.setOperativo(false);
					// i dae selezionati dovrebbero avere un solo programma
					// mautenzoine filtrato per data
					d.getProgrammiManutenzione().get(0).setMailAlert(ManutenzioneEnum.SCADUTA);
					daeDelegateService.insertDae(d, null, false);
				}
			}
			logger.info("Scadenza manutenzione - trovati " + daes.size() + " scaduti");
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING invioEmailScadenzaManutenzioneTask", e);
		}

	}

	private void invioMail(List<Dae> daes, MailTemplateEnum template, String mesi) {
		for (Dae d : daes) {
			if (d.getResponsabile() != null && !StringUtils.isEmpty(d.getResponsabile().getEmail())) {
				mailService.sendMail(d.getResponsabile().getEmail(), d, template, EntityType.DAE, d.getId());
			}
			// effettuo il clone dell'oggetto
			Dae respDAE = dtoService.convertObject(d, Dae.class);
			respDAE.getResponsabile().setCognome(null);
			respDAE.getResponsabile().setNome("Responsabile di Regione");
			// invio la mail pure al responsabile regoinale
			String email = anaService.getParameter(ParametersEnum.REGION_MASTER_EMAIL.name());
			mailService.sendMail(email, respDAE, template, EntityType.DAE, respDAE.getId());
		}
	}

	/**
	 * Crea un oggetto Calendar sommando i mesi passati come parametro e
	 * sostituendo le ore, minuti e secondi
	 * 
	 * @param mesi
	 *            da sommare
	 * @param ore
	 *            da sostituire
	 * @param minuti
	 *            da sostituire
	 * @param secondi
	 *            da sostituire
	 * @return
	 */
	private Date getData(int mesi, int ore, int minuti, int secondi) {
		Calendar c = Calendar.getInstance();

		c.add(Calendar.MONTH, mesi);

		c.set(Calendar.HOUR_OF_DAY, ore);
		c.set(Calendar.MINUTE, minuti);
		c.set(Calendar.SECOND, secondi);

		return c.getTime();
	}

}
