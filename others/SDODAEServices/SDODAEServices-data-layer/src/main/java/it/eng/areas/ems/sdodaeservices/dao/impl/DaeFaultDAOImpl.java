package it.eng.areas.ems.sdodaeservices.dao.impl;

import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.DaeFaultDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeFaultDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.DaeFaultDO;

@Repository
public class DaeFaultDAOImpl extends EntityDAOImpl<DaeFaultDO, String> implements DaeFaultDAO {

	@Override
	public Class<DaeFaultDO> getEntityClass() {
		return DaeFaultDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		if (name.equals(DaeFaultDeepDepthRule.NAME)) {
			return new DaeFaultDeepDepthRule();
		}
		return null;
	}

}
