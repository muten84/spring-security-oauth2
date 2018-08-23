/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.service;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.entity.GruppoDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.GruppoFilterDO;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTGruppoDO;

public interface GruppoTransactionalService {

	/**
	 * 
	 * Get an entity of type ExampleDO by its Id
	 * 
	 * @param id
	 * @return
	 */
	public List<GruppoDO> getAllGruppo();

	public GruppoDO getGruppoById(String id);

	public boolean deleteGruppoById(String id);

	long countAll();

	public List<GruppoDO> getGruppoByFilter(GruppoFilterDO daeFilter);

	public GruppoDO insertGruppo(GruppoDO daeDO);

	// public List<VCTGruppoDO> getAllVctGruppo();
}
