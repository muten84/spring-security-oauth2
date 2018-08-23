package it.eng.areas.ems.sdodaeservices.dao;

import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.DashboardPositionDO;

public interface DashboardPositionDAO extends EntityDAO<DashboardPositionDO, String> {

	List<DashboardPositionDO> getDashboardPositionsByUserId(String utenteId);

}
