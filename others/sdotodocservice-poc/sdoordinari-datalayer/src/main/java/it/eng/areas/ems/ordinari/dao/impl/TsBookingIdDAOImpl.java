package it.eng.areas.ems.ordinari.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.ordinari.dao.TsBookingIdDAO;
import it.eng.areas.ems.ordinari.dao.rule.BookingDetailRule;
import it.eng.areas.ems.ordinari.entity.TsBookingIdDO;
import it.eng.areas.ems.ordinari.entity.TsBookingIdPKDO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class TsBookingIdDAOImpl extends EntityDAOImpl<TsBookingIdDO,String> implements TsBookingIdDAO {

	public Class<TsBookingIdDO> getEntityClass() {
		return TsBookingIdDO.class;
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

	@Override
	public String generateBookingCode(String fetchRule) throws DataAccessException {
		
		TsBookingIdDO bookingID = new TsBookingIdDO();
		
		SimpleDateFormat df = new SimpleDateFormat("yy");
		String iCurrentYear = df.format(new Date());
		
		Criteria c = createCriteria();
		decorateFetchRule(getFetchRule(fetchRule), c);
		c.setProjection(Projections.rowCount());
		Long iCounter = (Long) c.uniqueResult();
		
		Long iProgressive = 1L;
		TsBookingIdPKDO bookingIDPK = null;
				
		if(iCounter != 0){
			bookingID = getTsBookingIdDO(fetchRule);
			bookingIDPK = bookingID.getId();
			if(iCurrentYear.equals(bookingIDPK.getYear()+"")){
				iProgressive = bookingIDPK.getBookingId()+1;				
			}
		}
		
		//update
		bookingIDPK.setBookingId(iProgressive);
		bookingIDPK.setYear(new Long(iCurrentYear));
		bookingID.setId(bookingIDPK);
		save(bookingID);
		
		//Create code from retrieved info 
		String code = StringUtils.leftPad(iCurrentYear,2,'0') + StringUtils.leftPad(iProgressive.toString(), 6, '0');

		return code;
	}
	
	private TsBookingIdDO getTsBookingIdDO(String fetchRule)  throws DataAccessException{
		Criteria c = createCriteria();
		decorateFetchRule(getFetchRule(fetchRule), c);
		return (TsBookingIdDO) c.uniqueResult();
	}
	
//	private int getCountBookingId(String fetchRule)  throws DataAccessException{
//		Criteria c = createCriteria();
//		decorateFetchRule(getFetchRule(fetchRule), c);
//		c.setProjection(Projections.rowCount());
//		return (Integer) c.uniqueResult();
//	}

}
