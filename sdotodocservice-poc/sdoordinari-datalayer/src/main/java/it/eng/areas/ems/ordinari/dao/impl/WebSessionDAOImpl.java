package it.eng.areas.ems.ordinari.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.ordinari.dao.WebSessionDAO;
import it.eng.areas.ems.ordinari.entity.WebSessionDO;

@Repository
public class WebSessionDAOImpl extends EntityDAOImpl<WebSessionDO, String> implements WebSessionDAO {

	public Class<WebSessionDO> getEntityClass() {
		return WebSessionDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.ordinari.dao.WebSessionDAO#getUserSession(java.lang.String)
	 */
	@Override
	public String getUserSession(String userId) {
		Criteria c = createCriteria();
		c.add(Restrictions.eq("webIdentityId", userId));
		WebSessionDO session = (WebSessionDO) c.uniqueResult();
		return session.getSessionId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.ordinari.dao.WebSessionDAO#checkSession(java.lang.String)
	 */
	@Override
	public WebSessionDO getSession(String userId) {
		Criteria c = createCriteria();
		c.add(Restrictions.eq("webIdentityId", userId));
		@SuppressWarnings("rawtypes")
		List list = c.list();
		if (list != null && !list.isEmpty()) {
			return (WebSessionDO) list.iterator().next();
		}
		return null;
	}

}
