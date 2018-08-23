/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DownloadArtifactResponse;
import it.eng.areas.ems.sdoemsrepo.entity.ArtifactDO;

/**
 * @author Bifulco Luigi
 *
 */
public interface ArtifactsManagerService {

	List<ArtifactDO> getArtifacts(String groupId, String artifactId);

	List<ArtifactDO> getArtifacts(String groupId);

	Optional<ArtifactDO> getLastArtifact(String groupId, String artifactId, String artifactType);

	DownloadArtifactResponse getDownloadInfo(ArtifactInfo info);

	String getArtifactPath(String fpath);

	/**
	 * @param groupId
	 * @param type
	 * @return
	 */
	List<ArtifactDO> listArtifactsInGroup(String groupId);

	/**
	 * @param a
	 * @return
	 * @throws IOException
	 */
	String readArtifactMetaData(ArtifactDO a);

}
