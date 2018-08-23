package it.eng.areas.ems.sdodaeservices.delegate.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.sdodaeservices.service.FirstResponderTransactionalService;
import net.javacrumbs.shedlock.core.SchedulerLock;

@Component
public class DoNotDisturbJob {

	private Logger logger = LoggerFactory.getLogger(DoNotDisturbJob.class);

	@Autowired
	private FirstResponderTransactionalService frTransactional;

	private DateFormat format = new SimpleDateFormat("HH:mm");

	@Scheduled(cron = "0 * * * * ?")
	@SchedulerLock(name = "doNotDisturbJob")
	public void executeDoNotDisturb() {
		String hour = format.format(new Date());
		logger.info("DoNotDisturbJob execute " + hour);

		frTransactional.updateDoNotDisturbStart(hour);

		frTransactional.updateDoNotDisturbEnd(hour);
	}

	@Scheduled(cron = "0 * * * * ?")
	@SchedulerLock(name = "silentJob")
	public void executeSilent() {
		String hour = format.format(new Date());
		logger.info("silentJob execute " + hour);

		frTransactional.updateSilentEnd(hour);
	}
}
