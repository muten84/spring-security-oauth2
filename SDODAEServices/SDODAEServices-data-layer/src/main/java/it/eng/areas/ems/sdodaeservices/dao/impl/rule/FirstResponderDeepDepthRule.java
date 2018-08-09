package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class FirstResponderDeepDepthRule implements FetchRule {
	public static String NAME = "DEEP";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("utente", FetchMode.JOIN);
		criteria.setFetchMode("utente.comuneResidenza", FetchMode.JOIN);
		criteria.setFetchMode("dispositivo", FetchMode.JOIN);
		criteria.setFetchMode("utente.indirizzo", FetchMode.JOIN);
		criteria.setFetchMode("utente.ruoli", FetchMode.JOIN);
		criteria.setFetchMode("utente.comuneResidenza.provincia", FetchMode.JOIN);
		criteria.setFetchMode("certificatoFr", FetchMode.JOIN);

		criteria.setFetchMode("utente.gruppi", FetchMode.JOIN);
		criteria.setFetchMode("utente.gruppi.province", FetchMode.JOIN);
		criteria.setFetchMode("utente.gruppi.comuni", FetchMode.JOIN);

		criteria.setFetchMode("profiloSanitario", FetchMode.JOIN);
		criteria.setFetchMode("medicoFr", FetchMode.JOIN);
		criteria.setFetchMode("professione", FetchMode.JOIN);

		criteria.setFetchMode("lastPosition", FetchMode.JOIN);
		criteria.setFetchMode("lastPosition.gpsLocation", FetchMode.JOIN);
		criteria.setFetchMode("lastPosition.indirizzo", FetchMode.JOIN);
		criteria.setFetchMode("lastPosition.comune", FetchMode.JOIN);
		criteria.setFetchMode("lastPosition.comune.provincia", FetchMode.JOIN);

		criteria.setFetchMode("categoriaFr", FetchMode.JOIN);
		criteria.setFetchMode("categoriaFr.utente", FetchMode.JOIN);
		criteria.setFetchMode("categoriaFr.colore", FetchMode.JOIN);

		criteria.setFetchMode("questionarioFirstResponder", FetchMode.JOIN);
		criteria.setFetchMode("questionarioFirstResponder.rispostaQuestionarioFirstResponder", FetchMode.JOIN);
		criteria.setFetchMode("questionarioFirstResponder.rispostaQuestionarioFirstResponder.domandaQuestionario",
				FetchMode.JOIN);
		criteria.setFetchMode(
				"questionarioFirstResponder.rispostaQuestionarioFirstResponder.domandaQuestionario.questionario",
				FetchMode.JOIN);

		criteria.setFetchMode("comuniCompetenza", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
