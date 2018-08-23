package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class InterventoForEventDepthRule implements FetchRule {
	public static String NAME = "FOR_EVENT";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("event", FetchMode.JOIN);
		criteria.setFetchMode("event.coordinate", FetchMode.JOIN);
		criteria.setFetchMode("event.categoriaFr", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.utente", FetchMode.JOIN);

	}

	@Override
	public String getName() {
		return NAME;
	}

}
