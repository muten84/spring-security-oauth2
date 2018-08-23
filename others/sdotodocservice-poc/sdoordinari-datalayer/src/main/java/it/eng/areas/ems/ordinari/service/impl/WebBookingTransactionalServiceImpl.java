/**
 * 
 */
package it.eng.areas.ems.ordinari.service.impl;


import it.eng.areas.ems.ordinari.dao.TsEquipmentDAO;
import it.eng.areas.ems.ordinari.dao.WebBookingDAO;
import it.eng.areas.ems.ordinari.dao.WebBookingEquipmentDAO;
import it.eng.areas.ems.ordinari.dao.filter.WebBookingFilterDO;
import it.eng.areas.ems.ordinari.dao.rule.TsBookingDeepDepthRule;
import it.eng.areas.ems.ordinari.dao.rule.WebBookingDeepDepthRule;
import it.eng.areas.ems.ordinari.entity.TsEquipmentDO;
import it.eng.areas.ems.ordinari.entity.WebBookingDO;
import it.eng.areas.ems.ordinari.entity.WebBookingEquipmentDO;
import it.eng.areas.ems.ordinari.service.WebBookingTransactionalService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Giuseppe Picone
 *
 */

@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class WebBookingTransactionalServiceImpl implements WebBookingTransactionalService {

	@Autowired
	private WebBookingDAO webBookingDAO;
	@Autowired
	private WebBookingEquipmentDAO webEquipmentBookingDAO;

	@Autowired
	private TsEquipmentDAO tsEquipmentDAO;

	
	@Override
	public WebBookingDO save(WebBookingDO webBooking) {
		WebBookingDO WebBookingDO = webBookingDAO.saveWebBooking(webBooking);
		return WebBookingDO;
	}

	@Override
	public WebBookingDO getWebBookingById(String id) {
			return webBookingDAO.get(WebBookingDeepDepthRule.NAME, id);



	}

	@Override
	public WebBookingDO updateWebBooking(WebBookingDO webBooking) throws Exception {
		WebBookingDO WebBookingDO = webBookingDAO.updateWebBooking(webBooking);
		return WebBookingDO;
	}

	@Override
	public List<WebBookingDO> searchWebBookingByFilter(
			WebBookingFilterDO WebBookingFilterDO) {

	return webBookingDAO.searchWebBookingByFilter(WebBookingFilterDO, WebBookingDeepDepthRule.NAME );
	
	}

	public WebBookingEquipmentDO searchWebBookingEquipmentByDescription(String description) throws Exception {
		WebBookingEquipmentDO webBookingEquipmentDO = webEquipmentBookingDAO.searchWebBookingEquipmentByDescription(description);
		return webBookingEquipmentDO;
	}

	public TsEquipmentDO searchTsEquipmentByDescription(String description) throws Exception {
		TsEquipmentDO tsEquipmentDO = tsEquipmentDAO.getTsEquipmentByDescription(description, TsBookingDeepDepthRule.NAME);
		return tsEquipmentDO;
	}

	
}
