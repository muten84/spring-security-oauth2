/**
 * 
 */
package it.eng.areas.ems.mobileagent.message.job;

import org.pmw.tinylog.Logger;

import it.eng.areas.ems.mobileagent.http.WsHandler;
import it.eng.areas.ems.mobileagent.message.MessageReceiver;

/**
 * @author Bifulco Luigi
 *
 */
public class MessageReceiverJob {

	public MessageReceiverJob() {

	}

	public void doRun(MessageReceiver receiver) {
		try {
			if (!receiver.isConnected()) {
				Thread.sleep(10000);
				//receiver.restart();
				
				//TODO: testare riconnessione con "mqttReconnectAttemptsMax":1000,"mqttReconnectDelay":30
				// long reconnectDelay = mqtt.reconnectDelay;
//		        if( reconnectDelay> 0 && mqtt.reconnectBackOffMultiplier > 1.0 ) {
//		            reconnectDelay = (long) Math.pow(mqtt.reconnectDelay*reconnects, mqtt.reconnectBackOffMultiplier);
//		        }
//		        reconnectDelay = Math.min(reconnectDelay, mqtt.reconnectDelayMax);
//		        reconnects += 1;
				
				Logger.info("WARNING messageReceiver IS NOT MORE CONNECTED!!");
				return;
			}
			Logger.info("calling receive");
			String message = receiver.receive();
			Logger.info("new message received: " + message);
			WsHandler.sendEvent(message);
		} catch (Exception e) {
			Logger.error(e,"Exception during MessageReceiverJob::doRun");
		}

	}

}
