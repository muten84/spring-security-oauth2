package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.CalendarioFestivitaDAO;
import it.eng.areas.ems.sdodaeservices.entity.CalendarioFestivitaDO;

@Repository public class  CalendarioFestivitaDAOImpl extends EntityDAOImpl<CalendarioFestivitaDO, String> implements CalendarioFestivitaDAO{
	
	public CalendarioFestivitaDAOImpl() {
	}

	@Override
	public Class<CalendarioFestivitaDO> getEntityClass() {
		return CalendarioFestivitaDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
