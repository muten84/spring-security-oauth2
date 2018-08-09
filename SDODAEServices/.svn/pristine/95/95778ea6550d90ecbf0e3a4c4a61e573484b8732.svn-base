package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class UtentePasswordDepthRule implements FetchRule {
	public static String NAME = "PASSWORD";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("passwordHistories", FetchMode.JOIN);

	}

	@Override
	public String getName() {
		return NAME;
	}

}
