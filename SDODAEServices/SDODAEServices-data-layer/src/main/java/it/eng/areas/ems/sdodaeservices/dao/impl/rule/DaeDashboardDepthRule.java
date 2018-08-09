package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class DaeDashboardDepthRule implements FetchRule {
	public static String NAME = "DASHBOARD";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("responsabile", FetchMode.JOIN);
		criteria.setFetchMode("programmiManutenzione", FetchMode.JOIN);
		criteria.setFetchMode("guasti", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
