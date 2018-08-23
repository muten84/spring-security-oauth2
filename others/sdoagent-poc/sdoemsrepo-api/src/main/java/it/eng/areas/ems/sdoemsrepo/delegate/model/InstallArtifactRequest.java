/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

/**
 * @author Bifulco Luigi
 *
 */
public class InstallArtifactRequest {

	private ArtifactInfo artifact;

	private DeviceInfo device;

	private String destinationPath;

	public InstallArtifactRequest() {

	}

	/**
	 * @return the artifact
	 */
	public ArtifactInfo getArtifact() {
		return artifact;
	}

	/**
	 * @param artifact
	 *            the artifact to set
	 */
	public void setArtifact(ArtifactInfo artifact) {
		this.artifact = artifact;
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
	 * @return the destinationPath
	 */
	public String getDestinationPath() {
		return destinationPath;
	}

	/**
	 * @param destinationPath
	 *            the destinationPath to set
	 */
	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}

}
