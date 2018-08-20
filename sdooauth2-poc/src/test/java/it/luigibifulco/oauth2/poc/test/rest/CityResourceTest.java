package it.luigibifulco.oauth2.poc.test.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.luigibifulco.oauth2.poc.jpa.service.CityRepository;
import it.luigibifulco.oauth2.poc.jpa.service.ICityService;
import it.luigibifulco.oauth2.poc.jpa.service.TransactionalService;
import it.luigibifulco.oauth2.poc.server.controller.CityResource;

@RunWith(SpringRunner.class)
@WebMvcTest(CityResource.class)
public class CityResourceTest {

	@MockBean
	private ICityService service;

	@MockBean
	private CityRepository mockrepo;

	@Autowired
	private MockMvc mockMvc;

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

	@Test
	public void startsWithTest() throws Exception {
		when(service.startsWith("")).thenReturn(null);
		this.mockMvc.perform(get("/prod/cities/startsWith/TEST")).andDo(print()).andExpect(status().is(401));
	}

}
