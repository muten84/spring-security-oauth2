package it.eng.areas.ems.sdodaeservices.dao.impl.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

 public class  CategoriaFrDeepDepthRule implements FetchRule {
	public static String NAME = "DEEP";

	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("utente", FetchMode.JOIN);
		criteria.setFetchMode("colore", FetchMode.JOIN);
	
		}

	
	@Override
	public String getName() {
		return NAME;
	}

}
