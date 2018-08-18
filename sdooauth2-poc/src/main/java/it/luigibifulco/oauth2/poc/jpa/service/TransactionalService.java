package it.luigibifulco.oauth2.poc.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.BooleanExpression;

import it.luigibifulco.oauth2.poc.jpa.domain.City;
import it.luigibifulco.oauth2.poc.jpa.domain.QCity;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
@Service
@Component("transactionalService")
public class TransactionalService implements ICityService {

	@Autowired
	private CityRepository cityRepository;

	@Override
	public Iterable<City> startsWith(String start) {
		QCity city = QCity.city;
		BooleanExpression startsWith = city.name.startsWith(start);
		return cityRepository.findAll(startsWith);
	}

	@Override
	public City createCity(City city) {
		return this.cityRepository.save(city);
	}

}
