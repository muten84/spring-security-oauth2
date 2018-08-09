package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class DaeImageRule implements FetchRule {
	public static String NAME = "IMAGE";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("immagine", FetchMode.JOIN);
		criteria.setFetchMode("creatoDA", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
