package it.eng.areas.ems.sdodaeservices.rest;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import it.eng.areas.ems.sdodaeservices.rest.service.app.DAEServiceREST;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DaeTestCTX.class })
@ActiveProfiles(profiles = "oracle")
@WebAppConfiguration
public class DaeTest {

	@Autowired
	protected DAEServiceREST daeRest;

	public void getAllDae() {

	}

}
