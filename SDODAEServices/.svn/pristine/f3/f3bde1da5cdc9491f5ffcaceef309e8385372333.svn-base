/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.persistence.graph;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.areas.ems.sdodaeservices.bean.DaeEventActivation;
import it.eng.areas.ems.sdodaeservices.bean.DaeSubscription;
import it.eng.areas.ems.sdodaeservices.bean.FirstResponderSubscription;
import it.eng.areas.ems.sdodaeservices.service.GraphTransactionalService;

/**
 * @author Bifulco Luigi
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GraphServiceTestContext.class)
public class GraphServicePersistenceTest {

	@Autowired
	private GraphTransactionalService service;

	@Test
	public final void shouldListSubscriptionsPerMonth() {
		Calendar from = GregorianCalendar.getInstance();
		from.set(GregorianCalendar.DAY_OF_MONTH, 1);
		from.set(GregorianCalendar.HOUR_OF_DAY, 0);
		from.set(GregorianCalendar.MINUTE, 0);
		from.set(GregorianCalendar.SECOND, 0);

		List<FirstResponderSubscription> ret = service.countFrSubscription(from, GregorianCalendar.getInstance());
		ret.forEach((k) -> System.out.println(k));
	}

	@Test
	public final void shouldListDAESubscription() {
		Calendar from = GregorianCalendar.getInstance();
		from.set(GregorianCalendar.DAY_OF_MONTH, 1);
		from.set(GregorianCalendar.HOUR_OF_DAY, 0);
		from.set(GregorianCalendar.MINUTE, 0);
		from.set(GregorianCalendar.SECOND, 0);

		List<DaeSubscription> ret = service.countDaeSubscription(from, GregorianCalendar.getInstance());
		ret.forEach((k) -> System.out.println(k));
	}

	@Test
	public final void shouldListDAEActivation() {

		Calendar from = GregorianCalendar.getInstance();
		from.set(GregorianCalendar.DAY_OF_MONTH, 1);
		from.set(GregorianCalendar.HOUR_OF_DAY, 0);
		from.set(GregorianCalendar.MINUTE, 0);
		from.set(GregorianCalendar.SECOND, 0);

		List<DaeEventActivation> ret = service.countDaeActivationByProvince(null, from,
				GregorianCalendar.getInstance());
		ret.forEach((k) -> System.out.println(k));
	}

	@Test
	public final void listFRAcceptedByType() {

		Calendar from = GregorianCalendar.getInstance();

		from.set(GregorianCalendar.DAY_OF_MONTH, 1);
		from.set(GregorianCalendar.HOUR_OF_DAY, 0);
		from.set(GregorianCalendar.MINUTE, 0);
		from.set(GregorianCalendar.SECOND, 0);

		List<DaeEventActivation> ret = service.listFRAcceptedByType("FIRST_RESPONDER", from,
				GregorianCalendar.getInstance());
		ret.forEach(System.out::println);

		ret = service.listFRAcceptedByType("2", from, GregorianCalendar.getInstance());
		ret.forEach(System.out::println);
	}

	@Test
	public final void listFRActivationsByType() {

		Calendar from = GregorianCalendar.getInstance();
		from.set(GregorianCalendar.DAY_OF_MONTH, 1);
		from.set(GregorianCalendar.HOUR_OF_DAY, 0);
		from.set(GregorianCalendar.MINUTE, 0);
		from.set(GregorianCalendar.SECOND, 0);

		List<DaeEventActivation> ret = service.listFRActivationsByType("FIRST_RESPONDER", from,
				GregorianCalendar.getInstance());
		ret.forEach(System.out::println);

		ret = service.listFRActivationsByType("2", from, GregorianCalendar.getInstance());
		ret.forEach(System.out::println);
	}

}
