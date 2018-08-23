/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.dao;

import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;

/**
 * @author Bifulco Luigi
 *
 */
public interface MessageListRepository {

	/**
	 * @param queueName
	 * @return
	 */
	Message popMessage(String queueName);

	/**
	 * @param queueName
	 * @param m
	 */
	void pushMessage(String queueName, Message m);
	
	/**
	 * 
	 */
	long countMessages(String queueName);

}
