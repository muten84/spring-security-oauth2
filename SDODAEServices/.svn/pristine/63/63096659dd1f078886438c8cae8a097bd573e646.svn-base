/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.service;

import java.util.Calendar;
import java.util.List;

import it.eng.areas.ems.sdodaeservices.bean.DaeEventActivation;
import it.eng.areas.ems.sdodaeservices.bean.DaeNumbers;
import it.eng.areas.ems.sdodaeservices.bean.DaeSubscription;
import it.eng.areas.ems.sdodaeservices.bean.FirstResponderSubscription;

/**
 * @author Bifulco Luigi
 *
 */
public interface GraphTransactionalService {

	/**
	 * @param to
	 * @param from
	 * @return
	 */
	List<FirstResponderSubscription> countFrSubscription(Calendar from, Calendar to);

	/**
	 * @param to
	 * @param from
	 * @return
	 */
	List<DaeSubscription> countDaeSubscription(Calendar from, Calendar to);

	/**
	 * 
	 * @param categoryId
	 * @return
	 */
	List<DaeEventActivation> countDaeActivationByProvince(String categoryId, Calendar from, Calendar to);

	List<DaeEventActivation> countDAEActivationByDay(Calendar from, Calendar to, String categoryId);

	/**
	 * @return
	 */
	List<DaeEventActivation> countDAEAccepetedActivationByDay(Calendar from, Calendar to, String categoryId);

	List<DaeEventActivation> listFRActivationsByType(String categoryId, Calendar fromCal, Calendar toCal);

	List<DaeEventActivation> listFRAcceptedByType(String categoryId, Calendar fromCal, Calendar toCal);

	List<DaeSubscription> listDAEValidation(Calendar from, Calendar to);

	List<DaeNumbers> fetchDaeNumbersByStatus(Calendar from, Calendar to);

	List<DaeNumbers> fetchFrNumbersByCategory(Calendar from, Calendar to);
}
