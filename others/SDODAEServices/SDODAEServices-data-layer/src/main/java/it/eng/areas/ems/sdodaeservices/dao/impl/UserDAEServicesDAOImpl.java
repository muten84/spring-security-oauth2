package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.UserDAEServicesDAO;
import it.eng.areas.ems.sdodaeservices.entity.UserDAEServicesDO;

@Repository public class  UserDAEServicesDAOImpl extends EntityDAOImpl<UserDAEServicesDO, String> implements UserDAEServicesDAO{
	
	public UserDAEServicesDAOImpl() {
	}

	@Override
	public Class<UserDAEServicesDO> getEntityClass() {
		return UserDAEServicesDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
