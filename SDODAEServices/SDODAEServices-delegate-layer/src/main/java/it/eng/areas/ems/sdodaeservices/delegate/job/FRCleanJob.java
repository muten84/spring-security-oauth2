package it.eng.areas.ems.sdodaeservices.delegate.job;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderImageRule;
import it.eng.areas.ems.sdodaeservices.entity.FRStatoProfiloEnum;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.entity.filter.FirstResponderFilterDO;
import it.eng.areas.ems.sdodaeservices.service.AnagraficheTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.FirstResponderTransactionalService;
import net.javacrumbs.shedlock.core.SchedulerLock;

/**
 * Job per la pulizia dei FR registrati ma mai attivati,
 * 
 * @author Miranda Mauro
 *
 */
@Component
public class FRCleanJob {

	@Autowired
	private FirstResponderTransactionalService frTransactional;

	@Autowired
	private AnagraficheTransactionalService anaService;

	private Logger logger = LoggerFactory.getLogger(FRCleanJob.class);

	/**
	 * Il job viene eseguito ogni ora
	 */
	@Scheduled(cron = "0 0 * * * ?")
	@SchedulerLock(name = "frCleanJob")
	public void executeFrClean() {
		logger.info("FRCleanJob - execute");
		FirstResponderFilterDO frFilter = new FirstResponderFilterDO();

		int hour = anaService.getParametro(ParametersEnum.HOUR_DELETE_FR.name(), 72);

		Date data24h = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(hour));

		frFilter.setDeleted(false);
		frFilter.setDataIscrizioneMax(data24h);
		frFilter.setStatoProfilo(FRStatoProfiloEnum.IN_ATTESA_DI_ATTIVAZIONE);
		frFilter.setFetchRule(FirstResponderImageRule.NAME);
		// frFilter.set
		List<FirstResponderDO> toClean = frTransactional.searchFirstResponderByFilter(frFilter);

		if (toClean != null && !toClean.isEmpty()) {
			logger.info("FRCleanJob - found " + toClean.size() + " fr to delete");
			// cancello tutti gli utenti creati
			toClean.forEach(fr -> {
				logger.info("FRCleanJob - deleting " + fr.getUtente().getEmail());
				frTransactional.deleteLogicallyFR(fr.getId(), null);
			});
		}

	}
}
