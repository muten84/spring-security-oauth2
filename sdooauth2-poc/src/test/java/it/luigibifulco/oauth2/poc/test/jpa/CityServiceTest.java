package it.luigibifulco.oauth2.poc.test.jpa;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.core.types.dsl.BooleanExpression;

import it.luigibifulco.oauth2.poc.jpa.domain.City;
import it.luigibifulco.oauth2.poc.jpa.domain.QCity;
import it.luigibifulco.oauth2.poc.jpa.service.CityRepository;
import it.luigibifulco.oauth2.poc.jpa.service.ICityService;
import it.luigibifulco.oauth2.poc.jpa.service.TransactionalService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MyTestConfiguration.class })
@DataJpaTest
public class CityServiceTest {

	/*
	 * questa classe serve per istanzaire tutti i bean che servono al test contest e
	 * che non si vuole dichiare nel contesto definitivo ad esempio potrei voler
	 * dichiarer dei bean solamente nel test e non nella spring boot app
	 */
	@TestConfiguration
	static class ContextTestContextConfiguration {

		@Bean
		public ICityService transactionalService() {
			return new TransactionalService();
		}
	}

	@Autowired
	// @Qualifier("transactionalService")
	private ICityService transactionalService;

	@MockBean
	private CityRepository repository;

	@Before
	public void init() {
		String start = "test";
		QCity qcity = QCity.city;

		BooleanExpression startsWith = qcity.name.startsWith(start);

		City city = new City();
		city.setName("TEST");

		List<City> cities = Arrays.asList(new City[] { city });

		boolean ok = startsWith.stringValue().toString().equals("str(startsWith(city.name,test))");
		Mockito.when(repository.findAll(startsWith)).thenReturn(cities);
	}

	@Test
	public final void testFindAllStartingWith() {
		Iterable<City> cities = this.transactionalService.startsWith("test");
		Assert.assertTrue(cities.iterator().next() != null);
	}

}
