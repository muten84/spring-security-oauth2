/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.service;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;

import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;

/**
 * @author Bifulco Luigi
 *
 */
public class JsonStoreService {

	private String storePath;

	private String groupId;

	private JsonMapper mapper;

	@Inject
	public JsonStoreService(@Named("agent.artifacts.store.path") String storePath,
			@Named("agent.artifacts.store.groupId") String groupId, JsonMapper jsonMapper) {
		this.storePath = storePath;
		this.mapper = jsonMapper;
		this.groupId = groupId;
	}

	public boolean storeArtifactInfoMetaData(ArtifactInfo info) throws IOException {
		String json = mapper.stringify(info);
		FileUtils.writeStringToFile(
				new File(storePath + "/" + info.getGroupId() + "/" + info.getArtifactId() + ".json"), json);
		return true;

	}

	public ArtifactInfo readArtifactInfoMetaData(String artifactId) throws IOException {
		File f = new File(storePath + "/" + groupId + "/" + artifactId + ".json");
		if (f.exists()) {
			String json = FileUtils.readFileToString(f);
			return mapper.parse(json, ArtifactInfo.class);
		} else
			return null;
	}

	public String readArtifactInfoMetaDataAsString(String artifactId) throws IOException {
		File f = new File(storePath + "/" + groupId + "/" + artifactId + ".json");
		if (f.exists()) {
			String json = FileUtils.readFileToString(f);
			return json;
		} else
			return null;
	}

}
