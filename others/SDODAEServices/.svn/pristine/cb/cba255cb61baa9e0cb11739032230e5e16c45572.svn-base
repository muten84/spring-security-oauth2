package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.StradeDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.StradeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.StradeDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.StradeFilterDO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class StradeDAOImpl extends EntityDAOImpl<StradeDO, String> implements StradeDAO {

	public StradeDAOImpl() {
	}

	@Override
	public Class<StradeDO> getEntityClass() {
		return StradeDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		if (name.equals(StradeDeepDepthRule.NAME)) {

			return new StradeDeepDepthRule();
		}

		return null;
	}

	@Override
	public List<StradeDO> searchStradeByFilter(StradeFilterDO filter) {

		Criteria criteria = createCriteria(filter);

		criteria.add(Restrictions.eq("deleted", false));

		if (!StringUtils.isEmpty(filter.getId())) {
			criteria.add(Restrictions.eq("id", filter.getId()));
		}
		if (!StringUtils.isEmpty(filter.getCodiceIstatComune()) || !StringUtils.isEmpty(filter.getNomeComune())) {
			criteria.createAlias("comune", "comune");
			if (!StringUtils.isEmpty(filter.getCodiceIstatComune())) {
				criteria.add(Restrictions.eq("comune.codiceIstat", filter.getCodiceIstatComune()));
			}

			if (!StringUtils.isEmpty(filter.getNomeComune())) {
				criteria.add(Restrictions.eq("comune.nomeComune", filter.getNomeComune()));
			}
		}

		if (!StringUtils.isEmpty(filter.getName())) {
			criteria.add(Restrictions.ilike("name", "%" + filter.getName().toLowerCase() + "%"));
		}
		criteria.addOrder(Order.desc("name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return (List<StradeDO>) criteria.list();
	}

}
