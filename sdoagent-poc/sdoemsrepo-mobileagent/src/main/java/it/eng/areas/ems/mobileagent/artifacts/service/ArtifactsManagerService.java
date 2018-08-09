/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.pmw.tinylog.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfoRequest;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DownloadArtifactResponse;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Bifulco Luigi
 *
 */
public class ArtifactsManagerService {
	//FIXME: le credenziali dovrebbero essere passate dall'esterno
	private final static String u = "emsmobile";
	private final static String p = "3m5m0b1l3";
	
	private String baseUrl;
	private String restPath;
	private String basePath;

	private final OkHttpClient client;
	private final ObjectMapper jsonMapper = new ObjectMapper();

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public ArtifactsManagerService(String base, String rest) {
		this.baseUrl = base;
		this.restPath = rest;
		this.basePath = this.baseUrl + "/" + this.restPath;
		OkHttpClient aclient = new OkHttpClient();
		client = aclient.newBuilder().connectTimeout(60, TimeUnit.SECONDS)
		.readTimeout(300, TimeUnit.SECONDS)
		.writeTimeout(300, TimeUnit.SECONDS).build();
	}
	
	
	public void downloadFileSync(String downloadUrl, File destination) throws IOException {

		 
	    Request request = new Request.Builder().url(downloadUrl).addHeader("X-Authorization-Token", "Bearer dummy")
				.addHeader("authorization", Credentials.basic(u, p)).build();
	    Response response = client.newCall(request).execute();
	    if (!response.isSuccessful()) {
	        throw new IOException("Failed to download file: " + response);
	    }
	    FileOutputStream fos = new FileOutputStream(destination);
	    fos.write(response.body().bytes());
	    fos.close();
	
}

	public DownloadArtifactResponse requestArtifactDownload(ArtifactInfo artifact) throws IOException {
		String opName = "requestArtifactDownload";
		String json = jsonMapper.writeValueAsString(artifact);
		json = this.doPostRequest(basePath + "/" + opName, json);
		DownloadArtifactResponse response = jsonMapper.readValue(json.getBytes(), DownloadArtifactResponse.class);
		response.setDownloadUri(this.basePath + "/repository/" + response.getDownloadUri());
		return response;
	}

	public List<ArtifactInfoRequest> listArtifactsInGroup(String groupId) throws IOException {
		String opName = "listArtifactsInGroup";
		String json = this.doGetRequest(basePath + "/" + opName + "/" + groupId);
		List<ArtifactInfoRequest> list = Arrays
				.asList(jsonMapper.readValue(json.getBytes(), ArtifactInfoRequest[].class));
		return list;
	}

	public ArtifactInfo getArtifactInfo(ArtifactInfoRequest request) throws IOException {
		String opName = "getArtifactInfo";
		String json = jsonMapper.writeValueAsString(request);
		json = this.doPostRequest(basePath + "/" + opName, json);
		return jsonMapper.readValue(json.getBytes(), ArtifactInfo.class);
	}

	protected String doGetRequest(String url) throws IOException {
		Request request = new Request.Builder().url(url).addHeader("X-Authorization-Token", "Bearer dummy")
				.addHeader("authorization", Credentials.basic(u, p)).build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) {
			response.close();
			throw new IOException("received non successufll response: " + response.code() + " - " + response.message());
		}
		return response.body().string();
	}

	protected String doPostRequest(String url, String json) throws IOException {
//		Logger.info("Invoking: " + url);
//		Logger.info(" with json: " + json);
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).addHeader("X-Authorization-Token", "Bearer dummy")
				.addHeader("authorization", Credentials.basic(u, p)).build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) {
			response.close();
			throw new IOException("received non successufll response: " + response.code() + " - " + response.message());
		}
		return response.body().string();
	}

}
