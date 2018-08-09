package it.eng.areas.ems.sdodaeservices.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.ConfigDAO;
import it.eng.areas.ems.sdodaeservices.entity.ConfigDO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class ConfigDAOImpl extends EntityDAOImpl<ConfigDO, String> implements ConfigDAO {

	public ConfigDAOImpl() {
	}

	@Override
	public Class<ConfigDO> getEntityClass() {
		return ConfigDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}

	@Override
	public ConfigDO getConfigParameter(String parameter) {

		Criteria criteria = createCriteria();

		if (!StringUtils.isEmpty(parameter)) {
			criteria.add(Restrictions.eq("parametro", parameter));
		}

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return (ConfigDO) criteria.uniqueResult();
	}

}
