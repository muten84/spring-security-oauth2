package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class UtenteDeepDepthRule implements FetchRule {
	public static String NAME = "DEEP";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("ruoli", FetchMode.JOIN);
		criteria.setFetchMode("gruppi", FetchMode.JOIN);
		criteria.setFetchMode("gruppi.province", FetchMode.JOIN);
		criteria.setFetchMode("gruppi.comuni", FetchMode.JOIN);

		criteria.setFetchMode("indirizzo", FetchMode.JOIN);
		criteria.setFetchMode("comuneResidenza", FetchMode.JOIN);
		criteria.setFetchMode("comuneResidenza.provincia", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
