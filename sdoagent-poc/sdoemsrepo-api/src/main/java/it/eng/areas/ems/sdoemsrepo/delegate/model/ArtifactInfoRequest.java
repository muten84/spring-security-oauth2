/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import java.io.Serializable;

/**
 * @author Bifulco Luigi
 *
 */
public class ArtifactInfoRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7447579792803072059L;

	private String artifacId;

	private String groupId;

	private String aritfactType;

	private DeviceInfo deviceInfo;

	/**
	 * @return the artifacId
	 */
	public String getArtifacId() {
		return artifacId;
	}

	/**
	 * @param artifacId
	 *            the artifacId to set
	 */
	public void setArtifacId(String artifacId) {
		this.artifacId = artifacId;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the aritfactType
	 */
	public String getAritfactType() {
		return aritfactType;
	}

	/**
	 * @param aritfactType
	 *            the aritfactType to set
	 */
	public void setAritfactType(String aritfactType) {
		this.aritfactType = aritfactType;
	}

	/**
	 * @return the deviceInfo
	 */
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	/**
	 * @param deviceInfo
	 *            the deviceInfo to set
	 */
	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

}
