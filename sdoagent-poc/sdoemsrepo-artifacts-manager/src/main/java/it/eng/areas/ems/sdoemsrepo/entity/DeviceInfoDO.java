/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Bifulco Luigi
 *
 */
@Entity(name = "DEVICE_INFO")
public class DeviceInfoDO implements Serializable {

	/**
	 * S/N or other device Universal Identifier
	 */
	@Id
	private String deviceId;

	/**
	 * Last address device used to provide info
	 */
	@Column
	private String deviceAddress;

	/**
	 * A JSON format representing a list of artifacts installed on device
	 */
	@Column
	private String deviceArtifacts;

	@Column(name = "LAST_UPDATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

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
	 * @return the deviceAddress
	 */
	public String getDeviceAddress() {
		return deviceAddress;
	}

	/**
	 * @param deviceAddress
	 *            the deviceAddress to set
	 */
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	/**
	 * @return the deviceArtifacts
	 */
	public String getDeviceArtifacts() {
		return deviceArtifacts;
	}

	/**
	 * @param deviceArtifacts
	 *            the deviceArtifacts to set
	 */
	public void setDeviceArtifacts(String deviceArtifacts) {
		this.deviceArtifacts = deviceArtifacts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DeviceInfoDO [deviceId=" + deviceId + ", deviceAddress=" + deviceAddress + ", deviceArtifacts="
				+ deviceArtifacts + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceInfoDO other = (DeviceInfoDO) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		return true;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
