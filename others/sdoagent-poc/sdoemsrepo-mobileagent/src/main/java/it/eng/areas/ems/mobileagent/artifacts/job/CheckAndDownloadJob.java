/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.job;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.pmw.tinylog.Logger;

import it.eng.areas.ems.mobileagent.artifacts.delegate.ArtifactsManager;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService;
import it.eng.areas.ems.mobileagent.artifacts.service.ArtifactsManagerStateService.AgentState;
import it.eng.areas.ems.mobileagent.artifacts.service.JsonStoreService;
import it.eng.areas.ems.mobileagent.artifacts.util.InstallUtils;
import it.eng.areas.ems.mobileagent.http.WsHandler;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Bifulco Luigi
 *
 */
public class CheckAndDownloadJob {

	private ArtifactsManager manager;

	private final String groupId;

	private final String artifactId;

	private final String destination;

	private JsonStoreService storeService;

	private ArtifactsManagerStateService stateService;

	private String localStorePath;

	public CheckAndDownloadJob(JsonStoreService storeService, ArtifactsManager manager, String groupId,
			String artifactId, String dest, ArtifactsManagerStateService stateService) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.destination = dest;
		this.manager = manager;
		this.storeService = storeService;
		this.stateService = stateService;
		this.localStorePath = dest;

	}

	public boolean start() throws IOException {
		AgentState state = AgentState.CHECKING;
		state.setReason(artifactId);
		String type = "\"type\": \"ARTIFACT_UPDATE\", ";
		WsHandler.sendEvent("[{ "+type+" \"result\": \"" + state.name() + " - " + state.getReason() + "\"}]");
		stateService.setState(state);
		ArtifactInfo artifact = manager.checkForNewVersion(groupId, artifactId);
		if(artifact!=null)
			Logger.info("Check new versione result is: " + artifact.getArtifactId());
		if (artifact != null) {
			Logger.info("Requesting download ...");
			String url = manager.requestDownload(artifact);
			Logger.info("Download URL is: "+url);
			state = AgentState.DOWNLOADING;
			state.setReason(url);
			WsHandler.sendEvent("[{ "+type+" \"result\": \"" + state.name() + " - " + state.getReason() + "\"}]");
			stateService.setState(state);
			String dest = null;
			try {
				Logger.info("Try downloading artifact from "+url+" to "+this.destination);
				dest = downloadArtifact(artifact, url, this.destination);
				Logger.info("Download success: "+dest);
				state = AgentState.DOWNLOAD_SUCCESS;
				state.setReason(artifact.getArtifactId());
				WsHandler.sendEvent("[{ "+type+" \"result\": \"" + state.name() + " - " + state.getReason() + "\"}]");
				stateService.setState(state);
			} catch (Exception e) {
				/*
				 * se la fase di download fallisce elimino il file che si stava scaricando così
				 * il prossimo job riparte da capo
				 */
				Logger.info("Download ERROR: "+e.getMessage());
				String d = this.destination + "/" + artifact.getGroupId() + "/" + artifact.getArtifactId() + "-"
						+ artifact.getVersion() + "." + artifact.getArtifactType();
//				File fd = new File(d);
//				fd.delete();
				
				state = AgentState.DOWNLOAD_ERROR;
				state.setReason(e.getMessage());
				WsHandler.sendEvent("[{"+type+"\"result\": \"" + state.name() + " - " + state.getReason() + "\"}]");
				stateService.setState(state);
				FileUtils.forceDelete(new File(d));
				throw new IOException(e);
			}
			
			if (artifact.isImmediateInstall()) {
				Logger.info("Artifact is immediate install try to install");
				AgentState s = AgentState.INSTALLING;
				s.setReason(artifact.getArtifactId());
				stateService.setState(s);
				WsHandler.sendEvent("[{"+type+"\"result\": \"" + state.name() + " - " + state.getReason() + "\"}]");
				boolean done = immediateInstall(artifact);
				if(done) {
					AgentState installed = AgentState.INSTALLED;
					installed.setReason(artifact.getArtifactId());
					stateService.setState(installed);
					WsHandler.sendEvent("[{"+type+"\"result\": \"" + installed.name() + " - " + installed.getReason() + "\"}]");
				}
				else {
					AgentState installed = AgentState.NOT_INSTALLED;
					installed.setReason(artifact.getArtifactId());
					stateService.setState(installed);
					WsHandler.sendEvent("[{"+type+"\"result\": \"" + installed.name() + " - " + installed.getReason() + "\"}]");
				}
			}
			Logger.info("Storing metadata");
			return storeService.storeArtifactInfoMetaData(artifact);
		} else {
			state = AgentState.IDLE;
			stateService.setState(state);
			// TODO: no need to download new version
			Logger.info("No need to download new version, or no aritfact metdata data could be downloaded");
			WsHandler.sendEvent("[{"+type+"\"result\": \"" + state.name() + " - " + state.getReason() + "\"}]");
			return false;
		}
	}

	protected boolean immediateInstall(ArtifactInfo artifact) {
		String archive = this.localStorePath;
		archive += "/" + artifact.getGroupId() + "/" + artifact.getArtifactId() + "-" + artifact.getVersion() + "."
				+ artifact.getArtifactType();

		boolean res = false;

		try {
			InstallUtils.backupFolder(this.localStorePath+"/backup/"+artifact.getArtifactId(), artifact.getLocalInstallPath());
			
			/*se è una webapp delta deve preparare la www a recepire le nuove modifiche*/
			if(artifact.getArtifactId().contains("wardelta")) {
				//InstallUtils.emptyDestination(artifact.getLocalInstallPath());
				Logger.info("detected war.delta preparing www folder: "+InstallUtils.prepareWebAppForDelta());
			}
			
			/*se è una webapp completa deve preparare la www a recepire un war da zero*/
			if(artifact.getArtifactId().contains("warall")) {				
				Logger.info("detected war.all preparing www folder: "+InstallUtils.emptyDestination(artifact.getLocalInstallPath()));
			}
			
			res = InstallUtils.extractAndCopy(archive, artifact.getLocalInstallPath());
			Logger.info("copy: " + res);
			if (res) {
				res = InstallUtils.installationDone(archive);
				Logger.info("delete: " + res);
			}
		} catch (Exception e) {			
			Logger.info("ERROR IN immediateInstall "+e.getMessage());
		}
		return res;
	}

	protected String downloadArtifact(ArtifactInfo artifact, String source, String dest) throws IOException {
		dest = dest + "/" + artifact.getGroupId() + "/" + artifact.getArtifactId() + "-" + artifact.getVersion() + "."
				+ artifact.getArtifactType();
		File destination = new File(dest);
		if(!destination.exists()) {
			destination.createNewFile();
		}
		//URL url = new URL(source);
		//FileUtils.copyURLToFile(url, destination);
		try {
			this.manager.downloadFileSync(source, destination);
		}
		catch(Exception e) {
			Logger.info("ERROR DOWNLOAD ARTIFACT FROM "+source);
			destination.delete();
			throw e;
		}		
		String s = String.valueOf(FileUtils.checksumCRC32(destination));
		if (s.equals(artifact.getArtifactHash())) {
			return destination.getAbsolutePath();
		} else {
			throw new IOException("CRC32 checksum failed");
		}
	}
	
	

}
