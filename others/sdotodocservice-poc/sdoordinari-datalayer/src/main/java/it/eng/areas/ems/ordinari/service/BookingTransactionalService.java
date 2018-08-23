/**
 * 
 */
package it.eng.areas.ems.ordinari.service;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.areas.ems.ordinari.entity.ToBookingDO;

/**
 * @author Bifulco Luigi
 *
 */
public interface BookingTransactionalService {

	/**
	 * @param bookingCode
	 * @param fetchRule
	 * @return
	 * @throws DataAccessException
	 */
	ToBookingDO getBookingByCode(String bookingCode, String fetchRule) throws DataAccessException;

	ToBookingDO saveBooking(ToBookingDO toBooking) throws DataAccessException;

	ToBookingDO getToBookingById(String string) throws DataAccessException;

}
