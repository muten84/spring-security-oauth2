package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class EventSearchDepthRule implements FetchRule {
	public static String NAME = "SEARCH";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("coordinate", FetchMode.JOIN);

		criteria.setFetchMode("interventi", FetchMode.JOIN);
		criteria.setFetchMode("categoriaFr", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
