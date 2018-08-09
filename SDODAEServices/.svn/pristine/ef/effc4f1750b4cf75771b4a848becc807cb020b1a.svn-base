package it.eng.areas.ems.sdodaeservices.dao;

import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.PasswordHistoryDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.PasswordHistoryFilterDO;

public interface PasswordHistoryDAO extends EntityDAO<PasswordHistoryDO, String> {

	PasswordHistoryDO searchPasswordHistory(String userId, String hashed);

	/**
	 * @param filter
	 * @return
	 */
	List<PasswordHistoryDO> searchPasswordHistoryByfilter(PasswordHistoryFilterDO filter);

	List<PasswordHistoryDO> searchPasswordHistoryByUserId(String userId);

	List<PasswordHistoryDO> searchWordInPassword(String userId);

}
