package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.ProvinciaDAO;
import it.eng.areas.ems.sdodaeservices.entity.ProvinciaDO;

@Repository
public class ProvinciaDAOImpl extends EntityDAOImpl<ProvinciaDO, String> implements ProvinciaDAO {

	public ProvinciaDAOImpl() {
	}

	@Override
	public Class<ProvinciaDO> getEntityClass() {
		return ProvinciaDO.class;
	}

	@Override
	public List<ProvinciaDO> getAllSorted() {
		Criteria criteria = createCriteria();
		criteria.addOrder(Order.desc("nomeProvincia"));
		return criteria.list();
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}

}
