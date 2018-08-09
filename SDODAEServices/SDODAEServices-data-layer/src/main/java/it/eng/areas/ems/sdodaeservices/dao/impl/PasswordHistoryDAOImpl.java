package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.PasswordHistoryDAO;
import it.eng.areas.ems.sdodaeservices.entity.PasswordHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.PasswordHistoryFilterDO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class PasswordHistoryDAOImpl extends EntityDAOImpl<PasswordHistoryDO, String> implements PasswordHistoryDAO {

	@Override
	public Class<PasswordHistoryDO> getEntityClass() {
		return PasswordHistoryDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}

	@Override
	public PasswordHistoryDO searchPasswordHistory(String userId, String hashed) {
		Criteria criteria = createCriteria();

		criteria.add(Restrictions.eq("utente.id", userId));
		criteria.add(Restrictions.eq("passwordHash", hashed));
		criteria.add(Restrictions.eq("type", "PASSWORD"));

		return (PasswordHistoryDO) criteria.uniqueResult();
	}

	@Override
	public List<PasswordHistoryDO> searchWordInPassword(String userId) {
		Criteria criteria = createCriteria();

		criteria.add(Restrictions.eq("utente.id", userId));
		criteria.add(Restrictions.eq("type", "WORD"));

		return criteria.list();
	}

	@Override
	public List<PasswordHistoryDO> searchPasswordHistoryByfilter(PasswordHistoryFilterDO filter) {
		Criteria criteria = createCriteria();

		if (!StringUtils.isEmpty(filter.getUserId())) {
			criteria.add(Restrictions.eq("utente.id", filter.getUserId()));
		}
		if (filter.getMaxResult() > 0) {
			criteria.addOrder(Order.desc("creationDate"));
			criteria.setMaxResults(filter.getMaxResult());

		} else if (!StringUtils.isEmpty(filter.getUserId())) {
			criteria.add(Restrictions.eq("passwordHash", filter.getHashed()));
		}
		if (!StringUtils.isEmpty(filter.getType())) {
			criteria.add(Restrictions.eq("type", filter.getType()));
		}
		return criteria.list();
	}

	@Override
	public List<PasswordHistoryDO> searchPasswordHistoryByUserId(String userId) {
		Criteria criteria = createCriteria();

		if (!StringUtils.isEmpty(userId)) {
			criteria.add(Restrictions.eq("utente.id", userId));
		}
		criteria.add(Restrictions.eq("type", "PASSWORD"));

		criteria.addOrder(Order.asc("creationDate"));

		return criteria.list();
	}
}
