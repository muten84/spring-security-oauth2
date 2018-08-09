/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.service;

import it.eng.areas.ems.sdodaeservices.entity.DaeDO;



public interface AnotherTransactionalService {

	DaeDO saveOrUpdateDae(DaeDO dae);
	
	DaeDO update(DaeDO dae);

}
