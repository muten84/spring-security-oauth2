package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.ComuneDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.ComuniProvinceDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.ComuneDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.ComuneFilterDO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class ComuneDAOImpl extends EntityDAOImpl<ComuneDO, String> implements ComuneDAO {

	public ComuneDAOImpl() {
	}

	@Override
	public Class<ComuneDO> getEntityClass() {
		return ComuneDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		if (name.equals(ComuniProvinceDeepDepthRule.NAME)) {
			return new ComuniProvinceDeepDepthRule();
		}
		return null;
	}

	@Override
	public List<ComuneDO> searchComuniByFilter(ComuneFilterDO filter) {

		Criteria criteria = createCriteria(filter);

		criteria.add(Restrictions.eq("deleted", false));

		if (!StringUtils.isEmpty(filter.getNomeProvincia())) {
			criteria.createAlias("provincia", "prov");
			criteria.add(Restrictions.eq("prov.nomeProvincia", filter.getNomeProvincia()));
		}

		if (!StringUtils.isEmpty(filter.getCodiceIstat())) {
			criteria.add(Restrictions.eq("codiceIstat", filter.getCodiceIstat()));
		}

		if (!StringUtils.isEmpty(filter.getNomeComune())) {

			criteria.add(Restrictions.ilike("nomeComune", "%" + filter.getNomeComune().toLowerCase() + "%"));
		}

		criteria.addOrder(Order.asc("nomeComune"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<ComuneDO> toRet = new ArrayList<ComuneDO>();

		return (List<ComuneDO>) criteria.list();
	}

	@Override
	public ComuneDO getComuneByIstat(String istat) {
		Criteria criteria = createCriteria();

		criteria.add(Restrictions.eq("deleted", false));

		if (!StringUtils.isEmpty(istat)) {
			criteria.add(Restrictions.eq("codiceIstat", istat));
		}
		return (ComuneDO) criteria.uniqueResult();
	}

}
