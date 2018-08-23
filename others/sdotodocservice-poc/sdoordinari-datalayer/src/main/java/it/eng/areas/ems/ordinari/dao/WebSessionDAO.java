package it.eng.areas.ems.ordinari.dao;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.ordinari.entity.WebSessionDO;

public interface WebSessionDAO extends EntityDAO<WebSessionDO, String> {

	public String getUserSession(String userId);

	public WebSessionDO getSession(String userId);
}
