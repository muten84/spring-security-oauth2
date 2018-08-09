/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.areas.ems.sdodaeservices.bean.DaeEventActivation;
import it.eng.areas.ems.sdodaeservices.bean.DaeNumbers;
import it.eng.areas.ems.sdodaeservices.bean.DaeSubscription;
import it.eng.areas.ems.sdodaeservices.bean.FirstResponderSubscription;
import it.eng.areas.ems.sdodaeservices.dao.DaeDAO;
import it.eng.areas.ems.sdodaeservices.dao.DaeHistoryDAO;
import it.eng.areas.ems.sdodaeservices.dao.EventDAO;
import it.eng.areas.ems.sdodaeservices.dao.FirstResponderDAO;
import it.eng.areas.ems.sdodaeservices.service.GraphTransactionalService;

/**
 * @author Bifulco Luigi
 *
 */
@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class GraphTransactionalServiceImpl implements GraphTransactionalService {

	@Autowired
	private FirstResponderDAO frDAO;

	@Autowired
	private DaeDAO daeDAO;

	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private DaeHistoryDAO daeHistoryDAO;

	@Override
	public List<FirstResponderSubscription> countFrSubscription(Calendar from, Calendar to) {
		return frDAO.countFirstResponderSubscriptionPerMonth(from, to);
	}

	@Override
	public List<DaeSubscription> countDaeSubscription(Calendar from, Calendar to) {
		return daeDAO.countDaeSubscription(from, to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdodaeservices.service.GraphTransactionalService#
	 * countDaeActivation()
	 */
	@Override
	public List<DaeEventActivation> countDaeActivationByProvince(String categoryId, Calendar from, Calendar to) {
		return eventDAO.countDaeEvent(categoryId, from, to);
	}

	@Override
	public List<DaeEventActivation> countDAEActivationByDay(Calendar from, Calendar to, String categoryId) {
		return eventDAO.countDaeEventByDay(from, to, categoryId);
	}

	@Override
	public List<DaeEventActivation> countDAEAccepetedActivationByDay(Calendar from, Calendar to, String categoryId) {
		return eventDAO.countDaeAcceptedEventByDay(from, to, categoryId);
	}

	@Override
	public List<DaeEventActivation> listFRActivationsByType(String categoryId, Calendar fromCal, Calendar toCal) {
		return eventDAO.listDAEActivationsByType(categoryId, fromCal, toCal);
	}

	@Override
	public List<DaeEventActivation> listFRAcceptedByType(String categoryId, Calendar fromCal, Calendar toCal) {
		return eventDAO.listDAEAcceptedByType(categoryId, fromCal, toCal);
	}

	@Override
	public List<DaeSubscription> listDAEValidation(Calendar from, Calendar to) {
		return daeHistoryDAO.listDAEValidation(from, to);
	}

	@Override
	public List<DaeNumbers> fetchDaeNumbersByStatus(Calendar from, Calendar to) {
		return daeDAO.fetchDaeNumbersByStatus(from, to);
	}

	@Override
	public List<DaeNumbers> fetchFrNumbersByCategory(Calendar from, Calendar to) {
		return frDAO.fetchFrNumbersByCategory(from, to);
	}

}
