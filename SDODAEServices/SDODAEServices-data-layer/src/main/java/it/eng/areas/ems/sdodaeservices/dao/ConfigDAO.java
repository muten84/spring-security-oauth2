package it.eng.areas.ems.sdodaeservices.dao;


import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.ConfigDO;

public interface ConfigDAO extends EntityDAO<ConfigDO, String> {

	ConfigDO getConfigParameter(String parameter);

}
