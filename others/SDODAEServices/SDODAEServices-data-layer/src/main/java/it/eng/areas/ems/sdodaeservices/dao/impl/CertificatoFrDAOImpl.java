package it.eng.areas.ems.sdodaeservices.dao.impl;


import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.CertificatoFrDAO;
import it.eng.areas.ems.sdodaeservices.entity.CertificatoFrDO;


@Repository public class  CertificatoFrDAOImpl extends EntityDAOImpl<CertificatoFrDO, String> implements CertificatoFrDAO{
	
	public CertificatoFrDAOImpl() {
	}

	@Override
	public Class<CertificatoFrDO> getEntityClass() {
		return CertificatoFrDO.class;
	}
	
	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}
	
}
