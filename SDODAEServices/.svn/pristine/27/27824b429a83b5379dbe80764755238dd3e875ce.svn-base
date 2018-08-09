/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.delegate;

import java.util.Calendar;
import java.util.List;

import it.eng.areas.ems.sdodaeservices.delegate.model.DaeEventActivationDTO;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeNumbersDTO;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeSubscriptionDTO;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponderSubscriptionDTO;

/**
 * @author Bifulco Luigi
 *
 */
public interface GraphServiceDelegate {

	/**
	 * @param from
	 * 
	 * @param to
	 * 
	 * @return
	 */
	List<FirstResponderSubscriptionDTO> listFrSubscriptions(Calendar from, Calendar to);

	/**
	 * @param from
	 * 
	 * @param to
	 * @return
	 */
	List<DaeSubscriptionDTO> listDAESubscriptions(Calendar from, Calendar to);

	/**
	 * @param categoryId
	 * @param from
	 * 
	 * @param to
	 * @return
	 */
	List<DaeEventActivationDTO> listDAEActivationsByProvince(String categoryId, Calendar from, Calendar to);

	List<DaeEventActivationDTO> listDAEActivationsByDay(Calendar from, Calendar to, String categoryId);

	/**
	 * @param from
	 *            o
	 * @param to
	 * @param string
	 * 
	 * @return
	 */
	List<DaeEventActivationDTO> listDAEAccepetedActivationsByDay(Calendar from, Calendar to, String categoryId);

	List<DaeEventActivationDTO> listFRActivationsByType(String string, Calendar fromCal, Calendar toCal);

	List<DaeEventActivationDTO> listFRAcceptedByType(String string, Calendar fromCal, Calendar toCal);

	List<DaeSubscriptionDTO> listDAEValidation(Calendar fromCal, Calendar toCal);

	List<DaeNumbersDTO> fetchDaeNumbersByStatus(Calendar fromCal, Calendar toCal);

	List<DaeNumbersDTO> fetchFrNumbersByCategory(Calendar fromCal, Calendar toCal);

}
