/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.to.sdoto.docservice.dao.BookingDocumentDAO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentDO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;
import it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter;
import it.esel.parsley.lang.StringUtils;

/**
 * @author Bifulco Luigi
 *
 */
@Repository
public class BookingDocumentDAOImpl extends EntityDAOImpl<BookingDocumentDO, String> implements BookingDocumentDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.area118.sdocommon.dao.BaseDAO#getEntityClass()
	 */
	@Override
	public Class<BookingDocumentDO> getEntityClass() {
		return BookingDocumentDO.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.area118.sdocommon.dao.impl.BaseDAOImpl#getFetchRule(java.lang.String)
	 */
	@Override
	protected FetchRule getFetchRule(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BookingDocumentDO> searchBookingDocument(BookingDocumentFilter filter) {
		Criteria c = createCriteria(filter);
		// long r = rowCount(c);
		// BookingDocumentDO e = (BookingDocumentDO) c.list().get(0);
		BookingDocumentState state = filter.getInState();
		BookingDocumentState currentState = filter.getCurrentState();
		BookingDocumentState excludeCurrentState = filter.getExcludeInCurrentState();
		String property = "all";

		if (state != null) {
			// cerca dalla data alla data per quello stato
			switch (state) {
			case CREATED:
				property = "creationDate";
				break;
			case CLOSED:
				property = "closeDate";
				break;
			case OPENED:
				property = "openDate";
				break;
			case SENT:
				property = "sentDate";
				break;
			default:
				property = "all";
				break;
			}
		}
		if (filter.getFromDate() != null && filter.getToDate() != null) {
			if (StringUtils.equals("all", property)) {
				c.add(Restrictions.or(Restrictions.between("creationDate", filter.getFromDate(), filter.getToDate()), //
						Restrictions.between("closeDate", filter.getFromDate(), filter.getToDate()), //
						Restrictions.between("openDate", filter.getFromDate(), filter.getToDate()), //
						Restrictions.between("sentDate", filter.getFromDate(), filter.getToDate())));
				// c.add();
				// c.add(Restrictions.between(openDate, filter.getFromDate(), filter.getToDate()));
				// c.add(Restrictions.between(sentDate, filter.getFromDate(), filter.getToDate()));
			} else {
				c.add(Restrictions.between(property, filter.getFromDate(), filter.getToDate()));
			}
		} else {
			throw new IllegalArgumentException("filter must contains at least from date AND toDate params");
		}
		if (!StringUtils.isEmpty(filter.getDocReference())) {
			c.add(Restrictions.or(Restrictions.eq("id", filter.getDocReference()), Restrictions.eqOrIsNull("bookingCode", filter.getDocReference())));
		}
		if (!StringUtils.isEmpty(filter.getParking())) {
			c.add(Restrictions.eq("parking", filter.getParking()));
		}
		if (currentState != null) {
			c.add(Restrictions.eq("state", currentState));
		}
		if (excludeCurrentState != null) {
			c.add(Restrictions.ne("state", excludeCurrentState));
		}
		return c.list();
	}

}
