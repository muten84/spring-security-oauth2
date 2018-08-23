package it.eng.areas.ems.sdodaeservices.dao;

import java.util.Calendar;
import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.bean.DaeNumbers;
import it.eng.areas.ems.sdodaeservices.bean.DaeSubscription;
import it.eng.areas.ems.sdodaeservices.entity.DaeDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.DaeFilterDO;

public interface DaeDAO extends EntityDAO<DaeDO, String> {

	public List<DaeDO> searchDaeByFilter(DaeFilterDO filter);

	public DaeDO insertDae(DaeDO daeDO);

	List<DaeDO> getAllDaeWithLocation();

	/**
	 * @param month
	 * @param year
	 * @return
	 */
	List<DaeSubscription> countDaeSubscription(Calendar from, Calendar to);

	public List<DaeDO> findDuplicate(DaeDO daeDO, List<String> duplicateDaeFieldList);

	public List<DaeNumbers> fetchDaeNumbersByStatus(Calendar from, Calendar to);

}
