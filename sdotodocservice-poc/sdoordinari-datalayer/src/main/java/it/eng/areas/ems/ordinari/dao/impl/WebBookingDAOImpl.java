package it.eng.areas.ems.ordinari.dao.impl;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.ordinari.dao.WebBookingDAO;
import it.eng.areas.ems.ordinari.dao.filter.WebBookingFilterDO;
import it.eng.areas.ems.ordinari.dao.rule.WebBookingDeepDepthRule;
import it.eng.areas.ems.ordinari.entity.WebBookingDO;
import it.eng.areas.ems.ordinari.entity.WebBookingEquipmentDO;
import it.esel.parsley.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class WebBookingDAOImpl extends EntityDAOImpl<WebBookingDO,String> implements WebBookingDAO {

	/*@Autowired
	GuidSequenceDAO guidSequenceDAO;
	*/
	public Class<WebBookingDO> getEntityClass() {
		return WebBookingDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		if (name.equals(WebBookingDeepDepthRule.NAME)) {

			return new WebBookingDeepDepthRule();
		}

		return null;
		
	}
	@Override
	public WebBookingDO saveWebBooking(WebBookingDO webBooking) {

		String id = "";
		if(webBooking.getCreationDate() == null){
			webBooking.setCreationDate( new Date());
		}
		
		/*if(webBooking.getId() != null){
		id = webBooking.getId();	
		WebBookingDO webBookingOnDb = get(id); 
		
		} else {
	*/
		Query query =  getSession().createNativeQuery("SELECT GUID.NEXTVAL as id  from Dual");
		BigDecimal big = (BigDecimal) query.getSingleResult();
		id = big.toString();
//		}
//	String id = guidSequenceDAO.getSequence();
	
	
		webBooking.setId(id);
		if (webBooking.getWebBookingEquipments() != null) {
			for (WebBookingEquipmentDO webBookingEquipment : webBooking.getWebBookingEquipments()) {
				webBookingEquipment.setWebBooking(webBooking);
				webBookingEquipment.setId(id);
			}
		}
		
		webBooking = super.save(webBooking);
		return webBooking;
	}

	@Override
	public WebBookingDO getById(String id) {
		return super.get(id);
		
	}

	@Override
	public WebBookingDO updateWebBooking(WebBookingDO webBooking) throws Exception {
		
		
		WebBookingFilterDO webBookingFilterDO = new WebBookingFilterDO();
		webBookingFilterDO.setIdExternalRef(webBooking.getIdExternalRef());
		
		List<WebBookingDO> webBookingOnDbL = searchWebBookingByFilter(webBookingFilterDO, WebBookingDeepDepthRule.NAME);
		WebBookingDO webBookingOnDb = null;
		if (webBookingOnDbL.size() == 1){
			
			webBookingOnDb = webBookingOnDbL.get(0);
			
			//Verifico se webBooking is Not Updatable e lancio eccezione e errore sync in questo caso
			if(webBookingOnDb.getStatus().equals("NU"))
				throw new Exception("Trasporto non modificabile perchè in gestione ");
			
			if (webBooking.getNote() != null) {
				webBookingOnDb.setNote(webBooking.getNote());
			}
		
			if (webBooking.getPatientCompanion()!= null) {
				webBookingOnDb.setPatientCompanion(webBooking.getPatientCompanion());
			}
			if (webBooking.getWebBookingEquipments()  != null) {
				webBookingOnDb.setWebBookingEquipments(webBooking.getWebBookingEquipments());
			}
			if (webBooking.getTransportDate() != null) {
				webBookingOnDb.setTransportDate(webBooking.getTransportDate());
			}
		
		}	
		else if (webBookingOnDbL.size() > 1) {
			throw new Exception("Trovati piu record con stesso ref esterno");
		} else {
			throw new Exception("Non trovato alcuna prenotazione per questo ref esterno");
		}
		webBookingOnDb = super.save(webBookingOnDb);
		return webBookingOnDb;
	}

	@Override
	public List<WebBookingDO> searchWebBookingByFilter(
			WebBookingFilterDO webBookingFilterDO, String fetchRule) {
		
	Criteria criteria = createCriteria(webBookingFilterDO, "super");

		
		if (fetchRule == null) {
			criteria.setFetchMode(fetchRule, FetchMode.JOIN);
		}
		
		if (webBookingFilterDO != null) {
			if (!StringUtils.isEmpty(webBookingFilterDO.getId())) {
				criteria.add(Restrictions.eq("id", webBookingFilterDO.getId()));
			}
			
			if (!StringUtils.isEmpty(webBookingFilterDO.getIdExternalRef())) {
				criteria.add(Restrictions.eq("idExternalRef", webBookingFilterDO.getIdExternalRef()));
			}
			

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return (List<WebBookingDO>) criteria.list();
		} else {
			return null;
		}

	}
		
}
	

