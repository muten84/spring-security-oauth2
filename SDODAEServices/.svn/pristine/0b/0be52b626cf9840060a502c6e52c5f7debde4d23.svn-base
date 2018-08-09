package it.eng.areas.ems.sdodaeservices.dao;

import java.util.Calendar;
import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.bean.DaeNumbers;
import it.eng.areas.ems.sdodaeservices.bean.FirstResponderSubscription;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.FirstResponderFilterDO;

public interface FirstResponderDAO extends EntityDAO<FirstResponderDO, String> {

	public FirstResponderDO insertFirstResponder(FirstResponderDO daeDO);

	public List<FirstResponderDO> searchFirstResponderByFilter(FirstResponderFilterDO firstResponderFilter);

	public FirstResponderDO getFRByLogonAndPasswordHash(String logon, String pwdHash);

	public List<FirstResponderDO> searchFirstResponderByFilter(FirstResponderFilterDO filter, String fetchRule);

	/**
	 * @param year
	 * @return
	 */
	public List<FirstResponderSubscription> countFirstResponderSubscriptionPerMonth(Calendar from, Calendar to);

	public FirstResponderDO getFirstResponderByUserId(String fetchRule, String userId);

	public List<FirstResponderDO> getFirstResponderToBeNotified(int catPriority);

	public List<FirstResponderDO> getFRToBeNotifiedModifyOrClosure(String eventId, int catPriority);

	public List<FirstResponderDO> loadFRDoNotDisturbEnd(String hour);

	public List<FirstResponderDO> loadFRDoNotDisturbStart(String hour);

	public List<FirstResponderDO> loadFRSilentEnd(String hour);

	public List<DaeNumbers> fetchFrNumbersByCategory(Calendar from, Calendar to);

}
