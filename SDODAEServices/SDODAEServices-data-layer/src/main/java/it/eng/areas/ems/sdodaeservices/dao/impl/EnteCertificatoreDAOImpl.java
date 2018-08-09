package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.EnteCertificatoreDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.EnteCertificatoreDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.EnteCertificatoreDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.EnteCertificatoreFilterDO;

@Repository
public class EnteCertificatoreDAOImpl extends EntityDAOImpl<EnteCertificatoreDO, String>
		implements EnteCertificatoreDAO {

	public EnteCertificatoreDAOImpl() {
	}

	@Override
	public Class<EnteCertificatoreDO> getEntityClass() {
		return EnteCertificatoreDO.class;
	}

	protected FetchRule getFetchRule(String name) {
		if (name.equals(EnteCertificatoreDeepDepthRule.NAME)) {

			return new EnteCertificatoreDeepDepthRule();
		}

		return null;
	}

	public List<EnteCertificatoreDO> searchEnteCertificatoreByFilter(EnteCertificatoreFilterDO filter) {
		return searchEnteCertificatoreByFilter(filter, null);
	}

	public List<EnteCertificatoreDO> searchEnteCertificatoreByFilter(EnteCertificatoreFilterDO filter,
			String fetchRule) {
		Criteria criteria = createCriteria(filter);

		if (!StringUtils.isEmpty(fetchRule)) {
			criteria.setFetchMode(fetchRule, FetchMode.JOIN);
		}

		if (filter.getOnlyForMed() != null) {
			if (filter.getOnlyForMed()) {
				criteria.add(Restrictions.eq("onlyForMed", true));
			}
		}

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public EnteCertificatoreDO insertEnteCertificatore(EnteCertificatoreDO enteCertificatoreDO) {

		return this.save(enteCertificatoreDO);
	}

}
