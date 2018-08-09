package it.eng.areas.ems.ordinari.dao;

import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.ordinari.dao.filter.WebBookingFilterDO;
import it.eng.areas.ems.ordinari.entity.WebBookingDO;

public interface WebBookingDAO extends EntityDAO<WebBookingDO,String> {

	public	WebBookingDO saveWebBooking(WebBookingDO webBooking);

	public WebBookingDO getById(String id);

	public WebBookingDO updateWebBooking(
			WebBookingDO webBooking)  throws Exception;

	public List<WebBookingDO> searchWebBookingByFilter(
			WebBookingFilterDO webBookingFilterDO, String fetchRule);
	
	
	
}
