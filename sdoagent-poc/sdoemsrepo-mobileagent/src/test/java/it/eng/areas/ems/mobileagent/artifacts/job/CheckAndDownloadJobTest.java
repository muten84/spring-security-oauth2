/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.job;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.delegate.impl.ArtifactsManagerImpl;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerService;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;

/**
 * @author Bifulco Luigi
 *
 */

@RunWith(JUnit4.class)
public class CheckAndDownloadJobTest {

	private ArtifactsManager manager;
	private ArtifactsManagerService service;
	private String url = "http://qalitax.eng.it:9129/sdo118Artifacts-manager/";
	private String groupId = "EE";
	private String artifactId = "terminal";
	private String dest = "/esel/terminal/download";

	private CheckAndDownloadJob job;

	@Before
	public void init() {
		if (service == null) {
			service = new ArtifactsManagerService(url, "api/rest/artifacts");
			JsonStoreService storeService = new JsonStoreService(dest, this.groupId, new JsonMapper());
			manager = new ArtifactsManagerImpl(service, storeService);
			//job = new CheckAndDownloadJob(null, storeService, manager, this.groupId, artifactId, dest);
		}
	}
	
//	public CheckAndDownloadJob createCheckAndDownloadJob(String artifactId) {
//		return new CheckAndDownloadJob(this.jsonStoreService, this.artifactsManager, this.groupId, artifactId,
//				this.localStorePath, this.artifactsManagerStateService);
//	}

	@Test
	public void testJobDone() throws IOException {
		job.start();
	}

}
