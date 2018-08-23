package it.luigibifulco.microservice.cloud.counterservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/counter-service")
public class CounterService {

	@Autowired
	private TransactionalCounterService service;

	@PostMapping("/newRequest")
	public CounterBean newRequest() {
		return this.service.increment();
	}

	@GetMapping("/lastCount")
	public CounterBean lastCount() {
		return this.service.lastCount();
	}

}
