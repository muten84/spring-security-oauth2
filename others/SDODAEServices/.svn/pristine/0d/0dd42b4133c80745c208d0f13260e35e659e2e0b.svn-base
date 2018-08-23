/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.service;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.MessaggioDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.MessaggioFilterDO;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTMessaggioDO;




public interface MessagesTransactionalService {

	/**
	 * 
	 * Get an entity of type ExampleDO by its Id
	 * 
	 * @param id
	 * @return
	 */
	public List<MessaggioDO> getAllMessaggio();
	
	public MessaggioDO getMessaggioById(String id);

	
	public boolean deleteMessaggioById(String id);


	public List<MessaggioDO> getMessaggioByFilter(MessaggioFilterDO daeFilter);


	public MessaggioDO insertMessaggio(MessaggioDO daeDO);

	public long countAll();

	public List<FirstResponderDO> getMessageResponders(String id);


	//public List<VCTMessaggioDO> getAllVctMessaggio();
}
