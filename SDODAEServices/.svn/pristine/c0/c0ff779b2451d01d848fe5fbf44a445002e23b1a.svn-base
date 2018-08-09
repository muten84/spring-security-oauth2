package it.eng.areas.ems.sdodaeservices.delegate.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.common.sdo.dto.CompoundDTORule;
import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.MessagesDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.MessagesDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.Messaggio;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.MessaggioFilter;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.MessaggioDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.MessaggioFilterDO;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTMessaggioDO;
import it.eng.areas.ems.sdodaeservices.service.MessagesTransactionalService;

@Component
public class MessagesDelegateServiceImpl implements MessagesDelegateService {

	private Logger logger = LoggerFactory.getLogger(MessagesDelegateServiceImpl.class);

	@Autowired
	private MessagesTransactionalService messagesService;

	@Autowired
	private DTOService dtoService;

	@Override
	public long countAll() {
		// TODO Auto-generated method stub

		return messagesService.countAll();
	}

	@Override
	public List<Messaggio> getAllMessaggio() {
		try {
			List<MessaggioDO> messaggioDoList = messagesService.getAllMessaggio();
			List<Messaggio> messaggioList = (List<Messaggio>) dtoService.convertCollection(messaggioDoList, Messaggio.class, new CompoundDTORule(MessaggioDO.class, Messaggio.class, MessagesDeepDepthRule.NAME));
			return messaggioList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllMessaggio", e);
			return null;
		}

	}

	@Override
	public Messaggio getMessaggioById(String id) {
		try {
			MessaggioDO messaggioDo = null;

			messaggioDo = messagesService.getMessaggioById(id);

			Messaggio messaggio = (Messaggio) dtoService.convertObject(messaggioDo, Messaggio.class, new CompoundDTORule(MessaggioDO.class, Messaggio.class, MessagesDeepDepthRule.NAME));
			return messaggio;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getMessaggioById", e);
			return null;
		}

	}

	@Override
	public List<Messaggio> getMessaggioByFilter(MessaggioFilter messaggioFilter) {
		try {
			List<MessaggioDO> messaggioDoList = null;

			MessaggioFilterDO messaggioFilterDO = null;

			messaggioFilterDO = dtoService.convertObject(messaggioFilter, MessaggioFilterDO.class);

			messaggioDoList = messagesService.getMessaggioByFilter(messaggioFilterDO);

			List<Messaggio> messaggioList = (List<Messaggio>) dtoService.convertCollection(messaggioDoList, Messaggio.class, new CompoundDTORule(MessaggioDO.class, Messaggio.class, MessagesDeepDepthRule.NAME));

			return messaggioList;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getMessaggioByFilter", e);
			return null;
		}
	}

	@Override
	public Messaggio insertMessaggio(Messaggio messaggio) {

		try {
			MessaggioDO messaggioDO = (MessaggioDO) dtoService.convertObject(messaggio, MessaggioDO.class, new CompoundDTORule(Messaggio.class, MessaggioDO.class, MessagesDeepDepthRule.NAME));

			MessaggioDO returnedMessaggioDo = messagesService.insertMessaggio(messaggioDO);
			returnedMessaggioDo = messagesService.getMessaggioById(returnedMessaggioDo.getId());

			Messaggio returnedMessaggio = (Messaggio) dtoService.convertObject(returnedMessaggioDo, Messaggio.class, new CompoundDTORule(MessaggioDO.class, Messaggio.class, MessagesDeepDepthRule.NAME));

			return returnedMessaggio;
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING insertMessaggio", e);
			return null;
		}
	}

	/*
	 * @Override public List<VCTMessaggioDO> getAllVctMessaggio() { try {
	 * ContextHolder.setDataSourceType(DataSourceType.GIS); return messagesService.getAllVctMessaggio(); } finally {
	 * ContextHolder.clearDataSourceType(); } }
	 */

	@Override
	public boolean deleteMessaggioById(String id) {
		try {
			return messagesService.deleteMessaggioById(id);
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllVctDae", e);
			return false;
		}
	}

	@Override
	public List<FirstResponder> getMessageResponders(String id){
		List<FirstResponderDO> responders = messagesService.getMessageResponders(id);
		/*FirstResponderDO_FirstResponder_BASIC*/
		return (List<FirstResponder>) dtoService.convertCollection(responders, FirstResponder.class, new CompoundDTORule(FirstResponderDO.class, FirstResponder.class, "BASIC"));
	}

}
