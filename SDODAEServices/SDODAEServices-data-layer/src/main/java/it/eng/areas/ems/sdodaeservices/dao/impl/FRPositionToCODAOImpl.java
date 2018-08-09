package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.FRPositionToCODAO;
import it.eng.areas.ems.sdodaeservices.entity.FRPositionToCODO;

@Repository
public class FRPositionToCODAOImpl extends EntityDAOImpl<FRPositionToCODO, String> implements FRPositionToCODAO {

	@Override
	public Class<FRPositionToCODO> getEntityClass() {
		return FRPositionToCODO.class;
	}

	@Override
	protected FetchRule getFetchRule(String arg0) {
		return null;
	}

	@Override
	public List<FRPositionToCODO> getLastFRPositionToCO(Integer maxResult) {
		Criteria c = createCriteria();

		c.setFetchMode("firstResponder", FetchMode.JOIN);
		c.setFetchMode("firstResponder.utente", FetchMode.JOIN);

		c.setMaxResults(maxResult);

		c.addOrder(Order.desc("timeStamp"));

		return c.list();
	}

}
