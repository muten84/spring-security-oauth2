/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate;

import java.util.List;

import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfoRequest;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DownloadArtifactResponse;

/**
 * @author Bifulco Luigi
 *
 */
public interface ArtifactsManagerDelegateService {

	/**
	 * 
	 * @param request
	 * @return
	 */

	ArtifactInfo getArtifactInfo(ArtifactInfoRequest request);

	/**
	 * @param info
	 * @return
	 */
	DownloadArtifactResponse requestArtifactDownload(ArtifactInfo info);

	/**
	 * @param fpath
	 */
	String getArtifactPath(String fpath);

	/**
	 * @param type
	 * @return
	 */
	List<ArtifactInfoRequest> listArtifactsInGroup(String groupId);

}
