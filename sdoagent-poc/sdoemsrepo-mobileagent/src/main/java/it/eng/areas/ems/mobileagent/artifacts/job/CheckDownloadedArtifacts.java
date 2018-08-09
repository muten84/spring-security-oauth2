/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.job;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.pmw.tinylog.Logger;

import it.eng.areas.ems.mobileagent.artifacts.ExecutionContext;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.artifacts.service.LocalArtifactsService;
import it.eng.areas.ems.mobileagent.artifacts.util.InstallUtils;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.service.util.FileListUtil;

/**
 * @author Bifulco Luigi
 *
 */
public class CheckDownloadedArtifacts {

	private FileListUtil fileUtil = new FileListUtil();

	private String groupId;

	private String localStorePath;

	private LocalArtifactsService localArtifactsService;

	private JsonMapper mapper;

	public CheckDownloadedArtifacts(@Named("agent.artifacts.store.groupId") String groupId, @Named("agent.artifacts.store.path") String localStorePath,
			LocalArtifactsService localArtifactsService, JsonMapper mapper) {
		this.groupId = groupId;
		this.localStorePath = localStorePath;
		this.mapper = mapper;
	}

	public boolean start() throws IOException {
		String groupId = this.groupId;
		String localStore = this.localStorePath;

		List<String> files = fileUtil.listFiles(localStore + "/" + groupId);
		if(this.localArtifactsService == null) {
			return false;
		}
		for (String f : files) {
			if (f.contains(".zip")) {
				String archivePath = localStore + "/" + groupId + "/" + f;
				String artifactObject = this.localArtifactsService.getLastArtifact(groupId, f.split("-")[0]);
				ArtifactInfo info = this.mapper.parse(artifactObject, ArtifactInfo.class);
				if (info == null) {
					continue;
				}
				if(info.isExcludeFromCheck()) {
					continue;
				}
				String s = String.valueOf(FileUtils.checksumCRC32(new File(archivePath)));
				if (!info.getArtifactHash().equals(s)) {
					throw new IOException("CRC 32 checksum failed, file deleted: " + new File(archivePath).delete());
				}
				InstallUtils.backupFolder(localStore+"/backup/"+info.getArtifactId(), info.getLocalInstallPath());
				boolean res = InstallUtils.extractAndCopy(archivePath, info.getLocalInstallPath());

				Logger.info("copy: " + res);
				if (res) {
					res = InstallUtils.installationDone(archivePath);
					Logger.info("delete: " + res);
				}

			}
		}
		return true;
		// List<ArtifactInfo> artifacts =
		// this.ctx.getArtifactsManagerService().listArtifactsInGroup(groupId);
		// for (ArtifactInfo artifactInfo : artifacts) {
		// artifactInfo.
		// }
	}

}
