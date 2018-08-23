package it.eng.areas.ems.sdodaeservices.dao;


import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.ProfessioneDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.ProfessioneFilterDO;

public interface ProfessioneDAO extends EntityDAO<ProfessioneDO, String> {

public	List<ProfessioneDO> searchProfessioneByFilter(ProfessioneFilterDO professioneFilter);

public	ProfessioneDO insertProfessione(ProfessioneDO professioneDO);

	
}
