package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.LogAlertDAEDAO;
import it.eng.areas.ems.sdodaeservices.entity.LogAlertDAEDO;

@Repository public class  LogAlertDAEDAOImpl extends EntityDAOImpl<LogAlertDAEDO, String> implements LogAlertDAEDAO{
	
	public LogAlertDAEDAOImpl() {
	}

	@Override
	public Class<LogAlertDAEDO> getEntityClass() {
		return LogAlertDAEDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
