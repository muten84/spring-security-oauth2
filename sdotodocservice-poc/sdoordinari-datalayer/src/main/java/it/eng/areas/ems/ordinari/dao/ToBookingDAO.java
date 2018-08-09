package it.eng.areas.ems.ordinari.dao;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.ordinari.entity.ToBookingDO;

public interface ToBookingDAO extends EntityDAO<ToBookingDO, String> {

	public ToBookingDO getBookingByCode(String bookingCode, String fetchRule) throws DataAccessException;
}
