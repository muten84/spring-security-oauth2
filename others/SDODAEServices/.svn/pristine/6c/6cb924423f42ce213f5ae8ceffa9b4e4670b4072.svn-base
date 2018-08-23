package it.eng.areas.ems.sdodaeservices.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;
import it.eng.areas.ems.sdodaeservices.dao.FirstResponderDAO;
import it.eng.areas.ems.sdodaeservices.dao.MessaggioDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.MessagesDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.MessaggioDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.MessaggioFilterDO;
import it.eng.areas.ems.sdodaeservices.gis.dao.VCTDaeDAO;
import it.eng.areas.ems.sdodaeservices.service.MessagesTransactionalService;

@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class MessagesTransactionalServiceImpl implements MessagesTransactionalService {

	@Autowired
	private MessaggioDAO messaggioDAO;

	@Autowired
	private FirstResponderDAO firstResponderDAO;

	@Autowired
	private VCTDaeDAO vctDaeDAO;

	public MessagesTransactionalServiceImpl() {

	}

	@PostConstruct
	public void init() {
		// TODO: all dependendecies injected, init your stateless service here
		// TODO: tutte le dipendenze sono state inettate inizializza qui il tuo
		// stateless service
	}

	@Override
	public MessaggioDO getMessaggioById(String id) {

		return messaggioDAO.get(MessagesDeepDepthRule.NAME, id);

		// if (example == null) {
		// throw new NullPointerException("The entity does not exist");
		// }

	}

	public long countAll() {
		long num = messaggioDAO.countAll();

		// if (example == null) {
		// throw new NullPointerException("The entity does not exist");
		// }
		return num;
	}

	@Override
	public List<MessaggioDO> getAllMessaggio() {
		// TODO Auto-generated method stub
		EntityFilter filter = new EntityFilter();
		filter.setFetchRule(MessagesDeepDepthRule.NAME);

		return messaggioDAO.getAll(filter);
	}

	@Override
	public List<MessaggioDO> getMessaggioByFilter(MessaggioFilterDO messaggioFilter) {

		messaggioFilter.setFetchRule(MessagesDeepDepthRule.NAME);

		return messaggioDAO.searchMessaggioByFilter(messaggioFilter);

	}

	@Override
	public MessaggioDO insertMessaggio(MessaggioDO messaggioDO) {
		return messaggioDAO.insertMessaggio(messaggioDO);
	}

	// @Override
	// public List<VCTMessaggioDO> getAllVctMessaggio() {
	// return vctMessaggioDAO.getAllVCTDAE();
	// }

	@Override
	public boolean deleteMessaggioById(String id) {
		MessaggioDO messaggioDO = getMessaggioById(id);
		if (messaggioDO != null) {
			messaggioDO.setDeleted(1);
			return true;
		}
		return false;
	}

	@Override
	public List<FirstResponderDO> getMessageResponders(String id) {
		MessaggioDO messaggioDO = messaggioDAO.getMessaggioWithResponders(id);
		if (messaggioDO != null) {
			return new ArrayList<>(messaggioDO.getResponders());
		}
		return new ArrayList<>();
	}

}
