package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.ProgrammaManutenzioneDAO;
import it.eng.areas.ems.sdodaeservices.entity.ProgrammaManutenzioneDO;

@Repository public class  ProgrammaManutenzioneDAOImpl extends EntityDAOImpl<ProgrammaManutenzioneDO, String> implements ProgrammaManutenzioneDAO{
	
	public ProgrammaManutenzioneDAOImpl() {
	}

	@Override
	public Class<ProgrammaManutenzioneDO> getEntityClass() {
		return ProgrammaManutenzioneDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
