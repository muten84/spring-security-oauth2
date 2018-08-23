package it.eng.areas.ems.sdodaeservices.dao;

import java.util.Calendar;
import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.bean.DaeEventActivation;
import it.eng.areas.ems.sdodaeservices.entity.EventDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.EventFilterDO;

public interface EventDAO extends EntityDAO<EventDO, String> {

	/**
	 * 
	 * @param eventFilter
	 * @return
	 */
	public List<EventDO> searchEventByFilter(EventFilterDO eventFilter);

	/**
	 * 
	 * @param eventDO
	 * @return
	 */
	public EventDO insertEvent(EventDO eventDO);

	/**
	 * @param categoryId
	 * @param month
	 * @param year
	 * @return
	 */
	public List<DaeEventActivation> countDaeEvent(String categoryId, Calendar from, Calendar to);

	public List<DaeEventActivation> countDaeEventByDay(Calendar from, Calendar to, String categoryId);

	public List<EventDO> getAvailableEvents(String frID, Integer maxIntervention, String fetchRule);

	public List<DaeEventActivation> countDaeAcceptedEventByDay(Calendar from, Calendar to, String categoryId);

	public List<EventDO> getNotifiedEvents(String frID, Long maxInt, String depth);

	public List<DaeEventActivation> listDAEActivationsByType(String categoryId, Calendar fromCal, Calendar toCal);

	public List<DaeEventActivation> listDAEAcceptedByType(String categoryId, Calendar fromCal, Calendar toCal);

}
