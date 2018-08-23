/**
 * 
 */
package it.eng.areas.ems.mobileagent.message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.pmw.tinylog.Logger;

import dagger.Lazy;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;
import it.eng.areas.ems.mobileagent.message.db.DocumentStore;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ConfigurationParameter;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ConfigurationSection;
import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;
import it.eng.areas.ems.sdoemsrepo.delegate.model.MobileResource;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Bifulco Luigi
 *
 */
// TODO: esternalizzare timeout di connessione, esternalizzare starvation dei
// messaggi
public class MessageService {

	private final static int DEFAULT_TTL = 100;
	private final static long DEFAULT_STARVATION_IN_SECONDS = 300;
	private final static long DEFAULT_TIMEOUT_IN_MILLIS = 120000;

	private MessageProcessor processor;

	private DocumentStore documentStore;

	private OkHttpClient client;

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	// private ObjectMapper jsonMapper = new ObjectMapper();

	private JsonMapper jsonMapper;

	private String basePath;

	private Lazy<OkHttpClient> clientFactory;
	
	private String token;
	private String username;
	private String password;

	@Inject
	public MessageService(@Named("agent.message.outbound.url") String messageServiceUrl, //
			@Named("agent.message.outbound.defaultTimeoutInMillis") String defaultMessageTimeoutINMillis, //
			JsonMapper jsonMapper, Lazy<OkHttpClient> clientFactory, DocumentStore documentStore) {
		this.jsonMapper = jsonMapper;
		this.processor = new MessageProcessor(Long.parseLong(defaultMessageTimeoutINMillis));
		this.client = clientFactory.get();
		this.clientFactory  = clientFactory;
		// this.client = new OkHttpClient();
		// this.client =
		// this.client.newBuilder().connectTimeout(DEFAULT_TIMEOUT_IN_MILLIS,
		// TimeUnit.MILLISECONDS)
		// .readTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
		// .writeTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS).build();
		this.basePath = messageServiceUrl;
		this.documentStore = documentStore;
		Logger.info("creating new instance of MessageService");
	}

	private static InetAddress getConnectToInetAddress(Proxy proxy, HttpUrl url) throws IOException {
		if (proxy == null || proxy.type() == Type.DIRECT) {
			return InetAddress.getByName(url.host());
		}
		return ((InetSocketAddress) proxy.address()).getAddress();
	}

	public static String login(String apiBaseUrl, String username, String password)
			throws IOException, MessageProcessingException {
		RequestBody body = RequestBody.create(JSON, "{}");
		OkHttpClient client = new OkHttpClient();
		client.newBuilder().connectTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
				.readTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
				.writeTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS).build();
		Request request = new Request.Builder().url(apiBaseUrl + "/api/rest/auth/login/").post(body)
				.addHeader("Authorization", Credentials.basic(username, password))
				.addHeader("Content-Type", "Basic YTph").build();

		Response response = client.newCall(request).execute();
		if ((response == null || response.code() != 200)) {
			throw new MessageProcessingException(null,
					"Error while procesing GET request for siteinfo operation " + response.body().string(), null);
		}
		// return jsonMapper.parse(response.body().string(), SiteInfo.class);

		return response.body().string();
	}

	public static String getSiteInfo(String apiBaseUrl, String siteId, String username, String password, String token)
			throws IOException, MessageProcessingException {
		// ObjectMapper jsonMapper = new ObjectMapper();
		OkHttpClient client = new OkHttpClient();
		client.newBuilder().connectTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
				.readTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
				.writeTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS).
				// authenticator(new Authenticator() {
				//
				// @Override
				// public Request authenticate(Route route, Response response) throws
				// IOException {
				// String credential = Credentials.basic("emsmobile", "3m5m0b1l3");
				// if (response.request().header("Authorization") != null) {
				// return null; // Give up, we've already attempted to authenticate.
				// }
				// System.out.println("Authenticating for response: " + response);
				// System.out.println("Challenges: " + response.challenges());
				// return response.request().newBuilder().header("Authorization",
				// credential).build();
				// }
				// }).
				build();
		Request request = new Request.Builder().url(apiBaseUrl + "/api/rest/resource/siteinfo/" + siteId)
				.addHeader("X-Authorization-Token", "Bearer " + token)
				.addHeader("authorization", Credentials.basic(username, password)).build();

		Response response = client.newCall(request).execute();
		if ((response == null || response.code() != 200)) {
			throw new MessageProcessingException(null,
					"Error while procesing GET request for siteinfo operation " + response.body().string(), null);
		}
		// return jsonMapper.parse(response.body().string(), SiteInfo.class);
		return response.body().string();
	}

	public String getXhr(String url) throws IOException, MessageProcessingException {
		// ObjectMapper jsonMapper = new ObjectMapper();
		OkHttpClient client = new OkHttpClient();
		client.newBuilder().connectTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
				.readTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
				.writeTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS).build();
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		if (response == null || response.code() != 200) {
			throw new MessageProcessingException(null,
					"Error while procesing GET request for siteinfo operation " + response.body().string(), null);
		}
		// return jsonMapper.parse(response.body().string(), SiteInfo.class);
		return response.body().string();
	}

	public MessageService(MessageProcessor processor, String baseUrl, String restPath) {
		this.processor = processor;
		this.client = new OkHttpClient();
		this.client = this.client.newBuilder().connectTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
				.readTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
				.writeTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS).build();

		this.basePath = baseUrl + "/" + restPath;
		this.documentStore = new DocumentStore();
	}

	public MessageService(String messageServiceUrl, long defaultMessageTimeoutINMillis) {
		this.processor = new MessageProcessor(defaultMessageTimeoutINMillis);
		this.client = new OkHttpClient();
		this.client = this.client.newBuilder().connectTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
				.readTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
				.writeTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS).build();

		this.basePath = messageServiceUrl;
		this.documentStore = new DocumentStore();
	}

	public MessageService(MessageProcessor processor, String messageServiceUrl) {
		this.processor = processor;
		this.client = new OkHttpClient();
		this.client = this.client.newBuilder().connectTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
				.readTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
				.writeTimeout(DEFAULT_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS).build();

		this.basePath = messageServiceUrl;
		this.documentStore = new DocumentStore();
	}

	public String extractLocalResource(String type, boolean configuration) throws MessageProcessingException {
		return this.processor.invoke(type, 30000, new RequestHandler<String, String>() {

			@Override
			public String handle(String type) throws Exception {
				if (configuration) {
					return jsonMapper.stringify(documentStore.getLocalConfiguration());
				}
				return jsonMapper.stringify(documentStore.listResourceByType(type));

			}

		});
	}

	// TODO in realtà questo metodo dovrebbe stare in un resource service ma
	// di fatto si comporta come una specifica send synch message con risposta
	public String extractResource(String type, boolean configuration) throws MessageProcessingException {
		if (!configuration) {
			return this.processor.invoke(type, 30000, new RequestHandler<String, String>() {

				@Override
				public String handle(String type) throws Exception {
					String result = null;
					String op = "resource/" + type;
					try {
						result = doGetRequest(basePath, op);
					} catch (Exception e) {
						Logger.error(e,"Exception in extractResource");
					}
					try {
						if (result == null || result.isEmpty()) {
							return jsonMapper.stringify(documentStore.listResourceByType(type));
						} else {
							MobileResource[] resources = jsonMapper.parse(result, MobileResource[].class);
							List<MobileResource> list = Arrays.asList(resources);
							documentStore.updateResourceList(list, type);
							return result;
						}
					} catch (Exception e) {
						Logger.error(e,"Exception in extractResource");
						throw new Exception(e);
					}
				}

			});
		} else {
			return this.processor.invoke(type, 30000, new RequestHandler<String, String>() {

				@Override
				public String handle(String m) throws Exception {
					String result = null;
					String op = "resource/configuration/" + type;
					try {
						result = doGetRequest(basePath, op);
					} catch (Exception e) {
						Logger.error(e,"Exception in extractResource");
					}
					if (result == null || result.isEmpty()) {
						return jsonMapper.stringify(documentStore.getLocalConfiguration());
					} else {
						String jsonString = jsonMapper.parse(result, String.class);
						ConfigurationSection[] sections = jsonMapper.parse(jsonString, ConfigurationSection[].class);
						List<ConfigurationSection> list = Arrays.asList(sections);
						documentStore.updateConfigurationDocument(list);
						return result;
					}

				}
			});
		}
	}

	public String getResourceVersion() throws MessageProcessingException {

		return this.processor.invoke("", 30000, new RequestHandler<String, String>() {

			@Override
			public String handle(String type) throws Exception {
				String result = null;
				try {
					result = doGetRequest(basePath, "getResourceVersion/").trim();
				} catch (Exception e) {
					Logger.error(e,"Exception in getResourceVersion");
					result = "\"{}\"";
				}
				return result;
			}

		});
	}

	public String sendMessage(String s, long timeout) throws MessageProcessingException {

		return this.processor.invoke(s, timeout, new RequestHandler<String, String>() {

			@Override
			public String handle(String messsage) throws Exception {
				String response = doPostRequest(basePath, "rpc", messsage);
				return response;
			}
		});
	}

	public Message sendStructMessage(Message m) throws MessageProcessingException {
		if (m.getTimeout() <= 0) {
			m.setTimeout(10000);
		}

		return this.processor.sendSynch(m, m.getTimeout(), new MessageHandler() {

			@Override
			public Message handle(Message m) throws Exception {
				String payload = jsonMapper.stringify(m);
				String response = doPostRequest(basePath, "rpc", payload);
				return jsonMapper.parse(response, Message.class);
			}
		});
	}

	/**
	 * Accoda il messaggio in un processore di messaggi contiene la logica di retry
	 * e di controllo di aging e di verifica del TTL dei messaggi, inoltre si
	 * preoccupe di persistire in un documentDb lo stato del messaggio.
	 * 
	 * @param m
	 * @return true se il messaggio è stato preso in carico dall'agent
	 * @throws MessageProcessingException
	 */
	public boolean enqueueMessage(Message m) throws MessageProcessingException {
		// metto il messaggio in db
		this.documentStore.storeNewMessage(m);

		// lo accodo nel processore dei messaggi
		return this.processor.sendAsynch(m, new MessageHandler() {

			@Override
			public Message handle(Message m) throws Exception {
				String payload = jsonMapper.stringify(m);
				String response = null;
				// verifico che non sono in fase di retry e che non ho superato il numero
				// massimo di tentativi
				if (m.isRetry() && m.getTtl() > DEFAULT_TTL) {
					// TODO tentato il tutto per tutto ma il messaggio non è stato possibile
					// trasmetterlo
					// restituire un messaggio dummy di risposta o null
					return null;
				}
				// try {
				// verifico che la data del messaggio non sia troppo vecchia per non creare
				// starvation
				long aging = new Date(m.getTimestamp()).toInstant().until(Calendar.getInstance().getTime().toInstant(),
						ChronoUnit.SECONDS);
				if (aging <= DEFAULT_STARVATION_IN_SECONDS) {
					// response = doPostRequest(basePath, "sendMessage", payload);
					doPostAsynch(basePath, "sendMessage", payload, new Callback() {

						@Override
						public void onResponse(Call call, Response response) throws IOException {
							// TODO Auto-generated method stub
							if (response == null) {
								documentStore.setMessageNotProcessed(m.getId());
								throw new IOException("no payload received in response for message " + m.getId());
							}
							String respBody = response.body().string();
							Message mResp = jsonMapper.parse(respBody, Message.class);
							documentStore.setMessageProcessed(m.getId());
							// return mResp;

						}

						@Override
						public void onFailure(Call call, IOException e) {
							// TODO Auto-generated method stub
							m.setRetry(true);
							m.setTtl(m.getTtl() + 1);
							m.setProcessed(false);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e2) {
								Logger.error(e2,"Exception in onFailure");
							}
							try {
								handle(m);
							} catch (Exception e1) {
								Logger.error(e1,"Exception in onFailure");
							}

						}
					});
				} else {
					// se il messaggio è troppo vecchio lo salvo come non processato e lancio una
					// eccezione
					documentStore.setMessageNotProcessed(m.getId());
					throw new TimeoutException(
							"message aging too long the message " + m.getId() + " is not more processable");
				}
				// }
				// catch (MessageProcessingException mpe) {
				// // se qualcosa è andato storto aumento il TTL e imposto il messaggio in
				// modalità
				// // retry
				// m.setRetry(true);
				// m.setTtl(m.getTtl() + 1);
				// m.setProcessed(false);
				// return this.handle(m);
				// }

				// se sono arrivato qui e la risposta è nulla vuole dire che la trasmissione è
				// andata a buon
				// fine ma il payload non è arrivato per un problema applicativo
				// setto cmq il messaggio come non processato.
				// if (response == null) {
				// documentStore.setMessageNotProcessed(m.getId());
				// throw new NullPointerException("no payload received in response for message "
				// + m.getId());
				// }
				//
				// Message mResp = jsonMapper.parse(response, Message.class);
				// documentStore.setMessageProcessed(m.getId());
				// return mResp;
				return null;
			}
		});
	}

	protected String doGetRequest(String url, String op) throws IOException, MessageProcessingException {
		Logger.info("--> GET: "+url+" - "+op);
		if (StringUtils.isEmpty(this.token)) {
			this.token = MessageService.login(System.getProperty("agent.apiSelector.baseUrl"), username, password);
		}
		Request request = new Request.Builder().url(url + "/" + op)
				.addHeader("X-Authorization-Token", "Bearer " + this.token)
				.addHeader("authorization", Credentials.basic(username, password)).build();
		Response response = client.newCall(request).execute();
		if (response == null || response.code() != 200) {
			throw new MessageProcessingException(null,
					"Error while procesing GET request for " + op + " operation " + response.body().string(), null);
		}
		return response.body().string();
	}

	protected void doPostAsynch(String url, String op, String json, Callback cb)
			throws IOException, MessageProcessingException {
		Logger.info("--> POST: "+url+" - "+op+": "+json);
		json = json.replaceAll("\"_", "\"");
		if (StringUtils.isEmpty(this.token)) {
			this.token = MessageService.login(System.getProperty("agent.apiSelector.baseUrl"), username, password);
		}
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url + "/" + op).post(body)
				.addHeader("X-Authorization-Token", "Bearer " + this.token)
				.addHeader("authorization", Credentials.basic(this.username, this.password)).build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if (response.code() != 200) {
					cb.onFailure(call, new IOException("response is not 200"));
					return;
				}
				cb.onResponse(call, response);

			}

			@Override
			public void onFailure(Call call, IOException e) {
				cb.onFailure(call, e);

			}
		});
	}
	
	public String dispatch(String method, String op, String json) throws IOException, MessageProcessingException {
		switch(method) {
			case "GET":
				return this.doGetRequest(basePath, op);
			case "POST":
				return this.doPostRequest(basePath, op, json);
		}
		return null;
	}

	protected String doPostRequest(String url, String op, String json) throws IOException, MessageProcessingException {
		Logger.info("--> POST: "+url+" - "+op+": "+json);
		json = json.replaceAll("\"_", "\"");
		if (StringUtils.isEmpty(this.token)) {
			this.token = MessageService.login(System.getProperty("agent.apiSelector.baseUrl"), username, password);
		}
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url + "/" + op).post(body)
				.addHeader("X-Authorization-Token", "Bearer " + this.token)
				.addHeader("authorization", Credentials.basic(this.username, this.password))
				.addHeader("Content-Type", "Basic YTph").build();
		Response response =null;
		try {
		response = client.newCall(request).execute();
		}catch(IOException e) {
			response = null;
			Logger.error(e,"Error during request/response process: "+getClientInfo());
			throw e;
		}
		if (response == null || response.code() != 200) {
			throw new MessageProcessingException(null,
					"Error while procesing POST request for " + op + " operation " + response.body().string(), null);
		}
		return response.body().string();
	}
	
	private String getClientInfo() {
		StringBuffer s = new StringBuffer();
		s.append("readTimeoutMillis: "+ client.readTimeoutMillis());
		s.append('\n');
		s.append("connectTimeoutMillis: "+ client.connectTimeoutMillis());
		s.append('\n');
		s.append("writeTimeoutMillis: "+ client.writeTimeoutMillis());
		return s.toString();
		
	}

	private static ConfigurationSection createConfigurationSection(String id) {
		ConfigurationSection a = new ConfigurationSection();
		a.setName("SECTION" + id);
		ConfigurationParameter p = new ConfigurationParameter();
		p.setName("p1");
		p.setValue("asdasdasd");
		ConfigurationParameter p2 = new ConfigurationParameter();
		p.setName("p2");
		p.setValue("asdasdasd");
		a.setParameter(Arrays.asList(new ConfigurationParameter[] { p, p2 }));
		return a;
	}

	public boolean resetDocumentStore(String type) {
		return this.documentStore.resetDocumentStore(type);
	}

	public DocumentStore getDocumentStore() {
		return this.documentStore;
	}

	public boolean closeMessageService() {
		this.processor.shutdownMessageProcessor();
		this.client = null;
		this.documentStore = null;
		this.jsonMapper = null;
		this.processor = null;
		return true;
	}

	public static void main(String[] args) throws IOException {

		// ConfigurationSection s1 = createConfigurationSection("1");
		// ConfigurationSection s2 = createConfigurationSection("2");
		//
		// List<ConfigurationSection> sections = Arrays.asList(new
		// ConfigurationSection[] { s1, s2 });
		//
		// ObjectMapper mapper = new ObjectMapper();
		// String result = mapper.stringify(sections);
		// Logger.info(result);
		//
		// String conf = "";
		// DocumentStore store = new DocumentStore();
		// ConfigurationSection[] sects = mapper.parse(conf,
		// ConfigurationSection[].class);
		// store.updateConfigurationDocument(Arrays.asList(sects));

	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
