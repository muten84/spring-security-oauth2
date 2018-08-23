/**
 * 
 */
package it.eng.areas.ems.ordinari.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.areas.ems.ordinari.dao.ToBookingDAO;
import it.eng.areas.ems.ordinari.dao.TsBookingIdDAO;
import it.eng.areas.ems.ordinari.dao.rule.BookingDetailRule;
import it.eng.areas.ems.ordinari.entity.ToBookingDO;
import it.eng.areas.ems.ordinari.service.BookingTransactionalService;

/**
 * @author Bifulco Luigi
 *
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BookingTransactionalServiceImpl implements BookingTransactionalService {

	@Autowired
	private ToBookingDAO bookingDAO;
	
	@Autowired
	private TsBookingIdDAO tsBookingIdDAO;

	@Override
	public ToBookingDO getBookingByCode(String bookingCode, String fetchRule) throws DataAccessException {
		return bookingDAO.getBookingByCode(bookingCode, fetchRule);
	}
	
	@Override
	public ToBookingDO saveBooking(ToBookingDO toBooking) throws DataAccessException {
		String fetchRule = BookingDetailRule.NAME;
		toBooking.setCode(tsBookingIdDAO.generateBookingCode(fetchRule));
		toBooking.setId(UUID.randomUUID().toString().substring(0, 28));
		toBooking.setCreationDate(new Date());
		toBooking.setCreatedBy("GTO");
		return bookingDAO.save(toBooking);
		//return toBooking;
	}

	@Override
	public ToBookingDO getToBookingById(String id) throws DataAccessException{
		String fetchRule = BookingDetailRule.NAME;
		return bookingDAO.get(fetchRule, id);
	}

}
