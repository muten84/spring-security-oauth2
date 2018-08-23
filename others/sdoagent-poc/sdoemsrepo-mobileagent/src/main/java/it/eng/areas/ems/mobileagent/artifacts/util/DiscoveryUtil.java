/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.pmw.tinylog.Logger;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Bifulco Luigi
 *
 */
public class DiscoveryUtil {

	private static OkHttpClient provideHttpClient(String connectTimeoutInMillis, //
			String readTimeoutInMillis, //
			String writeTimeoutInMillis//
	) {
		OkHttpClient client = new OkHttpClient();

		return client.newBuilder().connectTimeout(Long.parseLong(connectTimeoutInMillis), TimeUnit.MILLISECONDS)
				.readTimeout(Long.parseLong(readTimeoutInMillis), TimeUnit.MILLISECONDS)
				.writeTimeout(Long.parseLong(writeTimeoutInMillis), TimeUnit.MILLISECONDS).build();

	}

	public static String discoverNewSite() throws MalformedURLException {
		Map<String, String> map = null;
		try {
			map = getApiSelectorUrls();
		} catch (IOException e) {
			Logger.error(e,"IOException in discoverNewSite");
		}
		if (map == null) {
			return null;
		}
		String url = map.get("preferredSite");
		boolean checkOthers = true;
		try {
			checkOthers = !checkSiteAlive(url);
		} catch (IOException e) {
			Logger.error(e,"IOException in discoverNewSite "+url);
			checkOthers = true;
		}

		if (checkOthers) {
			for (String k : map.keySet()) {
				try {
					if (checkSiteAlive(map.get(k))) {
						url = map.get(k);
						break;
					}
				} catch (IOException e) {
					Logger.error(e,"IOExceptionn in discoverNewSite: " + map.get(k));
					url = null;
				}
			}
		}
		
		if(url!=null) {
			URL _url = new URL(url);
			url = _url.getProtocol()+"://"+_url.getHost()+":"+_url.getPort()+"/sdo118Artifacts-manager/";
		}

		Logger.info("Url found is: " + url);
		return url;
	}

	private static Map<String, String> getApiSelectorUrls() throws IOException {

		Map<String, String> map = new HashMap<String, String>();
		String resourceName = "sites.properties"; // could also be a constant
		Properties props = new Properties();
		FileInputStream stream = null;
		try {
			// FileUtils.readFileToString();
			File f = new File(System.getProperty("agent.workingDir") + "/etc/" + resourceName);
			if (!f.exists()) {
				Logger.warn("File " + f.getPath() + " not found");
			}
			stream = FileUtils.openInputStream(f);
			props.load(stream);
		} catch (Exception e) {

		} finally {
			if (stream != null)
				stream.close();
		}

		if (props.size() <= 0) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
				props.load(resourceStream);
			}
		}
		props.forEach((k, v) -> {
			map.put(k.toString(), v.toString());
		});

		Logger.info("found " + map.size() + " sites to scan");
		return map;
	}

	// FIXME: appogioare il test di connessione ad okhttp client
	private static boolean checkSiteAlive(String siteUrl) throws IOException {
		Logger.info("check site alive: " + siteUrl);
		OkHttpClient client = provideHttpClient("5000", "5000", "5000");
		String token = "dummy";
		String username= "emsmobile";
		String password= "3m5m0b1l3";
		Request request = new Request.Builder().url(siteUrl)
				.addHeader("X-Authorization-Token", "Bearer " + token)
				.addHeader("authorization", Credentials.basic(username, password)).build();
	
		Response response = client.newCall(request).execute();
		boolean resp = true;
		if (response == null || response.code() != 200) {
			Logger.info("response is:" + response.code() + " - " + response.toString());
			throw new IOException("Error while procesing GET request for health operation " + response.body().string(),
					null);
		}
		
//		boolean resp = response.body().string().contains("UP");
//		Logger.info("response is:" + resp);
		response.close();
		return resp;
		// try {
		// HttpURLConnection conn = (HttpURLConnection) new URL(siteUrl +
		// "/health").openConnection();
		// conn.setConnectTimeout(10000);
		// BufferedInputStream stream = new BufferedInputStream(conn.getInputStream());
		// byte[] buffer = new byte[16];
		// int r = -1;
		// while (0 < (r = stream.read(buffer))) {
		// Logger.info("reading response....");
		// }
		// String response = new String(buffer).trim();
		// Logger.info(response);
		// String ok = "{\"status\":\"UP\"}";
		// return response.equals(ok);
		// } catch (Exception e) {
		// Logger.info("error: " + e.getMessage() + " - " +
		// e.getStackTrace()[e.getStackTrace().length - 1]);
		// return false;
		// } finally {
		//
		// }
	}

}
