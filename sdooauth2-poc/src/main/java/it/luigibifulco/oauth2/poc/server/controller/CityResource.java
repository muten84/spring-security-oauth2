package it.luigibifulco.oauth2.poc.server.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import it.luigibifulco.oauth2.poc.jpa.domain.City;
import it.luigibifulco.oauth2.poc.jpa.domain.excpetion.ResourceNotFoundException;
import it.luigibifulco.oauth2.poc.jpa.service.CityRepository;
import it.luigibifulco.oauth2.poc.jpa.service.ICityService;

@RestController
@RequestMapping(path = "cities", produces = "application/it.luigibifulco.oauth2.app-v1+json", consumes = "application/it.luigibifulco.oauth2.app-v1+json")
public class CityResource {

	@Autowired
	@Qualifier("transactionalService")
	private ICityService cityService;

	@Autowired
	private CityRepository repository;

	@GetMapping(path = "/startsWith/{startsWith}")
	@ApiOperation(value = "create an entity", notes = "Requires an entity id to lookup", response = City.class, authorizations = {
			@Authorization(value = "oauth2", scopes = {
					@AuthorizationScope(scope = "read", description = "Read access on entity in my new API") }) })
	@ApiResponses({ @ApiResponse(code = 404, message = "Entity not found") })
	public Iterable<City> startsWith(@PathVariable("startsWith") String startsWith) {
		return this.cityService.startsWith(startsWith);

	}

	@GetMapping(path = "/{id}")
	public City getCityById(@PathVariable(name = "id") Long id) {
		Optional<City> city = this.repository.findById(id);
		if (!city.isPresent()) {
			throw new ResourceNotFoundException(City.class.toString(),
					"Resource with id " + "id" + "was not found on this db");
		}
		return city.get();
	}

	@PostMapping()
	@ApiOperation(value = "create an entity", notes = "Requires an entity id to lookup", response = City.class, authorizations = {
			@Authorization(value = "oauth2", scopes = {
					@AuthorizationScope(scope = "create", description = "Read access on entity in my new API") }) })
	@ApiResponses({ @ApiResponse(code = 404, message = "Entity not found") })
	public @ResponseBody ResponseEntity<Object> createCity(@RequestBody City city) {
		City saved = this.cityService.createCity(city);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saved.getId())
				.toUri();

		return ResponseEntity.created(location).build();

	}

}
