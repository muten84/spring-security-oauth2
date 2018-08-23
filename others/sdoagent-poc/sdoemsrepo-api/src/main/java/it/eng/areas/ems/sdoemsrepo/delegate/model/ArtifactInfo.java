/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import java.io.Serializable;

/**
 * @author Bifulco Luigi
 *
 */
public class ArtifactInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8811183756431329704L;

	private String groupId;

	private String artifactId;

	private String version;

	private String artifactType;

	private String artifactHash;

	private boolean localInstalled;

	private boolean immediateInstall;

	private boolean excludeFromCheck;

	private String localInstallPath;

	private DeviceInfo device;

	public ArtifactInfo() {

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
	 * @return the artifactId
	 */
	public String getArtifactId() {
		return artifactId;
	}

	/**
	 * @param artifactId
	 *            the artifactId to set
	 */
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the artifactType
	 */
	public String getArtifactType() {
		return artifactType;
	}

	/**
	 * @param artifactType
	 *            the artifactType to set
	 */
	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}

	/**
	 * @return the artifactHash
	 */
	public String getArtifactHash() {
		return artifactHash;
	}

	/**
	 * @param artifactHash
	 *            the artifactHash to set
	 */
	public void setArtifactHash(String artifactHash) {
		this.artifactHash = artifactHash;
	}

	/**
	 * @return the localInstalled
	 */
	public boolean isLocalInstalled() {
		return localInstalled;
	}

	/**
	 * @param localInstalled
	 *            the localInstalled to set
	 */
	public void setLocalInstalled(boolean localInstalled) {
		this.localInstalled = localInstalled;
	}

	/**
	 * @return the immediateInstall
	 */
	public boolean isImmediateInstall() {
		return immediateInstall;
	}

	/**
	 * @param immediateInstall
	 *            the immediateInstall to set
	 */
	public void setImmediateInstall(boolean immediateInstall) {
		this.immediateInstall = immediateInstall;
	}

	/**
	 * @return the localInstallPath
	 */
	public String getLocalInstallPath() {
		return localInstallPath;
	}

	/**
	 * @param localInstallPath
	 *            the localInstallPath to set
	 */
	public void setLocalInstallPath(String localInstallPath) {
		this.localInstallPath = localInstallPath;
	}

	/**
	 * @return the device
	 */
	public DeviceInfo getDevice() {
		return device;
	}

	/**
	 * @param device
	 *            the device to set
	 */
	public void setDevice(DeviceInfo device) {
		this.device = device;
	}

	/**
	 * @return the excludeFromCheck
	 */
	public boolean isExcludeFromCheck() {
		return excludeFromCheck;
	}

	/**
	 * @param excludeFromCheck
	 *            the excludeFromCheck to set
	 */
	public void setExcludeFromCheck(boolean excludeFromCheck) {
		this.excludeFromCheck = excludeFromCheck;
	}

}
