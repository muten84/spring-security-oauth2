/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.dao;

import org.springframework.data.repository.CrudRepository;

import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;

/**
 * @author Bifulco Luigi
 *
 */
public interface MessageRepository extends CrudRepository<Message, String> {

//	/**
//	 * 
//	 * @param m
//	 */
//	void saveMessage(Message m);
//
//	/**
//	 * @param id
//	 * @return
//	 */
//	Message getMessage(String id);
//
//	/**
//	 * @return
//	 */
//	List<Message> getAllMessages();
//
//	/**
//	 * @return
//	 */
//	long countMessages();

}
