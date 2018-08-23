/**
 * 
 */
package it.eng.areas.ems.mobileagent.message;

import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;

/**
 * @author Bifulco Luigi
 *
 */
public interface MessageHandler {
	
	Message handle(Message m) throws Exception;


}
