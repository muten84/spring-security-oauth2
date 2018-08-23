/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.delegate.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.pmw.tinylog.Logger;

import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerService;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import it.eng.areas.ems.mobileagent.device.DeviceInfoUtil;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfoRequest;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DeviceInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DownloadArtifactResponse;

/**
 * @author Bifulco Luigi
 *
 */
public class ArtifactsManagerImpl implements ArtifactsManager {

	private ArtifactsManagerService artifactsService;

	private JsonStoreService storeService;

	@Inject
	public ArtifactsManagerImpl(ArtifactsManagerService artifactsService, JsonStoreService storeService) {
		this.artifactsService = artifactsService;
		this.storeService = storeService;
	}

	/**
	 * return null if there is no new version to download
	 * 
	 * @param groupId
	 * @param artifactId
	 * @return
	 * @throws IOException
	 */
	@Override
	public ArtifactInfo checkForNewVersion(String groupId, String artifactId) {
		ArtifactInfoRequest request = new ArtifactInfoRequest();
		request.setArtifacId(artifactId);
		request.setGroupId(groupId);
		request.setAritfactType("zip");

		String json = "";
		try {
			json = storeService.readArtifactInfoMetaDataAsString(artifactId);
		} catch (IOException e) {
			// Logger.info(e.getMessage());
			Logger.error(e,"error in checkForNewVersion");
		}
		DeviceInfo device = DeviceInfoUtil.createDeviceInfo(json);

		request.setDeviceInfo(device);
		ArtifactInfo lastArtifact = null;
		try {
			lastArtifact = artifactsService.getArtifactInfo(request);
		} catch (IOException e) {
			Logger.error(e,"IOException in checkForNewVersion");
		}
		ArtifactInfo localArtifact = null;
		try {
			localArtifact = storeService.readArtifactInfoMetaData(artifactId);
		} catch (IOException e) {
			Logger.error(e,"IOException in checkForNewVersion");
		}
		if (localArtifact == null && lastArtifact == null) {
			return null;
		}
		if (localArtifact == null) {
			return lastArtifact;
		} else {
			Integer remoteVersion = Integer.valueOf(String.join("", lastArtifact.getVersion().split("\\.")));
			Integer localVersion = Integer.valueOf(String.join("", localArtifact.getVersion().split("\\.")));
			if (remoteVersion > localVersion) {
				return lastArtifact;
			}
		}
		return null;
	}

	/**
	 * If != null return a complete URL to download the artifact
	 * 
	 * @param artifact
	 * @return
	 * @throws IOException
	 */
	@Override
	public String requestDownload(ArtifactInfo artifact) throws IOException {
		DownloadArtifactResponse response = artifactsService.requestArtifactDownload(artifact);
		return response.getDownloadUri();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager#
	 * getArtifactsList(java.lang.String)
	 */
	@Override
	public List<ArtifactInfoRequest> getArtifactsList(String groupId) throws IOException {
		return artifactsService.listArtifactsInGroup(groupId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager#
	 * downloadFileSync(java.lang.String, java.io.File)
	 */
	@Override
	public void downloadFileSync(String source, File destination) throws IOException {
		artifactsService.downloadFileSync(source, destination);

	}
}
