package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.InterventoCoordDAO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoCoordDO;

@Repository
public class InterventoCoordDAOImpl extends EntityDAOImpl<InterventoCoordDO, String> implements InterventoCoordDAO {

	public InterventoCoordDAOImpl() {
	}

	@Override
	public Class<InterventoCoordDO> getEntityClass() {
		return InterventoCoordDO.class;
	}

	protected FetchRule getFetchRule(String name) {

		return null;
	}

	@Override
	public List<InterventoCoordDO> getFirstCoordinateOfIntervention(String id) {
		Criteria criteria = createCriteria();

		DetachedCriteria subQ = DetachedCriteria.forClass(InterventoCoordDO.class, "sub");

		subQ.add(Restrictions.eq("intervento.id", id));

		subQ.setProjection(Projections.min("dataCreazione"));

		criteria.add(Restrictions.eq("intervento.id", id));
		criteria.add(Subqueries.propertyLe("dataCreazione", subQ));

		return criteria.list();
	}

}
