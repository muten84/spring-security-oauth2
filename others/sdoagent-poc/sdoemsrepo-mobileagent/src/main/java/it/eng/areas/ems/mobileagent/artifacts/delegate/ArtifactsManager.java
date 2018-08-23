/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.delegate;

import java.io.File;
import java.io.IOException;
import java.util.List;

import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfoRequest;

/**
 * @author Bifulco Luigi
 *
 */
public interface ArtifactsManager {

	/**
	 * @param groupId
	 * @param artifactId
	 * @return
	 * @throws IOException
	 */
	ArtifactInfo checkForNewVersion(String groupId, String artifactId) ;

	/**
	 * @param artifact
	 * @return
	 * @throws IOException
	 */
	String requestDownload(ArtifactInfo artifact) throws IOException;

	/**
	 * <
	 * 
	 * @param groupId
	 * @return
	 * @throws IOException
	 */
	List<ArtifactInfoRequest> getArtifactsList(String groupId) throws IOException;

	/**
	 * @param source
	 * @param destination
	 * @throws IOException 
	 */
	void downloadFileSync(String source, File destination) throws IOException;

}
