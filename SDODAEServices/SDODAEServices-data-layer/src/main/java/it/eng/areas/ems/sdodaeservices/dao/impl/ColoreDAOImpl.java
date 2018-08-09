package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.ColoreDAO;
import it.eng.areas.ems.sdodaeservices.entity.ColoreDO;


@Repository public class  ColoreDAOImpl extends EntityDAOImpl<ColoreDO, String> implements ColoreDAO{
	
	public ColoreDAOImpl() {
	}

	@Override
	public Class<ColoreDO> getEntityClass() {
		return ColoreDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
