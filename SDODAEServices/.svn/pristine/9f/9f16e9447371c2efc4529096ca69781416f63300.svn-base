package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.LocationDAO;
import it.eng.areas.ems.sdodaeservices.entity.LocationDO;

@Repository public class  LocationDAOImpl extends EntityDAOImpl<LocationDO, String> implements LocationDAO{
	
	public LocationDAOImpl() {
	}

	@Override
	public Class<LocationDO> getEntityClass() {
		return LocationDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
