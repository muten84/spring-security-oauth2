package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class EventAppDepthRule implements FetchRule {
	public static String NAME = "APP";

	@Override
	public void applyRule(Criteria criteria) {

		criteria.setFetchMode("coordinate", FetchMode.JOIN);
		criteria.setFetchMode("categoriaFr", FetchMode.JOIN);

		criteria.setFetchMode("interventi", FetchMode.JOIN);
		// criteria.setFetchMode("interventi.coordinate", FetchMode.JOIN);
		criteria.setFetchMode("interventi.eseguitoDa", FetchMode.JOIN);
		criteria.setFetchMode("interventi.eseguitoDa.utente", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
