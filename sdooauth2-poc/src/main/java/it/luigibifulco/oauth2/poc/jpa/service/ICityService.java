package it.luigibifulco.oauth2.poc.jpa.service;

import it.luigibifulco.oauth2.poc.jpa.domain.City;

public interface ICityService {

	Iterable<City> startsWith(String startsWith);

}
