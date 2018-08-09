package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class GruppoDeepDepthRule implements FetchRule {

	public static String NAME = "DEEP";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("province", FetchMode.JOIN);
		criteria.setFetchMode("comuni", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
