package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class DaeForFaultDepthRule implements FetchRule {
	public static String NAME = "DAE_FAULT";

	@Override
	public void applyRule(Criteria criteria) {

		criteria.setFetchMode("guasti", FetchMode.JOIN);
		criteria.setFetchMode("guasti.utente", FetchMode.JOIN);
		criteria.setFetchMode("guasti.trace", FetchMode.JOIN);
		criteria.setFetchMode("guasti.trace.utente", FetchMode.JOIN);

		criteria.setFetchMode("responsabile", FetchMode.JOIN);
		criteria.setFetchMode("posizione", FetchMode.JOIN);
		criteria.setFetchMode("posizione.indirizzo", FetchMode.JOIN);
		criteria.setFetchMode("posizione.localita", FetchMode.JOIN);
		criteria.setFetchMode("posizione.comune", FetchMode.JOIN);
		criteria.setFetchMode("posizione.comune.provincia", FetchMode.JOIN);
		criteria.setFetchMode("posizione.gpsLocation", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
