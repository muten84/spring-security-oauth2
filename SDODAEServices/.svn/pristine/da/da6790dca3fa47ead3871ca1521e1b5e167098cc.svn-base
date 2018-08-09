package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class FirstResponderActivationRule implements FetchRule {
	public static String NAME = "ACTIVATION";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("utente", FetchMode.JOIN);
		criteria.setFetchMode("dispositivo", FetchMode.JOIN);
		criteria.setFetchMode("lastPosition", FetchMode.JOIN);
		criteria.setFetchMode("lastPosition.gpsLocation", FetchMode.JOIN);
		criteria.setFetchMode("comuniCompetenza", FetchMode.JOIN);
		criteria.setFetchMode("comuniCompetenza.provincia", FetchMode.JOIN);

		criteria.setFetchMode("categoriaFr", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
