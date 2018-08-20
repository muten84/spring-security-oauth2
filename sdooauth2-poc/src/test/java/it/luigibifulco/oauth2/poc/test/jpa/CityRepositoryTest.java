package it.luigibifulco.oauth2.poc.test.jpa;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.luigibifulco.oauth2.poc.jpa.domain.City;
import it.luigibifulco.oauth2.poc.jpa.service.CityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MyTestConfiguration.class })
@DataJpaTest
public class CityRepositoryTest {

	@Autowired
	private CityRepository sut;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public final void testFindOne() {
		City mockCity = new City();
		mockCity.setName("TEST");
		mockCity.setCountry("COUNYT");
		mockCity.setMap("map");
		mockCity.setState("state");
		entityManager.persistAndFlush(mockCity);
		List<City> cities = this.sut.findAll();
		Assert.assertTrue(!cities.isEmpty());
		Assert.assertTrue(cities.size() == 1);
	}

	@Test
	public final void testFindAll() {
		City mockCity = new City();
		mockCity.setName("TEST2");
		mockCity.setCountry("COUNYT2");
		mockCity.setMap("map2");
		mockCity.setState("state2");
		entityManager.persistAndFlush(mockCity);
		List<City> cities = this.sut.findAll();
		Assert.assertTrue(!cities.isEmpty());
		Assert.assertTrue(cities.size() == 1);
	}

}
