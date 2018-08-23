/**
 * 
 */
package it.eng.areas.ems.mobileagent.message;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.pmw.tinylog.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import it.eng.areas.ems.mobileagent.message.job.MessageReceiverJob;

/**
 * @author Bifulco Luigi
 *
 */
public class MessageReceiver {
	private MQTT mqtt = null;
	private String host;
	private int port;
	private String queue;
	BlockingConnection conn;
	private ExecutorService worker;
	private ScheduledExecutorService scheduler;
	private QoS qos;

	private QoS getMqttQosLevel(int level) {
		switch (level) {
		case 0:
			return QoS.AT_MOST_ONCE;
		case 1:
			return QoS.AT_LEAST_ONCE;
		case 2:
			return QoS.EXACTLY_ONCE;
		}
		return null;
	}

	public MessageReceiver(String queue, String host, int port, long connectionTimeout, int qosLevel) {
		this.qos = getMqttQosLevel(qosLevel);
		this.host = host;
		this.port = port;
		this.queue = queue;
		ThreadFactory fact = new ThreadFactoryBuilder().setNameFormat("MessageReceiverWorker-thread-%d").build();
		ThreadFactory fact2 = new ThreadFactoryBuilder().setNameFormat("MessageReceiverScheduler-thread-%d").build();
		this.worker = Executors.newSingleThreadExecutor(fact);
		this.scheduler = Executors.newScheduledThreadPool(1, fact2);
		// this.init(connectionTimeout);
	}

	public boolean shutdown() {
		try {
		this.worker.shutdownNow();
		this.scheduler.shutdown();
		}catch(Exception e) {}

		try {
			if (this.isConnected()) {
				this.conn.unsubscribe(new String[] { this.queue });
				this.conn.disconnect();
			}
			
		} catch (Exception e) {
			
		}
		this.conn = null;
		this.mqtt = null;
		return true;
	}

	public void rebuild() {
		ThreadFactory fact = new ThreadFactoryBuilder().setNameFormat("MessageReceiverWorker-thread-%d").build();
		ThreadFactory fact2 = new ThreadFactoryBuilder().setNameFormat("MessageReceiverScheduler-thread-%d").build();
		this.worker = Executors.newSingleThreadExecutor(fact);
		this.scheduler = Executors.newScheduledThreadPool(1, fact2);
	}

	public void init(long timeout, int mqttKeepAlive, long connectAttemptMax, long reconnectAttemptsMax,
			long reconnectDelay, long reconnectDelayMax, long receiveFreqInMillis) throws Exception {
		boolean connected = conn != null ? conn.isConnected() : false;
		Logger.info("init message receiver: " + connected);
		if (this.conn == null) {
			Logger.info("init service: " + this.host + " - " + this.port);
			mqtt = new MQTT();
			mqtt.setKeepAlive((short) mqttKeepAlive);
			mqtt.setConnectAttemptsMax(connectAttemptMax);
			mqtt.setReconnectAttemptsMax(reconnectAttemptsMax);
			mqtt.setReconnectDelay(reconnectDelay);
			mqtt.setReconnectDelayMax(reconnectDelayMax);
			// TODO verificare le altre configurazione dell'mqtt
			// mqtt.setHost("mqtt://172.30.50.45:1883"); //
			mqtt.setHost(this.host, this.port);
			mqtt.setClientId(this.queue);
			mqtt.setUserName("testuser");
			mqtt.setPassword("passwd");

			Logger.info("subscribing to : " + this.queue);
			this.conn = tryConnect().get(timeout, TimeUnit.MILLISECONDS);			

			Logger.info("connection done: " + this.conn.isConnected());

			trySubscribe().get(timeout, TimeUnit.MILLISECONDS);
			MessageReceiverJob receiver = new MessageReceiverJob();
			this.scheduler.scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					receiver.doRun(MessageReceiver.this);
				}
			}, 1000, receiveFreqInMillis, TimeUnit.MILLISECONDS);
		}
	}

	public Future<BlockingConnection> tryConnect() {
		return worker.submit(new Callable<BlockingConnection>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public BlockingConnection call() throws Exception {
				if (mqtt == null) {
					return null;
				}
				conn = mqtt.blockingConnection();
				conn.connect();
				return conn;
			}
		});
	}

	public Future<String> trySubscribe() {
		if (conn == null) {
			return null;
		}
		return worker.submit(new Callable<String>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public String call() throws Exception {
				return subscribe();
			}
		});
	}

	public String subscribe() throws Exception {
		if (conn == null) {
			throw new MessageProcessingException(null, "Cannot execute subscribe to" + queue,
					new NullPointerException("Connection is null"));
		}
		byte[] resp = conn.subscribe(new Topic[] { new Topic(queue, this.qos) });
		Logger.info("returning");
		return new String(resp);
	}

	public String receive() throws Exception {
		if (conn == null) {
			throw new MessageProcessingException(null, "Cannot execute receive from" + queue,
					new NullPointerException("Connection is null"));
		}
		Message message = this.conn.receive();
		Logger.info(message.getTopic());
		byte[] payload = message.getPayload();
		// process the message then:
		message.ack();
		return new String(payload);
	}

	public void publish(String topic, String payload) throws Exception {
		this.conn.publish(topic, payload.getBytes(), getMqttQosLevel(0), false);
	}

	public boolean isConnected() {
		if (this.conn == null) {
			return false;
		}
//		if(!this.conn.isConnected()) {
//			this.conn.resume();
//		}
		return this.conn.isConnected();
	}

	// public void restart() throws Exception {
	// try {
	// this.conn.disconnect();
	// } catch (Exception e) {
	// try {
	// this.conn.kill();
	// } catch (Exception e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// }
	// this.conn = null;
	// this.mqtt = null;
	// this.conn.connect();
	// }

}
