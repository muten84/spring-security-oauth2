/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Bifulco Luigi
 *
 */
public class DeviceInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1117880417543876046L;

	private String deviceId;

	private String ipAddress;

	private long timestamp;

	private String note;

	public DeviceInfo() {

	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 *            the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	

}
