package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.LocalitaDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.LocalitaDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.LocalitaDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.LocalitaFilterDO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class LocalitaDAOImpl extends EntityDAOImpl<LocalitaDO, String> implements LocalitaDAO {

	@Override
	public Class<LocalitaDO> getEntityClass() {
		return LocalitaDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		if (name.equals(LocalitaDeepDepthRule.NAME)) {
			return new LocalitaDeepDepthRule();
		}
		return null;
	}

	@Override
	public List<LocalitaDO> searchLocalitaByFilter(LocalitaFilterDO filter) {

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

		criteria.addOrder(Order.asc("name"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return (List<LocalitaDO>) criteria.list();

	}

}
