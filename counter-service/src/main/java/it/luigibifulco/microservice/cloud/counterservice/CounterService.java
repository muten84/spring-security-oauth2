package it.luigibifulco.microservice.cloud.counterservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/counter-service")
public class CounterService {

	private static Logger LOGGER = LoggerFactory.getLogger(CounterService.class);

	@Autowired
	private TransactionalCounterService service;

	@PostMapping("/newRequest")
	public CounterBean newRequest() {
		LOGGER.info("executing new request");
		return this.service.increment();
	}

	@GetMapping("/lastCount")
	public CounterBean lastCount() {
		return this.service.lastCount();
	}

}
