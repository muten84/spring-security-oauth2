package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.CategoriaFrDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.CategoriaFrDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.CategoriaFrDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.CategoriaFrFilterDO;

@Repository
public class CategoriaFrDAOImpl extends EntityDAOImpl<CategoriaFrDO, String> implements CategoriaFrDAO {

	public CategoriaFrDAOImpl() {
	}

	@Override
	public Class<CategoriaFrDO> getEntityClass() {
		return CategoriaFrDO.class;
	}

	protected FetchRule getFetchRule(String name) {
		if (name.equals(CategoriaFrDeepDepthRule.NAME)) {

			return new CategoriaFrDeepDepthRule();
		}

		return null;
	}

	public List<CategoriaFrDO> searchCategoriaFrByFilter(CategoriaFrFilterDO filter) {
		return searchCategoriaFrByFilter(filter, null);
	}

	public List<CategoriaFrDO> searchCategoriaFrByFilter(CategoriaFrFilterDO filter, String fetchRule) {
		Criteria criteria = createCriteria(filter);

		if (!StringUtils.isEmpty(fetchRule)) {
			criteria.setFetchMode(fetchRule, FetchMode.JOIN);
		}

		if (filter.getForApp() != null) {
			criteria.add(Restrictions.eqOrIsNull("visibleOnApp", filter.getForApp()));
		}

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public CategoriaFrDO insertCategoriaFr(CategoriaFrDO categoriaFrDO) {

		return this.save(categoriaFrDO);
	}

}
