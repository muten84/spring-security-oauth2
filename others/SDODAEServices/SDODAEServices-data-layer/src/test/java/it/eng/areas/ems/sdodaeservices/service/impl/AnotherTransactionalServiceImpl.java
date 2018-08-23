/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.areas.ems.sdodaeservices.dao.DaeDAO;
import it.eng.areas.ems.sdodaeservices.entity.DaeDO;
import it.eng.areas.ems.sdodaeservices.service.AnotherTransactionalService;

@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class AnotherTransactionalServiceImpl implements AnotherTransactionalService {

	@Autowired
	private DaeDAO daeDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.ems.common.sdo.service.AnotherTransactionalService#saveDae(it.
	 * eng.areas.ems.common.sdo.entity. DaeDO)
	 */
	@Override
	public DaeDO saveOrUpdateDae(DaeDO dae) {
		DaeDO dbDae = daeDAO.get(dae.getId());
		if (dbDae != null) {
			return daeDAO.update(dae);
		}
		return daeDAO.save(dae);
	}

	@Override
	public DaeDO update(DaeDO dae) {
		return daeDAO.update(dae);
	}

}
