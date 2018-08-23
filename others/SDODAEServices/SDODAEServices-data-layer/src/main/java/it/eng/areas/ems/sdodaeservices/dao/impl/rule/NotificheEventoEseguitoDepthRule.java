package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

public class NotificheEventoEseguitoDepthRule implements FetchRule {
	public static String NAME = "ESEGUITO";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("firstResponder", FetchMode.JOIN);
		criteria.setFetchMode("firstResponder.utente", FetchMode.JOIN);
		criteria.setFetchMode("firstResponder.categoriaFr", FetchMode.JOIN);
	}

	@Override
	public String getName() {
		return NAME;
	}

}
