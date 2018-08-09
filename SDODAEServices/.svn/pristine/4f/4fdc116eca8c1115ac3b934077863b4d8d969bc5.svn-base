package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class DaeAppMobileDepthRule implements FetchRule {
	public static String NAME = "APP_MOBILE";

	@Override
	public void applyRule(Criteria criteria) {
		// criteria.setFetchMode("tipologiaStruttura", FetchMode.JOIN);
		// criteria.setFetchMode("responsabile", FetchMode.JOIN);
		// criteria.setFetchMode("responsabile.comuneResidenza",
		// FetchMode.JOIN);
		// criteria.setFetchMode("responsabile.comuneResidenza.provincia",
		// FetchMode.JOIN);
		criteria.setFetchMode("posizione", FetchMode.JOIN);
		criteria.setFetchMode("posizione.indirizzo", FetchMode.JOIN);
		criteria.setFetchMode("posizione.localita", FetchMode.JOIN);
		criteria.setFetchMode("posizione.comune", FetchMode.JOIN);
		criteria.setFetchMode("posizione.comune.provincia", FetchMode.JOIN);
		criteria.setFetchMode("posizione.gpsLocation", FetchMode.JOIN);
		criteria.setFetchMode("currentStato", FetchMode.JOIN);

		// criteria.setFetchMode("programmiManutenzione", FetchMode.JOIN);
		// criteria.setFetchMode("programmiManutenzione.responsabile",
		// FetchMode.JOIN);
		// criteria.setFetchMode("programmiManutenzione.responsabile.comune",
		// FetchMode.JOIN);
		// criteria.setFetchMode("programmiManutenzione.responsabile.comune.provincia",
		// FetchMode.JOIN);
		criteria.setFetchMode("statoDAE", FetchMode.JOIN);
		// criteria.setFetchMode("creatoDA", FetchMode.JOIN);
		// criteria.setFetchMode("currentStato", FetchMode.JOIN);
		// criteria.setFetchMode("certificatoDae", FetchMode.JOIN);
		// criteria.setFetchMode("certificatoDae.immagine", FetchMode.JOIN);
		criteria.setFetchMode("disponibilita", FetchMode.JOIN);
		// criteria.setFetchMode("disponibilita.disponibilitaEccezioni",
		// FetchMode.JOIN);
		criteria.setFetchMode("disponibilita.disponibilitaSpecifica", FetchMode.JOIN);
		criteria.setFetchMode("disponibilita.disponibilitaSpecifica.disponibilitaGiornaliera", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
