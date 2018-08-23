package it.eng.areas.ems.ordinari.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.ordinari.dao.ToBookingDAO;
import it.eng.areas.ems.ordinari.dao.rule.BookingDetailRule;
import it.eng.areas.ems.ordinari.entity.ToBookingDO;

@Repository
public class ToBookingDAOImpl extends EntityDAOImpl<ToBookingDO, String> implements ToBookingDAO {

	public Class<ToBookingDO> getEntityClass() {
		return ToBookingDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		if (name == null) {
			name = "";
		}
		if (name.equals(BookingDetailRule.NAME)) {
			return new BookingDetailRule();
		}
		return null;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.ordinari.dao.ToBookingDAO#getBookingByCode(java.lang.String)
	 */

	public ToBookingDO getBookingByCode(String bookingCode, String fetchRule) throws DataAccessException {
		Criteria c = createCriteria();
		decorateFetchRule(getFetchRule(fetchRule), c);
		c.add(Restrictions.eqOrIsNull("code", bookingCode));
		return (ToBookingDO) c.uniqueResult();
	}

}
