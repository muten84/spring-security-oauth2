package it.luigibifulco.oauth2.poc.jpa.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import it.luigibifulco.oauth2.poc.jpa.domain.City;

public interface CityRepository extends JpaRepository<City, Long>, QuerydslPredicateExecutor<City> {

}
