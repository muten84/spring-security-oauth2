/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.to.sdoto.docservice.dao.DepartmentAdresseeDAO;
import it.eng.areas.to.sdoto.docservice.entity.DepartmentAdresseeDO;
import it.eng.areas.to.sdoto.docservice.entity.DepartmentAdresseePk;
import it.eng.areas.to.sdoto.docservice.entity.filter.DepartmentAdresseeFilter;
import it.esel.parsley.lang.StringUtils;

/**
 * @author Bifulco Luigi
 *
 */
@Repository
public class DepartmentAddresseeDAOImpl extends EntityDAOImpl<DepartmentAdresseeDO, DepartmentAdresseePk> implements DepartmentAdresseeDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.area118.sdocommon.dao.BaseDAO#getEntityClass()
	 */
	@Override
	public Class<DepartmentAdresseeDO> getEntityClass() {
		return DepartmentAdresseeDO.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.dao.DepartmentAdresseeDAO#searchDepartmentAddresses(it.eng.areas.to.sdoto.
	 * docservice.entity.filter.DepartmentAdresseeFilter)
	 */
	@Override
	public List<DepartmentAdresseeDO> searchDepartmentAddresses(DepartmentAdresseeFilter filter) {
		Criteria c = createCriteria(filter);
		if (!StringUtils.isEmpty(filter.getDepartment())) {
			if (filter.isExactDepartmentMatch()) {
				c.add(Restrictions.or(Restrictions.eq("description", filter.getDepartment()), Restrictions.eqOrIsNull("depId", filter.getDepartment())));
			} else {
				c.add(Restrictions.ilike("description", "%" + filter.getDepartment() + "%"));
			}
		}
		return c.list();
	}

	@Override
	public DepartmentAdresseeDO getUserDepartment(String userId) {
		Criteria c = createCriteria();
		c.add(Restrictions.eq("userId", userId));
		return (DepartmentAdresseeDO) c.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.area118.sdocommon.dao.impl.BaseDAOImpl#getFetchRule(java.lang.String)
	 */
	@Override
	protected FetchRule getFetchRule(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
