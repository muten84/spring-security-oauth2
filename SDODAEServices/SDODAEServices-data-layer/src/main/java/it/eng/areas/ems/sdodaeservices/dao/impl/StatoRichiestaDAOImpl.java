package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.StatoRichiestaDAO;
import it.eng.areas.ems.sdodaeservices.entity.StatoRichiestaDO;

@Repository public class  StatoRichiestaDAOImpl extends EntityDAOImpl<StatoRichiestaDO, String> implements StatoRichiestaDAO{
	
	public StatoRichiestaDAOImpl() {
	}

	@Override
	public Class<StatoRichiestaDO> getEntityClass() {
		return StatoRichiestaDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
