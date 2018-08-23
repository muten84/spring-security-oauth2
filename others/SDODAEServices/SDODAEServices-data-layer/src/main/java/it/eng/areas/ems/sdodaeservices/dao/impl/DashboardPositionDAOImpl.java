package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.DashboardPositionDAO;
import it.eng.areas.ems.sdodaeservices.entity.DashboardPositionDO;

@Repository
public class DashboardPositionDAOImpl extends EntityDAOImpl<DashboardPositionDO, String>
		implements DashboardPositionDAO {

	@Override
	public Class<DashboardPositionDO> getEntityClass() {
		return DashboardPositionDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}

	@Override
	public List<DashboardPositionDO> getDashboardPositionsByUserId(String utenteId) {
		Criteria cri = createCriteria();

		cri.add(Restrictions.eq("utenteId", utenteId));

		cri.addOrder(Order.asc("ordering"));

		return cri.list();
	}

}
