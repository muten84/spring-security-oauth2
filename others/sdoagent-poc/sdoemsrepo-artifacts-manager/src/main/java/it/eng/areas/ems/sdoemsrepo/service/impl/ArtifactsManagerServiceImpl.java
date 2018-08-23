/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DownloadArtifactResponse;
import it.eng.areas.ems.sdoemsrepo.entity.ArtifactDO;
import it.eng.areas.ems.sdoemsrepo.service.ArtifactsManagerService;
import it.eng.areas.ems.sdoemsrepo.service.util.FileListUtil;

/**
 * @author Bifulco Luigi
 *
 */
@Component
public class ArtifactsManagerServiceImpl implements ArtifactsManagerService {
	
	private final static Logger LOG = LoggerFactory.getLogger(ArtifactsManagerServiceImpl.class);

	@Value(value = "${sdoemsrepo.repository.basepath}")
	private String basePath;

	private FileListUtil fileUtil;

	@PostConstruct
	public void init() {
		if (fileUtil == null) {
			fileUtil = new FileListUtil();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.ems.sdoemsrepo.service.ArtifactsManagerService#getArtifacts(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public List<ArtifactDO> getArtifacts(String groupId, String artifactId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.ems.sdoemsrepo.service.ArtifactsManagerService#getArtifacts(
	 * java.lang.String)
	 */
	@Override
	public List<ArtifactDO> getArtifacts(String groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdoemsrepo.service.ArtifactsManagerService#
	 * getLastArtifact(java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<ArtifactDO> getLastArtifact(String groupId, String artifactId, String artifactType) {
		/* Prendo la lista delle versioni */
		Optional<ArtifactDO> artifact = Optional.empty();
		List<String> versions = fileUtil.listFolders(basePath + groupId + "/" + artifactId);
		List<Integer> intVersions = new ArrayList<>();
		// final String[][] versionSequence = new String [1][];
		versions.forEach((v) -> {
			// versionSequence[0]= v.split(".");
			intVersions.add(Integer.valueOf(v.replace(".", "")));
		});
		String lastVersion = String.valueOf(Collections.max(intVersions));
		lastVersion = String.join(".", "" + lastVersion.charAt(0), "" + lastVersion.charAt(1),
				"" + lastVersion.charAt(2));

		/* ho l'ultima versione dell'artefatto ora costruisco i metadati */

		List<String> artifacts = fileUtil.listFiles(basePath + groupId + "/" + artifactId + "/" + lastVersion);
		artifacts = artifacts.stream().filter((ar) -> {
			return ar.contains("." + artifactType);
		}).collect(Collectors.toList());
		System.out.println(artifacts);

		if (artifacts.size() > 1) {
			throw new RuntimeException("Non e' possibile avere più file dello stesso tipo e stessa versione");
		}
		artifact = Optional.of(buildArtifact(artifactId, groupId, lastVersion, artifactType));
		return artifact;
	}

	private String getArtifactHash(ArtifactDO a) throws IOException {
		String path = basePath + buildPathFromArtifactInfo(a);
		long checksum = FileUtils.checksum(new File(path), new CRC32()).getValue();
		// MD5Util.calculateMD5(path, size)
		a.setArtifactHash(String.valueOf(checksum));
		return a.getArtifactHash();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdoemsrepo.service.ArtifactsManagerService#
	 * getDownloadInfo(it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo)
	 */
	@Override
	public DownloadArtifactResponse getDownloadInfo(ArtifactInfo info) {
		String downloadPath = buildPathFromArtifactInfo(info);
		DownloadArtifactResponse response = new DownloadArtifactResponse();
		response.setArtifact(info);
		response.setDownloadUri(downloadPath);
		return response;
	}

	private ArtifactDO buildArtifact(String id, String groupId, String version, String type) {
		ArtifactDO a = new ArtifactDO();
		a.setArtifactId(id);
		a.setArtifactType(type);
		a.setGroupId(groupId);
		a.setVersion(version);
		try {
			a.setArtifactHash(getArtifactHash(a));
		} catch (IOException e) {

		}
		return a;
	}

	@Override
	public String readArtifactMetaData(ArtifactDO a) {
		File f = new File(basePath + "/" + a.getGroupId() + "/" + a.getArtifactId() + "/" + a.getVersion() + "/"
				+ a.getArtifactId() + "-" + a.getVersion() + ".json");
		if (f.exists()) {
			String json = "{}";
			try {
				json = FileUtils.readFileToString(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			return json;
		} else
			return null;
	}

	private String buildPathFromArtifactInfo(ArtifactInfo a) {
		return a.getGroupId() + "/" + a.getArtifactId() + "/" + a.getVersion() + "/" + a.getArtifactId() + "-"
				+ a.getVersion() + "." + a.getArtifactType();
	}

	private String buildPathFromArtifactInfo(ArtifactDO a) {
		return a.getGroupId() + "/" + a.getArtifactId() + "/" + a.getVersion() + "/" + a.getArtifactId() + "-"
				+ a.getVersion() + "." + a.getArtifactType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdoemsrepo.service.ArtifactsManagerService#
	 * getArtifactPath(java.lang.String)
	 */
	@Override
	public String getArtifactPath(String fpath) {
		File f = new File(basePath + fpath);
		if (f.exists()) {
			return f.getAbsolutePath();
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdoemsrepo.service.ArtifactsManagerService#
	 * listArtifactsByType(java.lang.String)
	 */
	@Override
	public List<ArtifactDO> listArtifactsInGroup(String groupId) {
		List<String> artifactsNames = fileUtil.listFolders(basePath + groupId);

		List<ArtifactDO> toRet = new ArrayList<>();
		for (String a : artifactsNames) {
			ArtifactDO ar = new ArtifactDO();
			ar.setArtifactId(a);
			ar.setGroupId(groupId);
			toRet.add(ar);
		}

		return toRet;
	}

}
