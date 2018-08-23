package it.eng.areas.ems.sdodaeservices.dao.impl;

import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.SMTPProtocolConfigDAO;
import it.eng.areas.ems.sdodaeservices.entity.SMTPProtocolConfigDO;

@Repository
public class SMTPProtocolConfigDAOImpl extends EntityDAOImpl<SMTPProtocolConfigDO, String>
		implements SMTPProtocolConfigDAO {

	public SMTPProtocolConfigDAOImpl() {
	}

	@Override
	public Class<SMTPProtocolConfigDO> getEntityClass() {
		return SMTPProtocolConfigDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		return null;
	}

}
