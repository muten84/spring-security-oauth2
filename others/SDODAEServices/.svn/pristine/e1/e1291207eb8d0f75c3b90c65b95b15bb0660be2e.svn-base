package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.TipoStrutturaDAO;
import it.eng.areas.ems.sdodaeservices.entity.TipoStrutturaDO;

@Repository
public class TipoStrutturaDAOImpl extends EntityDAOImpl<TipoStrutturaDO, String> implements TipoStrutturaDAO {

	public TipoStrutturaDAOImpl() {
	}

	@Override
	public Class<TipoStrutturaDO> getEntityClass() {
		return TipoStrutturaDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}

	@Override
	public List<TipoStrutturaDO> getAllSorted() {
		Criteria criteria = createCriteria();

		criteria.addOrder(Order.desc("descrizione"));

		return criteria.list();
	}

}
