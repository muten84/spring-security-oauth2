/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.jsondb.annotation.Document;

/**
 * @author Bifulco Luigi
 *
 */
@RedisHash("messages")
@Document(collection = "messages", schemaVersion = "1.0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message implements Serializable {

	@org.springframework.data.annotation.Id
	@io.jsondb.annotation.Id
	private String id;

	private String from;

	private String identity;

	private String to;

	private String payload;

	private long timestamp;

	private long timeout;

	private String fromUrl;

	private String type;

	private String rpcOperation;

	private String relatesTo;

	private short processed;

	private long ttl;

	private int priority;

	private boolean retry;

	private boolean asynch;

	// private String newField;

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
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * @param payload
	 *            the payload to set
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the fromUrl
	 */
	public String getFromUrl() {
		return fromUrl;
	}

	/**
	 * @param fromUrl
	 *            the fromUrl to set
	 */
	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the rpcOperation
	 */
	public String getRpcOperation() {
		return rpcOperation;
	}

	/**
	 * @param rpcOperation
	 *            the rpcOperation to set
	 */
	public void setRpcOperation(String rpcOperation) {
		this.rpcOperation = rpcOperation;
	}

	/**
	 * @return the relatesTo
	 */
	public String getRelatesTo() {
		return relatesTo;
	}

	/**
	 * @param relatesTo
	 *            the relatesTo to set
	 */
	public void setRelatesTo(String relatesTo) {
		this.relatesTo = relatesTo;
	}

	/**
	 * @return the processed
	 */
	public boolean isProcessed() {
		return processed > 0;
	}

	/**
	 * @param processed
	 *            the processed to set
	 */
	public void setProcessed(boolean processed) {
		if (processed) {
			this.processed = 1;
		} else {
			this.processed = 0;
		}
	}

	/**
	 * @return the ttl
	 */
	public long getTtl() {
		return ttl;
	}

	/**
	 * @param ttl
	 *            the ttl to set
	 */
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	/**
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * @param identity
	 *            the identity to set
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the processed
	 */
	public short getProcessed() {
		return processed;
	}

	/**
	 * @return the retry
	 */
	public boolean isRetry() {
		return retry;
	}

	/**
	 * @param retry
	 *            the retry to set
	 */
	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	/**
	 * @return the asynch
	 */
	public boolean isAsynch() {
		return asynch;
	}

	/**
	 * @param asynch
	 *            the asynch to set
	 */
	public void setAsynch(boolean asynch) {
		this.asynch = asynch;
	}

	/**
	 * @return the newField
	 */
	// public String getNewField() {
	// return newField;
	// }

	/**
	 * @param newField
	 *            the newField to set
	 */
	// public void setNewField(String newField) {
	// this.newField = newField;
	// }

}
