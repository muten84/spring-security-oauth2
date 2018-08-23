package it.eng.areas.ems.ordinari.dao.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

 public class  TsBookingDeepDepthRule implements FetchRule {

		public static String NAME = "DEEP";

	
	@Override
	public void applyRule(Criteria criteria) {
	criteria.setFetchMode("webBookingEquipments", FetchMode.JOIN);
		
		}


	
	@Override
	public String getName() {
		return NAME;
	}

	
}
