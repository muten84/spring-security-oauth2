package it.eng.areas.ems.sdodaeservices.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;
import it.eng.areas.ems.sdodaeservices.dao.GruppoDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.GruppoDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.GruppoDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.GruppoFilterDO;
import it.eng.areas.ems.sdodaeservices.service.GruppoTransactionalService;

@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class GruppoTransactionalServiceImpl implements GruppoTransactionalService {

	@Autowired
	private GruppoDAO gruppoDAO;

	public GruppoTransactionalServiceImpl() {

	}

	@PostConstruct
	public void init() {
		// TODO: all dependendecies injected, init your stateless service here
		// TODO: tutte le dipendenze sono state inettate inizializza qui il tuo
		// stateless service
	}

	@Override
	public GruppoDO getGruppoById(String id) {

		return gruppoDAO.get(GruppoDeepDepthRule.NAME, id);

		// if (example == null) {
		// throw new NullPointerException("The entity does not exist");
		// }

	}

	public long countAll() {
		long num = gruppoDAO.countAll();

		// if (example == null) {
		// throw new NullPointerException("The entity does not exist");
		// }
		return num;
	}

	@Override
	public List<GruppoDO> getAllGruppo() {
		// TODO Auto-generated method stub
		EntityFilter filter = new EntityFilter();
		filter.setFetchRule(GruppoDeepDepthRule.NAME);

		return gruppoDAO.getAll(filter);
	}

	@Override
	public List<GruppoDO> getGruppoByFilter(GruppoFilterDO gruppoFilter) {

		gruppoFilter.setFetchRule(GruppoDeepDepthRule.NAME);

		return gruppoDAO.searchGruppoByFilter(gruppoFilter);

	}

	@Override
	public GruppoDO insertGruppo(GruppoDO gruppoDO) {
		return gruppoDAO.insertGruppo(gruppoDO);
	}

	// @Override
	// public List<VCTGruppoDO> getAllVctGruppo() {
	// return vctGruppoDAO.getAllVCTDAE();
	// }

	@Override
	public boolean deleteGruppoById(String id) {
		GruppoDO gruppoDO = getGruppoById(id);
		if (gruppoDO != null) {
			gruppoDAO.delete(gruppoDO);
			return true;
		}
		return false;
	}

}
