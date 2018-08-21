package it.luigibifulco.oauth2.poc.test.webapp;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.client.test.OAuth2ContextSetup;
import org.springframework.security.oauth2.client.test.RestTemplateHolder;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import it.luigibifulco.oauth2.poc.jpa.domain.City;
import it.luigibifulco.oauth2.poc.test.webapp.WebAppTest.MyLessTrustedClient;

/**
 * The wohle server is started useful for integration test on the resources and
 * api
 * 
 * @author luigib
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@OAuth2ContextConfiguration(MyLessTrustedClient.class)
public class WebAppTest implements RestTemplateHolder {

	@LocalServerPort
	private int port;

	private RestTemplate restTemplate;

	@Rule
	public OAuth2ContextSetup context = OAuth2ContextSetup.standard(this);

	@Test
	public void unauthorized() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/prod/cities/startsWith/TEST",
				String.class)).contains("unauthorized");
	}

	@Test
	public void authorized_startsWith_empty() throws Exception {
		String url = "http://localhost:" + port + "/dev/cities/startsWith/TEST";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/it.luigibifulco.oauth2.app-v1+json");
		headers.set("Content-Type", "application/it.luigibifulco.oauth2.app-v1+json");

		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		assertThat(result.getBody()).contains("[]");
	}

	@Test(expected = HttpClientErrorException.class)
	public void authorozied_create_empty_resource() throws Exception {
		String url = "http://localhost:" + port + "/dev/cities";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/it.luigibifulco.oauth2.app-v1+json");
		headers.set("Content-Type", "application/it.luigibifulco.oauth2.app-v1+json");

		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		ResponseEntity<City> result = restTemplate.exchange(url, HttpMethod.POST, entity, City.class);
	}

	@Override
	public void setRestTemplate(RestOperations restTemplate) {

		// this.restTemplate = new TestRestTemplate(new RestTemplateBuilder());
		this.restTemplate = (RestTemplate) restTemplate;
	}

	@Override
	public RestOperations getRestTemplate() {
		// return this.restTemplate.getRestTemplate();
		return this.restTemplate;
	}

	public static class MyLessTrustedClient extends ResourceOwnerPasswordResourceDetails {
		public MyLessTrustedClient(Object target) {
			super();
			setClientId("SampleClientId");
			setScope(Arrays.asList("read"));
			setId(getClientId());
			setClientSecret("secret");
			setGrantType("password");
			setUsername("john");
			setPassword("123");
			WebAppTest test = (WebAppTest) target;
			setAccessTokenUri("http://127.0.0.1:" + test.port + "/dev/oauth/token");
			// setUserAuthorizationUri("http://127.0.0.1:" + test.port +
			// "/dev/oauth/authorize");
		}
	}

}
