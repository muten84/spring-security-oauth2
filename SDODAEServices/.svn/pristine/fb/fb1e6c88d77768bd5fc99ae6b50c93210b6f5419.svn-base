package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.InterventoDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.InterventoBareDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.InterventoDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.InterventoForEventDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.InterventoDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.InterventoFilterDO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class InterventoDAOImpl extends EntityDAOImpl<InterventoDO, String> implements InterventoDAO {

	public InterventoDAOImpl() {
	}

	@Override
	public Class<InterventoDO> getEntityClass() {
		return InterventoDO.class;
	}

	protected FetchRule getFetchRule(String name) {
		if (name.equals(InterventoDeepDepthRule.NAME)) {

			return new InterventoDeepDepthRule();
		}

		if (name.equals(InterventoForEventDepthRule.NAME)) {

			return new InterventoForEventDepthRule();
		}

		if (name.equals(InterventoBareDepthRule.NAME)) {

			return new InterventoBareDepthRule();
		}

		return null;
	}

	@Override
	public List<InterventoDO> searchInterventoByFilter(InterventoFilterDO filter) {
		Criteria criteria = createCriteria(filter);

		if (!StringUtils.isEmpty(filter.getFetchRule())) {
			criteria.setFetchMode(filter.getFetchRule(), FetchMode.JOIN);
		}

		criteria.createAlias("event", "evt");
		if (!StringUtils.isEmpty(filter.getEventId())) {
			criteria.add(Restrictions.eq("evt.id", filter.getEventId()));
		}

		if (!StringUtils.isEmpty(filter.getCoRiferimento())) {
			criteria.add(Restrictions.eq("evt.coRiferimento", filter.getCoRiferimento()));
		}

		if (!StringUtils.isEmpty(filter.getCartellinoEvento())) {
			criteria.add(Restrictions.eq("evt.cartellino", filter.getCartellinoEvento()));
		}

		if (!StringUtils.isEmpty(filter.getFirstResponder())) {
			criteria.add(Restrictions.eq("eseguitoDa.id", filter.getFirstResponder()));
		}

		if (filter.getAccepted() != null && filter.getAccepted()) {
			criteria.add(Restrictions.isNotNull("dataAccettazione"));
		}

		if (filter.getNotClosed() != null && filter.getNotClosed() == true) {
			criteria.add(Restrictions.and(Restrictions.isNotNull("dataAccettazione"),
					Restrictions.isNull("dataRifiuto"), Restrictions.isNull("dataChiusura"),
					Restrictions.isNull("dataAnnullamento"), Restrictions.eq("evt.closed", false)));
			// criteria.add(Restrictions.eq("evt.closed",false));
		}

		if (filter.getNotRejected() != null && filter.getNotRejected() == true) {
			criteria.add(Restrictions.and(Restrictions.isNotNull("dataAccettazione"), //
					Restrictions.isNull("dataRifiuto"), Restrictions.isNull("dataAnnullamento"),
					Restrictions.eq("evt.closed", false)));
		}

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public InterventoDO insertIntervento(InterventoDO interventoDO) {

		return this.save(interventoDO);
	}

}
