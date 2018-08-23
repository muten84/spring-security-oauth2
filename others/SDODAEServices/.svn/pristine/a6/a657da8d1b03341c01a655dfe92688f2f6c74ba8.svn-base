package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.StatoDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.StatoDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.StatoDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.StatoFilterDO;

@Repository
public class StatoDAOImpl extends EntityDAOImpl<StatoDO, String> implements StatoDAO {

	public StatoDAOImpl() {
	}

	@Override
	public Class<StatoDO> getEntityClass() {
		return StatoDO.class;
	}

	protected FetchRule getFetchRule(String name) {
		if (name.equals(StatoDeepDepthRule.NAME)) {

			return new StatoDeepDepthRule();
		}

		return null;
	}

	public List<StatoDO> searchStatoByFilter(StatoFilterDO filter) {
		return searchStatoByFilter(filter, null);
	}

	public List<StatoDO> searchStatoByFilter(StatoFilterDO filter, String fetchRule) {
		Criteria criteria = createCriteria(filter);

		if (!fetchRule.equals(null)) {
			criteria.setFetchMode(fetchRule, FetchMode.JOIN);
		}

		criteria.addOrder(Order.desc("nome"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public StatoDO insertStato(StatoDO statoDO) {

		return this.save(statoDO);
	}

}
