package it.eng.areas.ems.sdodaeservices.test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.areas.ems.sdodaeservices.delegate.GraphServiceDelegate;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeNumbersDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { GraphTestCtx.class })
@ActiveProfiles(profiles = "oracle")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GraphTest {

	@Autowired
	private GraphServiceDelegate graphServiceDelegate;

	@Test
	public void countDae() {

		Calendar fromCal = GregorianCalendar.getInstance();
		fromCal.setTimeInMillis(0);
		Calendar toCal = GregorianCalendar.getInstance();

		List<DaeNumbersDTO> res = graphServiceDelegate.fetchFrNumbersByCategory(fromCal, toCal);

		res.forEach(System.out::println);
	}

}
