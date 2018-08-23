/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import io.jsondb.annotation.Document;
import io.swagger.annotations.ApiModel;

/**
 * @author Bifulco Luigi
 *
 */
@Document(collection = "sites", schemaVersion = "1.0")
@ApiModel
public class SiteInfo {

	@io.jsondb.annotation.Id
	private String id;

	private String name;

	/* url http che riceve le richieeste dai terminali */
	private String messageServiceUrl;

	/*
	 * url mqtt che permette di inviare i messaggi ai terminali tramite broker
	 */
	private String messageReceiverUrl;

	private int mqttQosLevel;

	private int mqttKeepAlive;

	private long mqttConnectAttemptMax;

	private long mqttReconnectAttemptsMax;

	private long mqttReconnectDelay;

	private long mqttReconnectDelayMax;

	private long mqttReceiveFreqInMillis;

	private int defaultLogType;

	private long connectTimeoutInMillis;

	private long readTimeoutInMillis;

	private long writeTimeoutInMillis;

	private DeviceConfiguration deviceConf;

	/**
	 * @return the mqttKeepAlive
	 */
	public int getMqttKeepAlive() {
		return mqttKeepAlive;
	}

	/**
	 * @param mqttKeepAlive
	 *            the mqttKeepAlive to set
	 */
	public void setMqttKeepAlive(int mqttKeepAlive) {
		this.mqttKeepAlive = mqttKeepAlive;
	}

	/**
	 * @return the mqttConnectAttemptMax
	 */
	public long getMqttConnectAttemptMax() {
		return mqttConnectAttemptMax;
	}

	/**
	 * @param mqttConnectAttemptMax
	 *            the mqttConnectAttemptMax to set
	 */
	public void setMqttConnectAttemptMax(long mqttConnectAttemptMax) {
		this.mqttConnectAttemptMax = mqttConnectAttemptMax;
	}

	/**
	 * @return the mqttReconnectAttemptsMax
	 */
	public long getMqttReconnectAttemptsMax() {
		return mqttReconnectAttemptsMax;
	}

	/**
	 * @param mqttReconnectAttemptsMax
	 *            the mqttReconnectAttemptsMax to set
	 */
	public void setMqttReconnectAttemptsMax(long mqttReconnectAttemptsMax) {
		this.mqttReconnectAttemptsMax = mqttReconnectAttemptsMax;
	}

	/**
	 * @return the mqttReconnectDelay
	 */
	public long getMqttReconnectDelay() {
		return mqttReconnectDelay;
	}

	/**
	 * @param mqttReconnectDelay
	 *            the mqttReconnectDelay to set
	 */
	public void setMqttReconnectDelay(long mqttReconnectDelay) {
		this.mqttReconnectDelay = mqttReconnectDelay;
	}

	/**
	 * @return the mqttReconnectDelayMax
	 */
	public long getMqttReconnectDelayMax() {
		return mqttReconnectDelayMax;
	}

	/**
	 * @param mqttReconnectDelayMax
	 *            the mqttReconnectDelayMax to set
	 */
	public void setMqttReconnectDelayMax(long mqttReconnectDelayMax) {
		this.mqttReconnectDelayMax = mqttReconnectDelayMax;
	}

	/**
	 * @return the deviceConf
	 */
	public DeviceConfiguration getDeviceConf() {
		return deviceConf;
	}

	/**
	 * @param deviceConf
	 *            the deviceConf to set
	 */
	public void setDeviceConf(DeviceConfiguration deviceConf) {
		this.deviceConf = deviceConf;
	}

	/**
	 * @return the messageServiceUrl
	 */
	public String getMessageServiceUrl() {
		return messageServiceUrl;
	}

	/**
	 * @param messageServiceUrl
	 *            the messageServiceUrl to set
	 */
	public void setMessageServiceUrl(String messageServiceUrl) {
		this.messageServiceUrl = messageServiceUrl;
	}

	/**
	 * @return the messageReceiverUrl
	 */
	public String getMessageReceiverUrl() {
		return messageReceiverUrl;
	}

	/**
	 * @param messageReceiverUrl
	 *            the messageReceiverUrl to set
	 */
	public void setMessageReceiverUrl(String messageReceiverUrl) {
		this.messageReceiverUrl = messageReceiverUrl;
	}

	/**
	 * @return the mqttQosLevel
	 */
	public int getMqttQosLevel() {
		return mqttQosLevel;
	}

	/**
	 * @param mqttQosLevel
	 *            the mqttQosLevel to set
	 */
	public void setMqttQosLevel(int mqttQosLevel) {
		this.mqttQosLevel = mqttQosLevel;
	}

	/**
	 * @return the defaultLogType
	 */
	public int getDefaultLogType() {
		return defaultLogType;
	}

	/**
	 * @param defaultLogType
	 *            the defaultLogType to set
	 */
	public void setDefaultLogType(int defaultLogType) {
		this.defaultLogType = defaultLogType;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the mqttReceiveFreqInMillis
	 */
	public long getMqttReceiveFreqInMillis() {
		return mqttReceiveFreqInMillis;
	}

	/**
	 * @param mqttReceiveFreqInMillis
	 *            the mqttReceiveFreqInMillis to set
	 */
	public void setMqttReceiveFreqInMillis(long mqttReceiveFreqInMillis) {
		this.mqttReceiveFreqInMillis = mqttReceiveFreqInMillis;
	}

	/**
	 * @return the connectTimeoutInMillis
	 */
	public long getConnectTimeoutInMillis() {
		return connectTimeoutInMillis;
	}

	/**
	 * @param connectTimeoutInMillis
	 *            the connectTimeoutInMillis to set
	 */
	public void setConnectTimeoutInMillis(long connectTimeoutInMillis) {
		this.connectTimeoutInMillis = connectTimeoutInMillis;
	}

	/**
	 * @return the readTimeoutInMillis
	 */
	public long getReadTimeoutInMillis() {
		return readTimeoutInMillis;
	}

	/**
	 * @param readTimeoutInMillis
	 *            the readTimeoutInMillis to set
	 */
	public void setReadTimeoutInMillis(long readTimeoutInMillis) {
		this.readTimeoutInMillis = readTimeoutInMillis;
	}

	/**
	 * @return the writeTimeoutInMillis
	 */
	public long getWriteTimeoutInMillis() {
		return writeTimeoutInMillis;
	}

	/**
	 * @param writeTimeoutInMillis
	 *            the writeTimeoutInMillis to set
	 */
	public void setWriteTimeoutInMillis(long writeTimeoutInMillis) {
		this.writeTimeoutInMillis = writeTimeoutInMillis;
	}

}
