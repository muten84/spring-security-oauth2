package it.eng.areas.ems.sdodaeservices.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;
import it.eng.areas.ems.sdodaeservices.dao.CategoriaFrDAO;
import it.eng.areas.ems.sdodaeservices.dao.ColoreDAO;
import it.eng.areas.ems.sdodaeservices.dao.FirstResponderDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.CategoriaFrDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.CategoriaFrDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.CategoriaFrFilterDO;
import it.eng.areas.ems.sdodaeservices.gis.dao.VCTDaeDAO;
import it.eng.areas.ems.sdodaeservices.service.CategoriaFrTransactionalService;

@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CategoriaFrTransactionalServiceImpl implements CategoriaFrTransactionalService {

	@Autowired
	private CategoriaFrDAO categoriaFrDAO;

	@Autowired
	private ColoreDAO coloreDAO;

	@Autowired
	private FirstResponderDAO firstResponderDAO;

	@Autowired
	private VCTDaeDAO vctDaeDAO;

	public CategoriaFrTransactionalServiceImpl() {

	}

	@PostConstruct
	public void init() {
		// TODO: all dependendecies injected, init your stateless service here
		// TODO: tutte le dipendenze sono state inettate inizializza qui il tuo
		// stateless service
	}

	@Override
	public CategoriaFrDO getCategoriaFrById(String id) {

		return categoriaFrDAO.get(CategoriaFrDeepDepthRule.NAME, id);


	}

	public long countAll() {
		long num = categoriaFrDAO.countAll();


		return num;
	}

	@Override
	public List<CategoriaFrDO> getAllCategoriaFr() {
		// TODO Auto-generated method stub
		EntityFilter filter = new EntityFilter();
		filter.setFetchRule(CategoriaFrDeepDepthRule.NAME);

		return categoriaFrDAO.getAll(filter);
	}

	@Override
	public List<CategoriaFrDO> getCategoriaFrByFilter(CategoriaFrFilterDO categoriaFrFilter) {

		categoriaFrFilter.setFetchRule(CategoriaFrDeepDepthRule.NAME);

		return categoriaFrDAO.searchCategoriaFrByFilter(categoriaFrFilter);

	}

	@Override
	public CategoriaFrDO insertCategoriaFr(CategoriaFrDO categoriaFrDO) {
		
	
		
		CategoriaFrDO returnedCategoriaFrDO = categoriaFrDAO.insertCategoriaFr(categoriaFrDO);
				
		
				
		return returnedCategoriaFrDO;
	}

	// @Override
	// public List<VCTCategoriaFrDO> getAllVctCategoriaFr() {
		// return vctCategoriaFrDAO.getAllVCTDAE();
	// }

	@Override
	public boolean deleteCategoriaFrById(String id) {
		CategoriaFrDO categoriaFrDO = getCategoriaFrById(id);
		if (categoriaFrDO != null) {
			categoriaFrDAO.delete(categoriaFrDO);
			return true;
		}
		return false;
	}


}
