package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class FirstResponderCertificatoImageRule implements FetchRule {
	public static String NAME = "CERTIFICATO_IMAGE";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("certificatoFr", FetchMode.JOIN);
		criteria.setFetchMode("certificatoFr.immagine", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
