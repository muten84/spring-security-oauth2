/*
 * 
 */
package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.area118.sdocore.dao.impl.OperationalCentralAwareDAO;
import it.eng.areas.ems.sdodaeservices.dao.PrivacyDictionaryDAO;
import it.eng.areas.ems.sdodaeservices.entity.PrivacyDictionaryDO;

@Repository
public class PrivacyDictionaryDAOImpl extends OperationalCentralAwareDAO<PrivacyDictionaryDO, String>
		implements PrivacyDictionaryDAO {

	public PrivacyDictionaryDAOImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.area118.sdocommon.dao.BaseDAO#getEntityClass()
	 */
	@Override
	public Class<PrivacyDictionaryDO> getEntityClass() {
		return PrivacyDictionaryDO.class;
	}

	@Override
	public DetachedCriteria createOperationalCentralFilter(String operationalCentral) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.area118.sdocore.security.dao.PrivacyDictionaryDAO#checkDictionary(
	 * )
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkDictionary(String pwd) {

		String hql = "select word from PrivacyDictionaryDO dictionary where instr(:password,word) > 0";
		Query query = getSession().createQuery(hql);
		query.setString("password", pwd);

		List<Object> list = query.list();

		if (!list.isEmpty()) {
			return true;
		}

		return false;
	}

}
