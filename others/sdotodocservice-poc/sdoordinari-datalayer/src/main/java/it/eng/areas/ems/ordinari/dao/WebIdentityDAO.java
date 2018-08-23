package it.eng.areas.ems.ordinari.dao;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.ordinari.entity.WebIdentityDO;

public interface WebIdentityDAO extends EntityDAO<WebIdentityDO, String> {

	public WebIdentityDO getIdentityByUsername(String username);
}
