package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.StatoUtenteDAO;
import it.eng.areas.ems.sdodaeservices.entity.StatoUtenteDO;

@Repository public class  StatoUtenteDAOImpl extends EntityDAOImpl<StatoUtenteDO, String> implements StatoUtenteDAO{
	
	public StatoUtenteDAOImpl() {
	}

	@Override
	public Class<StatoUtenteDO> getEntityClass() {
		return StatoUtenteDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
