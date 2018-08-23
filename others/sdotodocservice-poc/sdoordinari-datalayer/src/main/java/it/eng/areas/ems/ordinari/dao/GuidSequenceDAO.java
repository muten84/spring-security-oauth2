package it.eng.areas.ems.ordinari.dao;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.ordinari.entity.GuidSequenceDO;

public interface GuidSequenceDAO extends EntityDAO<GuidSequenceDO,String> {

public	String getSequence();

	
	
	
}
