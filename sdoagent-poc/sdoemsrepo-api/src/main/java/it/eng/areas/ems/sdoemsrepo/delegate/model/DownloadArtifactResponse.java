/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import java.io.Serializable;

/**
 * @author Bifulco Luigi
 *
 */
public class DownloadArtifactResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2729846209207968787L;

	private ArtifactInfo artifact;

	private String downloadUri;

	public DownloadArtifactResponse() {

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
	 * @return the downloadUri
	 */
	public String getDownloadUri() {
		return downloadUri;
	}

	/**
	 * @param downloadUri
	 *            the downloadUri to set
	 */
	public void setDownloadUri(String downloadUri) {
		this.downloadUri = downloadUri;
	}

}
