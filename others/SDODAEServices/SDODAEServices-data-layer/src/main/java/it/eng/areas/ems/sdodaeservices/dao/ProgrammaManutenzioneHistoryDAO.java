package it.eng.areas.ems.sdodaeservices.dao;

import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.ProgrammaManutenzioneHistoryDO;

public interface ProgrammaManutenzioneHistoryDAO extends EntityDAO<ProgrammaManutenzioneHistoryDO, String> {

	public List<ProgrammaManutenzioneHistoryDO> listByProgrammaId(String id);

}
