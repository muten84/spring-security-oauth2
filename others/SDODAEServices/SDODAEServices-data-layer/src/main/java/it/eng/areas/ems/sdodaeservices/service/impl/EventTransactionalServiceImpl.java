package it.eng.areas.ems.sdodaeservices.service.impl;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;
import it.eng.areas.ems.sdodaeservices.dao.EventDAO;
import it.eng.areas.ems.sdodaeservices.dao.InterventoCoordDAO;
import it.eng.areas.ems.sdodaeservices.dao.InterventoDAO;
import it.eng.areas.ems.sdodaeservices.dao.NotificheEventoDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.EventAppDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.EventDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.EventDetailDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.InterventoDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.NotificheEventoDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.EventDO;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoCoordDO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoDO;
import it.eng.areas.ems.sdodaeservices.entity.NotificheEventoDO;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.entity.filter.EventFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.InterventoFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.NotificheEventoFilterDO;
import it.eng.areas.ems.sdodaeservices.service.AnagraficheTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.EventTransactionalService;

@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EventTransactionalServiceImpl implements EventTransactionalService {

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private InterventoDAO interventoDAO;

	@Autowired
	private InterventoCoordDAO interventoCoordDAO;

	@Autowired
	private NotificheEventoDAO notificheEventoDAO;

	@Autowired
	private AnagraficheTransactionalService anaService;

	private SimpleDateFormat sdf;

	public EventTransactionalServiceImpl() {

	}

	@PostConstruct
	public void init() {
		// TODO: all dependendecies injected, init your stateless service here
		// TODO: tutte le dipendenze sono state inettate inizializza qui il tuo
		// stateless service
		sdf = new SimpleDateFormat("dd/MM/yyyy");
	}

	@Override
	public EventDO getEventById(String id) {

		return getEventById(EventDeepDepthRule.NAME, id);
	}

	@Override
	public EventDO getEventById(String fetch, String id) {

		return eventDAO.get(fetch, id);
	}

	public long countAll() {
		long num = eventDAO.countAll();

		// if (example == null) {
		// throw new NullPointerException("The entity does not exist");
		// }
		return num;
	}

	@Override
	public List<EventDO> getAllEvent() {
		// TODO Auto-generated method stub
		EntityFilter filter = new EntityFilter();
		filter.setFetchRule(EventDeepDepthRule.NAME);

		return eventDAO.getAll(filter);
	}

	@Override
	public List<EventDO> getAllActiveEvents(String frID) {
		Integer maxInt = Integer
				.valueOf(anaService.getParametro(ParametersEnum.MAX_ACTIVED_FIRST_RESPONDER.name(), "5"));
		List<EventDO> evts = eventDAO.getAvailableEvents(frID, maxInt, EventDeepDepthRule.NAME);

		return evts;
	}

	@Override
	public List<EventDO> getEventByFilter(EventFilterDO eventFilter) {

		eventFilter.setFetchRule(EventDeepDepthRule.NAME);

		return eventDAO.searchEventByFilter(eventFilter);

	}

	@Override
	public EventDO saveEvent(EventDO eventDO) {

		if (StringUtils.isEmpty(eventDO.getData())) {
			eventDO.setData(sdf.format(eventDO.getTimestamp().getTime()));
		}
		if (!StringUtils.isEmpty(eventDO.getId())) {
			// se è un aggiornamento , prendo la vacchia categoria dal db così
			// evito di sovrascriverla
			EventDO old = eventDAO.get(eventDO.getId());
			if (old != null) {
				eventDO.setCategoriaFr(old.getCategoriaFr());
			}
		}

		return eventDAO.insertEvent(eventDO);
	}

	// @Override
	// public List<VCTEventDO> getAllVctEvent() {
	// return vctEventDAO.getAllVCTDAE();
	// }

	@Override
	public boolean deleteEventById(String id) {
		EventDO eventDO = getEventById(id);
		if (eventDO != null) {
			eventDAO.delete(eventDO);
			return true;
		}
		return false;
	}

	@Override
	public InterventoDO insertIntervento(InterventoDO interventoDO) {

		InterventoDO returnedinterventoDO = interventoDAO.insertIntervento(interventoDO);

		return returnedinterventoDO;

	}

	@Override
	public InterventoDO getInterventoById(String id) {
		return interventoDAO.get(InterventoDeepDepthRule.NAME, id);

	}

	@Override
	public boolean deleteInterventoById(String id) {
		InterventoDO interventoDO = getInterventoById(id);
		if (interventoDO != null) {
			interventoDAO.delete(interventoDO);
			return true;
		}
		return false;
	}

	@Override
	public List<InterventoDO> getAllIntervento() {
		EntityFilter filter = new EntityFilter();
		filter.setFetchRule(InterventoDeepDepthRule.NAME);

		return interventoDAO.getAll(filter);
	}

	@Override
	public List<InterventoDO> getInterventiByFirstResponder(FirstResponderDO firstResponder) {

		InterventoFilterDO filt = new InterventoFilterDO();
		filt.setFirstResponder(firstResponder.getId());
		filt.setFetchRule(InterventoDeepDepthRule.NAME);
		return interventoDAO.searchInterventoByFilter(filt);

	}

	@Override
	public List<InterventoDO> getInterventiAttiviByFirstResponder(FirstResponderDO firstResponder) {

		InterventoFilterDO filt = new InterventoFilterDO();
		filt.setFirstResponder(firstResponder.getId());
		return interventoDAO.searchInterventoByFilter(filt);

	}

	@Override
	public NotificheEventoDO insertNotificheEvento(NotificheEventoDO notificheEventoDO) {

		return notificheEventoDAO.insertNotificheEvento(notificheEventoDO);
	}

	@Override
	public NotificheEventoDO getNotificheEventoById(String id) {
		return notificheEventoDAO.get(NotificheEventoDeepDepthRule.NAME, id);
	}

	@Override
	public List<NotificheEventoDO> getAllNotificheEvento() {
		EntityFilter filter = new EntityFilter();
		filter.setFetchRule(NotificheEventoDeepDepthRule.NAME);

		return notificheEventoDAO.getAll(filter);
	}

	@Override
	public List<NotificheEventoDO> getNotificheEventiByEventId(String eventId) {

		NotificheEventoFilterDO filter = new NotificheEventoFilterDO(NotificheEventoDeepDepthRule.NAME);
		filter.setEvent(eventId);
		return notificheEventoDAO.getAll(filter);
	}

	@Override
	public List<InterventoDO> searchInterventionByFilter(InterventoFilterDO filter) {
		return interventoDAO.searchInterventoByFilter(filter);
	}

	@Override
	public List<NotificheEventoDO> getNotificheByEventId(String eventID) {
		NotificheEventoFilterDO filter = new NotificheEventoFilterDO();
		filter.setEvent(eventID);
		filter.setFetchRule(NotificheEventoDeepDepthRule.NAME);
		List<NotificheEventoDO> nots = notificheEventoDAO.searchNotificheEventoByFilter(filter);
		return nots;
	}

	@Override
	public List<EventDO> searchEventByFilter(EventFilterDO filterDO) {
		List<EventDO> events = eventDAO.searchEventByFilter(filterDO);

		if (filterDO.getFetchRule().equals(EventAppDepthRule.NAME)
				|| filterDO.getFetchRule().equals(EventDetailDepthRule.NAME)) {
			events.forEach(e -> {
				eventDAO.evict(e);

				e.getInterventi().forEach(i -> {
					i.setCoordinate(new HashSet<>(getFirstCoordinateOfIntervention(i.getId())));
				});
				//
				// if
				// (filterDO.getFetchRule().equals(EventDetailDepthRule.NAME)) {
				// NotificheEventoFilterDO filterN = new
				// NotificheEventoFilterDO();
				//
				// filterN.setFetchRule(NotificheEventoEseguitoDepthRule.NAME);
				// filterN.setEvent(e.getId());
				//
				// e.setNotifiche(new
				// HashSet<>(searchNotificheEventoByFilter(filterN)));
				// }
			});
		}

		return events;
	}

	public List<InterventoCoordDO> getFirstCoordinateOfIntervention(String id) {
		return interventoCoordDAO.getFirstCoordinateOfIntervention(id);
	}

	public EventDO updateNumAccepted(String id, int numAccepted) {
		EventDO ev = eventDAO.get(id);
		ev.setAcceptedResponders(numAccepted);
		return eventDAO.save(ev);
	}

	@Override
	public List<EventDO> getNotifiedEvents(String frID) {
		Long maxInt = Long.valueOf(anaService.getParametro(ParametersEnum.MAX_ACTIVED_FIRST_RESPONDER.name(), "5"));
		List<EventDO> evts = eventDAO.getNotifiedEvents(frID, maxInt, EventDeepDepthRule.NAME);

		return evts;
	}

	@Override
	public long countNotified(String id) {
		return notificheEventoDAO.countNotified(id);
	}

	@Override
	public List<NotificheEventoDO> searchNotificheEventoByFilter(NotificheEventoFilterDO notificheEventoFilter) {
		return notificheEventoDAO.searchNotificheEventoByFilter(notificheEventoFilter);
	}

	@Override
	public EventDO saveAdditionalDataToEvent(EventDO event) {
		EventDO actual = eventDAO.get(event.getId());

		actual.setDefibrillato(event.getDefibrillato());
		actual.setNoteDAE(event.getNoteDAE());

		return actual;
	}

}
