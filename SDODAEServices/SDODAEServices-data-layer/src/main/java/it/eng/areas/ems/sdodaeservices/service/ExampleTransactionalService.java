/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.service;

import it.eng.areas.ems.sdodaeservices.entity.ExampleDO;




public interface ExampleTransactionalService {

	/**
	 * 
	 * Get an entity of type ExampleDO by its Id
	 * 
	 * @param id
	 * @return
	 */
	ExampleDO getExampleById(String id);

}
