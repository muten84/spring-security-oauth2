package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class DaeFaultDeepDepthRule implements FetchRule {
	public static String NAME = "DEEP";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("dae", FetchMode.JOIN);
		criteria.setFetchMode("utente", FetchMode.JOIN);

		criteria.setFetchMode("trace", FetchMode.JOIN);
		criteria.setFetchMode("trace.utente", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
