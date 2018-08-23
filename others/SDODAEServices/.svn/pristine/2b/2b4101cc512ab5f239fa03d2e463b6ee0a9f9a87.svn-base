package it.eng.areas.ems.sdodaeservices.test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.area118.sdocommon.dao.filter.Paging;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.EventDetailDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.EventDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.PushNotificationService;
import it.eng.areas.ems.sdodaeservices.delegate.model.CategoriaFr;
import it.eng.areas.ems.sdodaeservices.delegate.model.Comune;
import it.eng.areas.ems.sdodaeservices.delegate.model.Event;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.GPSLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.Intervento;
import it.eng.areas.ems.sdodaeservices.delegate.model.Provincia;
import it.eng.areas.ems.sdodaeservices.delegate.model.Strade;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.EventFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.StradeFilter;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTEventDO;
import it.eng.areas.ems.sdodaeservices.service.EventTransactionalService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { EventDelegateTestCtx.class })
@ActiveProfiles(profiles = "oracle")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventDelegateTest extends AbstractJUnit4SpringContextTests {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	String lastID = "";

	@Autowired
	protected EventDelegateService eventDelegateService;

	@Autowired
	protected AnagraficheDelegateService anagraficaDelegateService;

	@Autowired
	private EventTransactionalService service;

	@Autowired
	private PushNotificationService pushService;

	@Autowired
	protected FirstResponderDelegateService firstResponderDelegateService;

	@Test
	public void selectEvent() {

		EventFilter filter = new EventFilter();
		filter.setFetchRule(EventDetailDepthRule.NAME);
		filter.setId("8af6a0bd5e7bd620015e9f818af000a8");
		List<Event> events = eventDelegateService.searchEventByFilter(filter);

		events.forEach(System.out::println);
		System.out.println(events.size());
	}

	@Test
	public void testMyEvent() {
		FirstResponder fr = new FirstResponder();
		fr.setId("8af6a0bd5982d29b015988a4e1180038");

		GPSLocation lastPosition = new GPSLocation();
		lastPosition.setLatitudine(40.84615f);
		lastPosition.setLongitudine(14.287256f);

		fr.setLastPosition(lastPosition);

		List<Event> events = eventDelegateService.getAvailableEventsForFirstResponder(fr);

		System.out.println(events.size());
	}

	@Test
	public void listEvent() {
		EventFilter filter = new EventFilter();

		filter.setFrID("8af6a0bd58ce7fb50158cedd7a060017");
		filter.setFetchRule("APP");
		filter.setAccepted(true);

		// filter.setMaxResult(10);
		filter.setPaging(new Paging(0, 10));

		List<Event> events = eventDelegateService.searchEventByFilter(filter);
		System.out.println(events);

		System.out.println(events.size());
	}

	@Test
	public void AtestCrudEvent() {

		Event event = createEvent();

		Event returnedEvent = eventDelegateService.insertEvent(event);

		List<FirstResponder> frs = eventDelegateService.alertFirstResponderForNewEvent(returnedEvent);

		Assert.assertNotNull(frs);

		Assert.assertTrue(frs.size() > 0);

		// pushService.notifyToFirstResponder(event, frs.get(0));

		lastID = returnedEvent.getId();
		Assert.assertNotNull(lastID);

		String id = lastID;
		event = eventDelegateService.getEventById(id);

		Assert.assertNotNull(event);

		boolean del = eventDelegateService.deleteEventById(id);
		Assert.assertTrue(del);

	}

	public void testNotifyPushWoosh() {

	}

	@Test
	public void BgetAllEventi() {

		List<Event> eventList = eventDelegateService.getAllEvent();

		Assert.assertNotNull(eventList);
		Assert.assertTrue(!eventList.isEmpty());

	}

	@Test
	public void CtestCrudIntervento() {

		Event returnedEvent = eventDelegateService.insertEvent(createEvent());
		Intervento intervento = createIntervento(returnedEvent.getId());

		Intervento returnedIntervento = eventDelegateService.insertIntervento(intervento);

		lastID = returnedIntervento.getId();
		Assert.assertNotNull(lastID);

		String id = lastID;
		intervento = eventDelegateService.getInterventoById(id);

		Assert.assertNotNull(intervento);

		boolean del = eventDelegateService.deleteInterventoById(id);
		Assert.assertTrue(del);

		boolean del2 = eventDelegateService.deleteEventById(returnedEvent.getId());
		Assert.assertTrue(del2);

	}

	@Test
	public void DgetAllInterventi() {

		Event returnedEvent = eventDelegateService.insertEvent(createEvent());
		Intervento intervento = createIntervento(returnedEvent.getId());

		Intervento returnedIntervento = eventDelegateService.insertIntervento(intervento);

		List<Intervento> interventoList = eventDelegateService.getAllIntervento();

		Assert.assertNotNull(interventoList);
		Assert.assertTrue(!interventoList.isEmpty());

		boolean del = eventDelegateService.deleteInterventoById(returnedIntervento.getId());
		Assert.assertTrue(del);

		boolean del2 = eventDelegateService.deleteEventById(returnedEvent.getId());
		Assert.assertTrue(del2);

	}

	@Test
	public void EgetInterventiByFirstResponder() {

		Event returnedEvent = eventDelegateService.insertEvent(createEvent());

		Intervento intervento = createIntervento(returnedEvent.getId());
		Intervento returnedIntervento = eventDelegateService.insertIntervento(intervento);

		FirstResponder firstResponder = firstResponderDelegateService
				.getFirstResponderById(FirstResponderDeepDepthRule.NAME, "prova");

		List<Intervento> interventoList = eventDelegateService.getInterventiByFirstResponder(firstResponder);

		Assert.assertNotNull(interventoList);
		Assert.assertTrue(!interventoList.isEmpty());

		boolean del = eventDelegateService.deleteInterventoById(returnedIntervento.getId());
		Assert.assertTrue(del);

		boolean del2 = eventDelegateService.deleteEventById(returnedEvent.getId());
		Assert.assertTrue(del2);

	}

	/*
	 * @Ignore public void CgetAllVCTDAE() {
	 * 
	 * List<VCTEventDO> eventList = eventDelegateService.getAllVctEvent();
	 * 
	 * Assert.assertNotNull(eventList); Assert.assertTrue(!eventList.isEmpty());
	 * 
	 * }
	 * 
	 */

	private Event createEvent() {

		Event event = new Event();

		event.setInfo("Evento Codice blu");
		event.setNote("Il paziente si trova sul selciato");
		event.setRiferimento("Riferimento 1");
		event.setTelefono("3733248924303");
		event.setTempoArrivoAmbulanza(44);
		event.setTimestamp(getCalendar(2016, 10, 10, 10, 10, 10));
		event.setCoordinate(createLuogo());
		event.setIndirizzo("VIA ROMA");
		event.setCivico("32");
		event.setComune("BOLOGNA");
		List<CategoriaFr> frsCats = anagraficaDelegateService.getAllCategoriaFR();
		event.setCategoria(frsCats.get(0));
		return event;
	}

	private GPSLocation createLuogo() {
		// TODO Auto-generated method stub
		GPSLocation gpsLocation = new GPSLocation();

		gpsLocation.setComune(createComune());
		gpsLocation.setCivico("22A");
		gpsLocation.setError(1.2f);
		gpsLocation.setLatitudine(44.4938100f);
		gpsLocation.setAltitudine(50f);
		gpsLocation.setLongitudine(11.3387500f);
		gpsLocation.setTimeStamp(getCalendar(2016, 12, 12, 12, 12, 12).getTime());

		gpsLocation.setLuogoPubblico("Scuola Media Fazello");
		gpsLocation.setTipoLuogoPubblico("Scuola");

		return gpsLocation;

	}

	private Strade getStrade() {

		StradeFilter stradeFilter = new StradeFilter();

		/*
		 * AnagraficheDelegateTest anagraficheDelegateTest = new
		 * AnagraficheDelegateTest();
		 * appContext.getAutowireCapableBeanFactory().autowireBean(
		 * anagraficheDelegateTest); ;
		 * 
		 * anagraficheDelegateTest.DgetStrdeByFilter();
		 */

		stradeFilter.setCodiceIstatComune("040036");
		stradeFilter.setName("Nazionale");

		List<Strade> stradeList = anagraficaDelegateService.searchStradeByFilter(stradeFilter);

		return stradeList.get(0);
	}

	private Set<Event> createEventi() {

		Event event = new Event();
		Set<Event> eventi = new HashSet<Event>();
		eventi.add(event);
		return eventi;
	}

	private Comune createComune() {
		Comune comune = new Comune();
		comune.setId("BO_MUN_094023");
		comune.setCodiceIstat("094023");
		comune.setNomeComune("ISERNIA");
		Provincia prov = new Provincia();
		prov.setId("BO_PROV_094");
		prov.setNomeProvincia("IS");
		comune.setProvincia(prov);
		return comune;
	}

	private Calendar getCalendar(int year, int month, int day, int hour, int min, int sec) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, sec);

		return c;
	}

	private Intervento createIntervento(String eventId) {

		Intervento intervervento = new Intervento();
		intervervento.setAccettatoDa("operatore 1");
		intervervento.setDataAccettazione(new Date());
		intervervento.setDataCreazione(new Date());
		intervervento.setDurata(20);
		Event event = eventDelegateService.getEventById(eventId);
		intervervento.setEvent(event);
		FirstResponder firstResponder = firstResponderDelegateService
				.getFirstResponderById(FirstResponderDeepDepthRule.NAME, "prova");
		intervervento.setEseguitoDa(firstResponder);
		return intervervento;

	}

}
