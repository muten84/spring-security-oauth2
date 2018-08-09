package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.StaticDataDAO;
import it.eng.areas.ems.sdodaeservices.entity.StaticDataDO;

@Repository
public class StaticDataDAOImpl extends EntityDAOImpl<StaticDataDO, String> implements StaticDataDAO {

	@Override
	public Class<StaticDataDO> getEntityClass() {
		return StaticDataDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String arg0) {
		return null;
	}

	@Override
	public List<StaticDataDO> searchStaticDataByType(String type) {
		Criteria c = createCriteria();

		c.add(Restrictions.eq("type", type));

		c.addOrder(Order.asc("ordering"));

		return c.list();
	}

}
