/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.service;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.entity.EventDO;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoDO;
import it.eng.areas.ems.sdodaeservices.entity.NotificheEventoDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.EventFilterDO;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTEventDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.InterventoFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.NotificheEventoFilterDO;

public interface EventTransactionalService {

	/**
	 * 
	 * Get an entity of type ExampleDO by its Id
	 * 
	 * @param id
	 * @return
	 */
	public List<EventDO> getAllEvent();

	public EventDO getEventById(String id);

	public EventDO getEventById(String fetch, String id);

	public boolean deleteEventById(String id);

	long countAll();

	public List<EventDO> getEventByFilter(EventFilterDO daeFilter);

	public EventDO saveEvent(EventDO daeDO);

	public InterventoDO insertIntervento(InterventoDO interventoDO);

	public InterventoDO getInterventoById(String id);

	public boolean deleteInterventoById(String id);

	public List<InterventoDO> getAllIntervento();

	public List<InterventoDO> getInterventiByFirstResponder(FirstResponderDO firstResponder);

	public NotificheEventoDO insertNotificheEvento(NotificheEventoDO notificheEventoDO);

	public NotificheEventoDO getNotificheEventoById(String id);

	public List<NotificheEventoDO> getAllNotificheEvento();

	public List<NotificheEventoDO> getNotificheEventiByEventId(String eventId);

	public List<InterventoDO> searchInterventionByFilter(InterventoFilterDO filter);

	public List<InterventoDO> getInterventiAttiviByFirstResponder(FirstResponderDO firstResponder);

	public List<NotificheEventoDO> getNotificheByEventId(String eventID);

	List<EventDO> getAllActiveEvents(String frID);

	public List<EventDO> searchEventByFilter(EventFilterDO filterDO);

	public EventDO updateNumAccepted(String id, int numAccepted);

	public List<EventDO> getNotifiedEvents(String id);

	public long countNotified(String id);

	public List<NotificheEventoDO> searchNotificheEventoByFilter(NotificheEventoFilterDO notificheEventoFilter);

	public EventDO saveAdditionalDataToEvent(EventDO event);

}
