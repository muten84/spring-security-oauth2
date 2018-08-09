package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.NotificheConfigDAO;
import it.eng.areas.ems.sdodaeservices.entity.NotificheConfigDO;

@Repository public class  NotificheConfigDAOImpl extends EntityDAOImpl<NotificheConfigDO, String> implements NotificheConfigDAO{
	
	public NotificheConfigDAOImpl() {
	}

	@Override
	public Class<NotificheConfigDO> getEntityClass() {
		return NotificheConfigDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
