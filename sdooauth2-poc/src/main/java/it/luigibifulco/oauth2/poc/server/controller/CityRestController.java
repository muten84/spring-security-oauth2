package it.luigibifulco.oauth2.poc.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.luigibifulco.oauth2.poc.jpa.domain.City;
import it.luigibifulco.oauth2.poc.jpa.service.ICityService;

@RestController
public class CityRestController {

	@Autowired
	@Qualifier("transactionalService")
	private ICityService cityService;

	@RequestMapping("/findAll/{startsWith}")
	public Iterable<City> startsWith(@PathVariable("startsWith") String startsWith) {
		return this.startsWith(startsWith);

	}

}
