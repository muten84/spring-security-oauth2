package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.ProfiloSanitarioDAO;
import it.eng.areas.ems.sdodaeservices.entity.ProfiloSanitarioDO;

@Repository public class  ProfiloSanitarioDAOImpl extends EntityDAOImpl<ProfiloSanitarioDO, String> implements ProfiloSanitarioDAO{
	
	public ProfiloSanitarioDAOImpl() {
	}

	@Override
	public Class<ProfiloSanitarioDO> getEntityClass() {
		return ProfiloSanitarioDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
