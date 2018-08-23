package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class UtenteLoginDepthRule implements FetchRule {
	public static String NAME = "LOGIN";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("ruoli", FetchMode.JOIN);
		criteria.setFetchMode("gruppi", FetchMode.JOIN);
		criteria.setFetchMode("gruppi.province", FetchMode.JOIN);
		criteria.setFetchMode("gruppi.comuni", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
