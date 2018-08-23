package it.eng.areas.ems.sdodaeservices.dao;


import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.CategoriaFrDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.CategoriaFrFilterDO;

public interface CategoriaFrDAO extends EntityDAO<CategoriaFrDO, String> {

public	List<CategoriaFrDO> searchCategoriaFrByFilter(CategoriaFrFilterDO categoriaFrFilter);

public	CategoriaFrDO insertCategoriaFr(CategoriaFrDO categoriaFrDO);

	
}
