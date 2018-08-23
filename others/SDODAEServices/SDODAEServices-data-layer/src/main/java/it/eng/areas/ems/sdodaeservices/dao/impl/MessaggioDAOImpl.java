package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.MessaggioDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.MessagesDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.MessaggioDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.MessaggioFilterDO;

@Repository
public class MessaggioDAOImpl extends EntityDAOImpl<MessaggioDO, String> implements MessaggioDAO {

	public MessaggioDAOImpl() {
	}

	@Override
	public Class<MessaggioDO> getEntityClass() {
		return MessaggioDO.class;
	}

	protected FetchRule getFetchRule(String name) {
		if (name.equals(MessagesDeepDepthRule.NAME)) {

			return new MessagesDeepDepthRule();
		}

		return null;
	}

	public List<MessaggioDO> searchMessaggioByFilter(MessaggioFilterDO filter) {
		return searchMessaggioByFilter(filter, null);
	}

	public List<MessaggioDO> searchMessaggioByFilter(MessaggioFilterDO filter, String fetchRule) {
		Criteria criteria = createCriteria(filter);

		if (fetchRule != null) {
			criteria.setFetchMode(fetchRule, FetchMode.JOIN);
		}

		if (filter.getDa() != null) {
			criteria.add(Restrictions.ge("invio", filter.getDa()));
		}

		if (filter.getA() != null) {
			criteria.add(Restrictions.le("invio", filter.getA()));
		}

		criteria.add(Restrictions.eq("deleted", 0));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("invio"));

		return criteria.list();
	}

	@Override
	public MessaggioDO insertMessaggio(MessaggioDO messaggioDO) {
		return this.save(messaggioDO);
	}

	@Override
	public MessaggioDO getMessaggioWithResponders(String id) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eqOrIsNull("id", id));
		criteria.setFetchMode("responders", FetchMode.JOIN);
		criteria.setFetchMode("responders.utente", FetchMode.JOIN);
		criteria.add(Restrictions.eq("deleted", 0));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (MessaggioDO) criteria.uniqueResult();
	}

}
