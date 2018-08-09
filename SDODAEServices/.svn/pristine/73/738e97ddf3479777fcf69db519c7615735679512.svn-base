/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.service;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.entity.CategoriaFrDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.CategoriaFrFilterDO;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTCategoriaFrDO;




public interface CategoriaFrTransactionalService {

	/**
	 * 
	 * Get an entity of type ExampleDO by its Id
	 * 
	 * @param id
	 * @return
	 */
	public List<CategoriaFrDO> getAllCategoriaFr();

	
	public CategoriaFrDO getCategoriaFrById(String id);
	
	public boolean deleteCategoriaFrById(String id);

	long countAll();


	public List<CategoriaFrDO> getCategoriaFrByFilter(CategoriaFrFilterDO daeFilter);


	public CategoriaFrDO insertCategoriaFr(CategoriaFrDO daeDO);


	//public List<VCTCategoriaFrDO> getAllVctCategoriaFr();
}
