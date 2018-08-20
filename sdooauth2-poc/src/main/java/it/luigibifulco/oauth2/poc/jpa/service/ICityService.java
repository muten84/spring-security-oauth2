package it.luigibifulco.oauth2.poc.jpa.service;

import org.springframework.stereotype.Service;

import it.luigibifulco.oauth2.poc.jpa.domain.City;

@Service
public interface ICityService {

	Iterable<City> startsWith(String startsWith);

	City createCity(City city);

}
