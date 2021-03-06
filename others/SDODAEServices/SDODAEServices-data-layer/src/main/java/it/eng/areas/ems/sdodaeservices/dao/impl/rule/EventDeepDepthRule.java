package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class EventDeepDepthRule implements FetchRule {
	public static String NAME = "DEEP";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("coordinate", FetchMode.JOIN);
		criteria.setFetchMode("categoriaFr", FetchMode.JOIN);
		criteria.setFetchMode("notifiche", FetchMode.JOIN);

	}

	@Override
	public String getName() {
		return NAME;
	}

}
