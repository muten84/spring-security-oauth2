/**
 * 
 */
package it.eng.areas.ems.ordinari.service;

import java.util.List;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.areas.ems.ordinari.dao.filter.WebBookingFilterDO;
import it.eng.areas.ems.ordinari.entity.WebBookingDO;

/**
 * @author Bifulco Luigi
 *
 */
public interface WebBookingTransactionalService {

	/**
	 * @param bookingCode
	 * @param fetchRule
	 * @return
	 * @throws DataAccessException
	 */
	
	public WebBookingDO save(WebBookingDO webBooking);
	public WebBookingDO getWebBookingById(String id) ;
	public WebBookingDO updateWebBooking(WebBookingDO webBooking) throws Exception;
	public List<WebBookingDO> searchWebBookingByFilter(
			WebBookingFilterDO WebBookingFilterDO);
	
}
