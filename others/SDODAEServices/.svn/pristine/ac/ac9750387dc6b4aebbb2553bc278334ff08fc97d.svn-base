/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.persistence;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.areas.ems.sdodaeservices.dao.impl.rule.EventDetailDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.InterventoForEventDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.EventDO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.EventFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.InterventoFilterDO;
import it.eng.areas.ems.sdodaeservices.service.EventTransactionalService;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EventTestCtx.class)
public class EventTest {

	@Autowired
	private EventTransactionalService eventService;

	@Test
	public final void getAllActiveEvents() {
		List<EventDO> events = eventService.getAllActiveEvents("8af6a0bd58ce7fb50158cedd7a060017");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		events.forEach(e -> {
			System.out.println(e.getCartellino());
		});
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		InterventoFilterDO filter = new InterventoFilterDO();
		filter.setNotClosed(true);
		filter.setFetchRule(InterventoForEventDepthRule.NAME);
		filter.setFirstResponder("8af6a0bd58ce7fb50158cedd7a060017");
		List<InterventoDO> interv = eventService.searchInterventionByFilter(filter);

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		interv.forEach(e -> {
			System.out.println(e.getId());
		});
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

	}

	@Test
	public final void getNotifiedEvents() {
		List<EventDO> events = eventService.getNotifiedEvents("8af6a0bd58ce7fb50158cedd7a060017");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		events.forEach(e -> {
			System.out.println(e.getCartellino());
		});
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}

	@Test
	public final void searchEvent() {

		EventFilterDO filterDO = new EventFilterDO(EventDetailDepthRule.NAME);
		Calendar dataDA = Calendar.getInstance();
		dataDA.set(Calendar.DAY_OF_MONTH, 1);
		dataDA.set(Calendar.MONTH, 11);
		dataDA.set(Calendar.YEAR, 2017);
		filterDO.setDataDA(dataDA);
		List<EventDO> events = eventService.searchEventByFilter(filterDO);

		events.forEach(e -> {
			System.out.println(e.getCartellino());
		});

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		filterDO.setManaged(false);
		filterDO.setAccepted(true);
		events = eventService.searchEventByFilter(filterDO);

		events.forEach(e -> {
			System.out.println(e.getCartellino());
		});

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		filterDO.setManaged(true);
		events = eventService.searchEventByFilter(filterDO);

		events.forEach(e -> {
			System.out.println(e.getCartellino());
		});

	}

	@Test
	public final void countNotified() {

		long num = eventService.countNotified("8af6a0bd5a3c6638015a3d20a2ec0034");
		System.out.println(num);

	}
}
