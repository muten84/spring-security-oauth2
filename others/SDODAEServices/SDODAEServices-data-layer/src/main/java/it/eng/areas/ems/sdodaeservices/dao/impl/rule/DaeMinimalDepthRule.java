package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class DaeMinimalDepthRule implements FetchRule {
	public static String NAME = "MINIMAL";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("posizione", FetchMode.JOIN);
		criteria.setFetchMode("posizione.indirizzo", FetchMode.JOIN);
		criteria.setFetchMode("posizione.localita", FetchMode.JOIN);
		criteria.setFetchMode("posizione.comune", FetchMode.JOIN);
		criteria.setFetchMode("posizione.comune.provincia", FetchMode.JOIN);
		criteria.setFetchMode("posizione.gpsLocation", FetchMode.JOIN);
		criteria.setFetchMode("responsabile", FetchMode.JOIN);
		criteria.setFetchMode("disponibilita", FetchMode.JOIN);
		criteria.setFetchMode("disponibilita.disponibilitaSpecifica", FetchMode.JOIN);
		criteria.setFetchMode("disponibilita.disponibilitaSpecifica.disponibilitaGiornaliera", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
