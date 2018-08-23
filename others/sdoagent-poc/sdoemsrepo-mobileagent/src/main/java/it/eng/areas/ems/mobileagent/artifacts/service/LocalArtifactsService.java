/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.service;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.pmw.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.service.util.FileListUtil;

/**
 * @author Bifulco Luigi
 *
 */
public class LocalArtifactsService {

	private FileListUtil fileUtil;
	private String localStorePath;
	private JsonStoreService storeService;
	private JsonMapper mapper;

	@Inject
	public LocalArtifactsService(@Named("agent.artifacts.store.path") String localStorePath,
			JsonStoreService storeService, JsonMapper mapper) {
		this.fileUtil = new FileListUtil();
		this.localStorePath = localStorePath;
		this.storeService = storeService;
		this.mapper = mapper;
	}

	public String getLastArtifact(String groupId, String artifactId) {
		File jsonFile = new File(localStorePath + "/" + groupId + "/" + artifactId + ".json");
		if (!jsonFile.exists()) {
			ArtifactInfo info = new ArtifactInfo();
			info.setGroupId(groupId);
			info.setArtifactId(artifactId);
			info.setArtifactHash("-1");
			try {
				return mapper.stringify(info);
			} catch (JsonProcessingException e) {
				Logger.error(e,"JsonProcessingException in getLastArtifact");
			}
		}
		try {
			// String s= FileUtils.readFileToString(f);
			ArtifactInfo info = storeService.readArtifactInfoMetaData(artifactId);
			File zipFile = new File(localStorePath + "/" + info.getGroupId() + "/" + info.getArtifactId() + "-"
					+ info.getVersion() + "." + info.getArtifactType());
			info.setLocalInstalled(!zipFile.exists());
			return mapper.stringify(info);
		} catch (IOException e) {
			Logger.error(e,"IOException in getLastArtifact");
		}
		return "{}";

	}

	public boolean resetLocalStore(String groupId) {
		File f = new File(localStorePath + "/" + groupId);
		return f.delete();
	}
}
