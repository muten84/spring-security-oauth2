/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.service;

import it.eng.areas.to.sdoto.docservice.entity.*;

/**
 * @author Bifulco Luigi
 *
 */

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
