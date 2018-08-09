package it.eng.areas.ems.ordinari.dao.impl;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.ordinari.dao.GuidSequenceDAO;
import it.eng.areas.ems.ordinari.dao.rule.BookingDetailRule;
import it.eng.areas.ems.ordinari.entity.GuidSequenceDO;

import java.math.BigDecimal;

import org.hibernate.SQLQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class GuidSequenceDAOImpl extends EntityDAOImpl<GuidSequenceDO, String> implements GuidSequenceDAO {

	public Class<GuidSequenceDO> getEntityClass() {
		return GuidSequenceDO.class;
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
	public String getSequence(){ 
	
		
		/*//Query query =  getSession().createNativeQuery("SELECT GUID.NEXTVAL as seq_id  from Dual");
		//SqlQuery query =  getSession().createSQLQuery("Insert into GUIDA(seq_id) Values ( GUID.NEXTVAL)");
		SQLQuery q = getSession().createSQLQuery("Insert into GUIDA(seq_id) Values ( GUID.NEXTVAL)");
		q.executeUpdate();
		Query query =  getSession().createQuery("SELECT max(seq_id) from GUIDA");
		GuidSequenceDO guidSequenceDO = (GuidSequenceDO)query.getSingleResult();
		//BigDecimal big = (BigDecimal) query.getSingleResult();
	//	String id = big.toString();
		String id = guidSequenceDO.getSeq_id();
		*/
	/*	Query query =  getSession().createNativeQuery("SELECT GUID.NEXTVAL as seq_id  from Dual");
		BigDecimal big = (BigDecimal) query.getSingleResult();
		String id = big.toString();*/
		String id= null;
	return id;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.ordinari.dao.GuidSequenceDAO#getBookingByCode(java.lang.String)
	 */

	
}
