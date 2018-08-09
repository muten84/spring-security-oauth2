/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.job;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfoRequest;

/**
 * @author Bifulco Luigi
 *
 */
public class MultipleArtifactsJob {

	private String groupId;

	private ArtifactsManager artifactsManager;

	private String localStorePath;

	private JsonStoreService jsonStoreService;

	private ArtifactsManagerStateService artifactsManagerStateService;

	@Inject
	public MultipleArtifactsJob(@Named("agent.artifacts.store.groupId") String groupId,
			@Named("agent.artifacts.store.path") String localStorePath, ArtifactsManager artifactsManager,
			JsonStoreService jsonStoreService,ArtifactsManagerStateService artifactsManagerStateService) {
		this.groupId = groupId;
		this.artifactsManager = artifactsManager;
		this.localStorePath = localStorePath;
		this.jsonStoreService = jsonStoreService;
		this.artifactsManagerStateService = artifactsManagerStateService;
	}

	public boolean start() throws IOException {
		boolean done = true;
		List<ArtifactInfoRequest> artifacts = this.artifactsManager.getArtifactsList(this.groupId);
		for (ArtifactInfoRequest artifactInfoRequest : artifacts) {
			// this.ctx.getArtifactManager().checkForNewVersion(artifactInfoRequest.getGroupId(),
			// artifactInfoRequest.getArtifacId());
			done = this.createCheckAndDownloadJob(artifactInfoRequest.getArtifacId()).start();
		}
		return done;

		// this.ctx.createCheckAndDownloadJob();
	}

	public CheckAndDownloadJob createCheckAndDownloadJob(String artifactId) {
		return new CheckAndDownloadJob(this.jsonStoreService, this.artifactsManager, this.groupId, artifactId,
				this.localStorePath, this.artifactsManagerStateService);
	}

}
