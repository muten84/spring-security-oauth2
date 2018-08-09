package it.eng.areas.ems.sdodaeservices.dao;

import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.StaticDataDO;

public interface StaticDataDAO extends EntityDAO<StaticDataDO, String> {

	List<StaticDataDO> searchStaticDataByType(String type);

}
