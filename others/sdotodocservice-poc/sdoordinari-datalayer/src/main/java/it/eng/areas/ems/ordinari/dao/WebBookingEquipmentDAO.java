package it.eng.areas.ems.ordinari.dao;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.ordinari.entity.WebBookingEquipmentDO;

public interface WebBookingEquipmentDAO extends EntityDAO<WebBookingEquipmentDO,String> {

public	WebBookingEquipmentDO searchWebBookingEquipmentByDescription(
			String description);

	
	
	
}
