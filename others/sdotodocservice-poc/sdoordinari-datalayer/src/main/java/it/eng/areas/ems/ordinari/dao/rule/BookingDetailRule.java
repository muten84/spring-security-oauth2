/**
 * 
 */
package it.eng.areas.ems.ordinari.dao.rule;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;

import it.eng.area118.sdocommon.dao.impl.FetchRule;

/**
 * @author Bifulco Luigi
 *
 */
public class BookingDetailRule implements FetchRule {

	public static String NAME = BookingDetailRule.class.getName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.area118.sdocommon.dao.impl.FetchRule#getName()
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.area118.sdocommon.dao.impl.FetchRule#applyRule(org.hibernate.Criteria)
	 */
	@Override
	public void applyRule(Criteria criteria) {
		criteria.setFetchMode("bookingPatients", FetchMode.JOIN);
		criteria.setFetchMode("bookingEquipments", FetchMode.JOIN);
		criteria.setFetchMode("bookingDpis", FetchMode.JOIN);

	}

}
