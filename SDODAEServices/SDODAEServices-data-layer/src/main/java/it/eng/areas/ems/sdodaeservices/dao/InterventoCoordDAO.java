package it.eng.areas.ems.sdodaeservices.dao;

import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoCoordDO;

public interface InterventoCoordDAO extends EntityDAO<InterventoCoordDO, String> {

	List<InterventoCoordDO> getFirstCoordinateOfIntervention(String id);

}
