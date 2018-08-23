package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.UbicazioneDAO;
import it.eng.areas.ems.sdodaeservices.entity.UbicazioneDO;

@Repository public class  UbicazioneDAOImpl extends EntityDAOImpl<UbicazioneDO, String> implements UbicazioneDAO{
	
	public UbicazioneDAOImpl() {
	}

	@Override
	public Class<UbicazioneDO> getEntityClass() {
		return UbicazioneDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
