package it.eng.areas.ems.sdodaeservices.delegate;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.delegate.model.CheckInterventionBean;
import it.eng.areas.ems.sdodaeservices.delegate.model.CheckInterventionResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Event;
import it.eng.areas.ems.sdodaeservices.delegate.model.EventInfoRequest;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.GPSLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericException;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Intervento;
import it.eng.areas.ems.sdodaeservices.delegate.model.NotificheEvento;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.EventFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.InterventoFilter;
import it.eng.areas.ems.sdodaeservices.entity.filter.NotificheEventoFilterDO;

public interface EventDelegateService {

	public Event getEventById(String id);

	public boolean deleteEventById(String id);

	public long countAll();

	public List<Event> getEventByFilter(EventFilter eventFilter);

	public Event insertEvent(Event event);

	public List<Event> getAllEvent();

	public Intervento insertIntervento(Intervento intervento);

	public Intervento getInterventoById(String id);

	public boolean deleteInterventoById(String id);

	public List<Intervento> getAllIntervento();

	public List<Intervento> getInterventiByFirstResponder(FirstResponder firstResponder);

	public Event getEventInterventionDetails(EventInfoRequest request) throws GenericException;

	public List<FirstResponder> alertFirstResponderForNewEvent(Event event);

	public boolean insertNotificheEvento(NotificheEvento notificheEvento);

	public List<NotificheEvento> getAllNotificheEvento();

	public List<NotificheEvento> getNotificheEventiByEventId(String eventId);

	public List<Intervento> searchInterventoByFilter(InterventoFilter filter);

	public CheckInterventionResponse checkIntervention(CheckInterventionBean checkIntervention, FirstResponder fr)
			throws GenericException, Exception;

	public List<Intervento> updateEvent(Event event);

	public Event getEventByCartellinoAndCO(String cartellino, String coRiferimento);

	public GenericResponse rejectEvent(EventInfoRequest event, FirstResponder fr);

	public Intervento getInterventoAttivoByFirstResponderAndEventId(String firstResponderID, String eventId);

	public Intervento getInterventoAttivoByFirstResponder(String firstResponderID);

	public List<Event> getAvailableEventsForFirstResponder(FirstResponder fr);

	public int closeOrAbortInterventionByEvent(EventInfoRequest eventInfo, boolean abort);

	public boolean closeIntervention(String firstResponderId, String eventID, GPSLocation coordinate);

	public List<Event> searchEventByFilter(EventFilter filter);

	public long countNotified(String id);

	public Event saveEvent(Event event);

	public List<NotificheEvento> searchNotifiche(NotificheEventoFilterDO filter);

}
