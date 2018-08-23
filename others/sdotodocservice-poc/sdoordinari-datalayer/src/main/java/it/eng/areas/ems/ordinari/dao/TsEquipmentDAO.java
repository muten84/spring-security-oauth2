package it.eng.areas.ems.ordinari.dao;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.ordinari.entity.DdtDO;
import it.eng.areas.ems.ordinari.entity.TsEquipmentDO;

import java.util.List;

import javax.persistence.Id;

import org.hibernate.Criteria;

public interface TsEquipmentDAO extends EntityDAO<TsEquipmentDO,String> {

public	TsEquipmentDO getTsEquipmentByDescription(String description, String nAME);


	
	
	
}
