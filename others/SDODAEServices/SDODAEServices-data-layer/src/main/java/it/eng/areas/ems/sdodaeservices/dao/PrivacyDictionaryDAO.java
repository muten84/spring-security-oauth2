/*
 * 
 */
package it.eng.areas.ems.sdodaeservices.dao;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.PrivacyDictionaryDO;

public interface PrivacyDictionaryDAO extends EntityDAO<PrivacyDictionaryDO, String> {

	public boolean checkDictionary(String pwd);
}
