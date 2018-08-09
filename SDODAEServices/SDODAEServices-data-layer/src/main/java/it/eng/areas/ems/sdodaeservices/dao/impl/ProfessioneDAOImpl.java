package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.ProfessioneDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.ProfessioneDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.ProfessioneDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.ProfessioneFilterDO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class ProfessioneDAOImpl extends EntityDAOImpl<ProfessioneDO, String> implements ProfessioneDAO {

	public ProfessioneDAOImpl() {
	}

	@Override
	public Class<ProfessioneDO> getEntityClass() {
		return ProfessioneDO.class;
	}

	protected FetchRule getFetchRule(String name) {
		if (name.equals(ProfessioneDeepDepthRule.NAME)) {

			return new ProfessioneDeepDepthRule();
		}

		return null;
	}

	public List<ProfessioneDO> searchProfessioneByFilter(ProfessioneFilterDO filter) {
		return searchProfessioneByFilter(filter, null);
	}

	public List<ProfessioneDO> searchProfessioneByFilter(ProfessioneFilterDO filter, String fetchRule) {
		Criteria criteria = createCriteria(filter);

		if (fetchRule != null && !fetchRule.equals(null)) {
			criteria.setFetchMode(fetchRule, FetchMode.JOIN);
		} else {
			criteria.setFetchMode(ProfessioneDeepDepthRule.NAME, FetchMode.JOIN);

		}

		if (!StringUtils.isEmpty(filter.getName())) {
			criteria.add(Restrictions.ilike("descrizione", "%" + filter.getName().toLowerCase() + "%"));
		}

		criteria.addOrder(Order.desc("descrizione"));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public ProfessioneDO insertProfessione(ProfessioneDO professioneDO) {

		return this.save(professioneDO);
	}

}
