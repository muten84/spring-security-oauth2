/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.entity;

import java.io.Serializable;

/**
 * @author Bifulco Luigi
 *
 */
public class ArtifactDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3071068215763579856L;

	private String groupId;

	private String artifactId;

	private String version;

	private String artifactType;

	private String artifactHash;

	private boolean immediateInstall;

	private boolean excludeFromCheck;

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
