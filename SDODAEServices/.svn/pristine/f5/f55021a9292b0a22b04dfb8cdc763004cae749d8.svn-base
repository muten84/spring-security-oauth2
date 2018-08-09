package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class NotificheEventoDeepDepthRule implements FetchRule {
	public static String NAME = "DEEP";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("firstResponder", FetchMode.JOIN);
		criteria.setFetchMode("event", FetchMode.JOIN);
		criteria.setFetchMode("event.interventi", FetchMode.JOIN);

	}

	@Override
	public String getName() {
		return NAME;
	}

}
