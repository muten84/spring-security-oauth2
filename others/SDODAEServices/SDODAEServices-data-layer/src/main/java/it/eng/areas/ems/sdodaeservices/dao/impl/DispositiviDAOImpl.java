package it.eng.areas.ems.sdodaeservices.dao.impl;

import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.DispositiviDAO;
import it.eng.areas.ems.sdodaeservices.entity.DispositiviDO;

@Repository
public class DispositiviDAOImpl extends EntityDAOImpl<DispositiviDO, String> implements DispositiviDAO {

	@Override
	public Class<DispositiviDO> getEntityClass() {
		return DispositiviDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}

}
