package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.GPSLocationDAO;
import it.eng.areas.ems.sdodaeservices.entity.GPSLocationDO;

@Repository public class  GPSLocationDAOImpl extends EntityDAOImpl<GPSLocationDO, String> implements GPSLocationDAO{
	
	public GPSLocationDAOImpl() {
	}

	@Override
	public Class<GPSLocationDO> getEntityClass() {
		return GPSLocationDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
