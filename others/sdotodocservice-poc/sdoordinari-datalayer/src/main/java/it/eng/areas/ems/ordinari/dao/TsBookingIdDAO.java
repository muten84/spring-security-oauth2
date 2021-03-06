package it.eng.areas.ems.ordinari.dao;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.ordinari.entity.DdtDO;
import it.eng.areas.ems.ordinari.entity.TsBookingIdDO;

import java.util.List;

import javax.persistence.Id;

public interface TsBookingIdDAO extends EntityDAO<TsBookingIdDO,String> {

	public String generateBookingCode(String fetchRule) throws DataAccessException;
	
}
