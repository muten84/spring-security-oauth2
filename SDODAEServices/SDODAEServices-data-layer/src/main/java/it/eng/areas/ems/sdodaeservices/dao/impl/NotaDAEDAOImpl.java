package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.NotaDAEDAO;
import it.eng.areas.ems.sdodaeservices.entity.NotaDAEDO;

@Repository public class  NotaDAEDAOImpl extends EntityDAOImpl<NotaDAEDO, String> implements NotaDAEDAO{
	
	public NotaDAEDAOImpl() {
	}

	@Override
	public Class<NotaDAEDO> getEntityClass() {
		return NotaDAEDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
