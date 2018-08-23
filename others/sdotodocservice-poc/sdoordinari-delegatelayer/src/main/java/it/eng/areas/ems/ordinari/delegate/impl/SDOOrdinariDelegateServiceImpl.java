package it.eng.areas.ems.ordinari.delegate.impl;


import it.eng.areas.ems.common.sdo.dto.CompoundDTORule;
import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.ordinari.dao.rule.WebBookingDeepDepthRule;
import it.eng.areas.ems.ordinari.delegate.SDOOrdinariDelegateService;
import it.eng.areas.ems.ordinari.entity.WebBookingDO;
import it.eng.areas.ems.ordinari.model.WebBooking;
import it.eng.areas.ems.ordinari.service.WebBookingTransactionalService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SDOOrdinariDelegateServiceImpl implements SDOOrdinariDelegateService {

	private Logger logger = LoggerFactory
			.getLogger(SDOOrdinariDelegateServiceImpl.class);

	@Autowired
	private WebBookingTransactionalService webBookingTransactionalService;
	
	
	@Autowired
	private DTOService dtoService;


	@SuppressWarnings("unused")
	@Override
	public WebBooking saveWebBooking(WebBooking webBooking) {

		try{
			
		WebBookingDO webBookingDO = (WebBookingDO) dtoService.convertObject(webBooking, WebBookingDO.class,
			new CompoundDTORule(WebBooking.class, WebBookingDO.class, WebBookingDeepDepthRule.NAME));

		WebBookingDO returnedWebBookingDo = webBookingTransactionalService.save(webBookingDO);
		WebBookingDO returnedWebBookingDoById = webBookingTransactionalService.getWebBookingById(returnedWebBookingDo.getId().toString());

//		WebBooking returnedWebBooking = (WebBooking) dtoService.convertObject(returnedWebBookingDoById, WebBooking.class,
//				new CompoundDTORule(WebBookingDO.class, WebBooking.class, WebBookingDeepDepthRule.NAME));

		return webBooking; //returnedWebBooking;
		
	} catch (Exception e) {
		logger.error("ERROR WHILE EXECUTING insertWebBooking", e);
		e.printStackTrace();
	}
	return null;
		
		
	}


	@Override
	public WebBooking getWebBookingById(String id) {
		
		try {
			WebBookingDO webBookingDo = null;
			webBookingDo = webBookingTransactionalService.getWebBookingById(id);
			WebBooking webBooking = (WebBooking) dtoService.convertObject(webBookingDo, WebBooking.class,
					new CompoundDTORule(WebBookingDO.class, WebBooking.class, WebBookingDeepDepthRule.NAME));
			return webBooking;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getWebBookingById", e);
			return null;
		}
	}



	
}