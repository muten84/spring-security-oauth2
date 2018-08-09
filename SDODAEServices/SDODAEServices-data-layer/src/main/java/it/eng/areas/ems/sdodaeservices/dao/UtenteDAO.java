package it.eng.areas.ems.sdodaeservices.dao;

import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.UtenteFilterDO;

public interface UtenteDAO extends EntityDAO<UtenteDO, String> {

	public List<UtenteDO> searchUtenteByFilter(UtenteFilterDO filter);

	public UtenteDO getUtenteByLogonAndPasswordHash(String logon, String hashed);

}
