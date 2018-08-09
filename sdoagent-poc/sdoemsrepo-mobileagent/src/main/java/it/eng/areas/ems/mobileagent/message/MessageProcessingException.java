/**
 * 
 */
package it.eng.areas.ems.mobileagent.message;

import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;

/**
 * @author Bifulco Luigi
 *
 */
public class MessageProcessingException extends Exception {

	private final Message m;

	public MessageProcessingException(Message m, String reason, Throwable cause) {
		super(reason, cause);
		this.m = m;		
	}

}
