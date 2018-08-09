package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class InterventoDeepDepthRule implements FetchRule {
	public static String NAME = "DEEP";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("event", FetchMode.JOIN);
		criteria.setFetchMode("event.coordinate", FetchMode.JOIN);
		criteria.setFetchMode("event.categoriaFr", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa", FetchMode.JOIN);

		criteria.setFetchMode("eseguitoDa.utente", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.utente.comuneResidenza", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.comuniCompetenza", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.dispositivo", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.utente.indirizzo", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.utente.ruoli", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.utente.comuneResidenza.provincia", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.certificatoFr", FetchMode.JOIN);

		criteria.setFetchMode("eseguitoDa.utente.gruppi", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.utente.gruppi.province", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.utente.gruppi.comuni", FetchMode.JOIN);

		criteria.setFetchMode("eseguitoDa.profiloSanitario", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.professione", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.medicoFr", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.lastPosition", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.lastPosition.gpsLocation", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.lastPosition.indirizzo", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.lastPosition.comune", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.lastPosition.comune.provincia", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.categoriaFr", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.categoriaFr.utente", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.categoriaFr.colore", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.questionarioFirstResponder", FetchMode.JOIN);
		criteria.setFetchMode("eseguitoDa.questionarioFirstResponder.rispostaQuestionarioFirstResponder",
				FetchMode.JOIN);
		criteria.setFetchMode(
				"eseguitoDa.questionarioFirstResponder.rispostaQuestionarioFirstResponder.domandaQuestionario",
				FetchMode.JOIN);
		criteria.setFetchMode(
				"eseguitoDa.questionarioFirstResponder.rispostaQuestionarioFirstResponder.domandaQuestionario.questionario",
				FetchMode.JOIN);

	}

	@Override
	public String getName() {
		return NAME;
	}

}
