package it.eng.areas.ems.ordinari.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.ordinari.dao.WebIdentityDAO;
import it.eng.areas.ems.ordinari.entity.WebIdentityDO;

@Repository
public class WebIdentityDAOImpl extends EntityDAOImpl<WebIdentityDO, String> implements WebIdentityDAO {

	public Class<WebIdentityDO> getEntityClass() {
		return WebIdentityDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String arg0) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.ordinari.dao.WebIdentityDAO#getIdentityByUsername(java.lang.String)
	 */
	@Override
	public WebIdentityDO getIdentityByUsername(String username) {
		Criteria c = createCriteria();
		c.add(Restrictions.eqOrIsNull("login", username));
		return (WebIdentityDO) c.uniqueResult();
	}

}
