/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.areas.ems.sdoemsrepo.delegate.ArtifactsManagerDelegateService;
import it.eng.areas.ems.sdoemsrepo.delegate.mapper.ArtifactsMapper;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfoRequest;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DownloadArtifactResponse;
import it.eng.areas.ems.sdoemsrepo.entity.ArtifactDO;
import it.eng.areas.ems.sdoemsrepo.service.ArtifactsManagerService;

/**
 * @author Bifulco Luigi
 *
 */
@Component
public class ArtifactsManagerDelegateServiceImpl implements ArtifactsManagerDelegateService {

	@Autowired
	private ArtifactsManagerService artifactsManager;

	@Autowired
	private ArtifactsMapper artifactsMapper;

	@Autowired
	private ObjectMapper jsonMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.ems.sdoemsrepo.delegate.ArtifactsManagerDelegateService#
	 * getArtifactInfo(it.eng.areas.ems.sdoemsrepo.delegate.model.
	 * ArtifactInfoRequest)
	 */
	@Override
	public ArtifactInfo getArtifactInfo(ArtifactInfoRequest request) {
		Optional<ArtifactDO> artifact = artifactsManager.getLastArtifact(request.getGroupId(), request.getArtifacId(),
				request.getAritfactType());
		ArtifactInfo info = null;
		if (artifact.isPresent()) {
			String response = artifactsManager.readArtifactMetaData(artifact.get());
			if (response == null) {
				throw new RuntimeException("Metadata not present for " + artifact.get().getGroupId() + " - "
						+ artifact.get().getArtifactId() + " - " + artifact.get().getVersion());

			}
			ArtifactInfo metadata = null;
			try {
				metadata = jsonMapper.readValue(response.getBytes(), ArtifactInfo.class);
				info = artifactsMapper.fromDO2Info(artifact.get());
				info.setImmediateInstall(metadata.isImmediateInstall());
				info.setExcludeFromCheck(metadata.isExcludeFromCheck());
				info.setLocalInstallPath(metadata.getLocalInstallPath());
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.ems.sdoemsrepo.delegate.ArtifactsManagerDelegateService#
	 * requestArtifactDownload(it.eng.areas.ems.sdoemsrepo.delegate.model.
	 * ArtifactInfo)
	 */
	@Override
	public DownloadArtifactResponse requestArtifactDownload(ArtifactInfo info) {
		return artifactsManager.getDownloadInfo(info);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.ems.sdoemsrepo.delegate.ArtifactsManagerDelegateService#
	 * getArtifactPath(java.lang.String)
	 */
	@Override
	public String getArtifactPath(String fpath) {
		return artifactsManager.getArtifactPath(fpath);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.ems.sdoemsrepo.delegate.ArtifactsManagerDelegateService#
	 * listArtifactsByType(java.lang.String)
	 */
	@Override
	public List<ArtifactInfoRequest> listArtifactsInGroup(String groupId) {
		List<ArtifactDO> artifacts = artifactsManager.listArtifactsInGroup(groupId);
		return artifactsMapper.fromDOList2RequestList(artifacts);
	}

}
