/**
 * 
 */
package it.eng.areas.ordinari.aaa;

import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import it.eng.areas.ordinari.aaa.model.AAARequest;
import it.eng.areas.ordinari.aaa.model.AAAResponse;

@RunWith(SpringRunner.class)
@Import(TestConfigurationCtx.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AAAModuleControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private JsonParser jsonParser;

	public static final String USERNAME = "ADMIN";
	public static final String PASSWORD = "ADMIN";

	@Test
	@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = "delete from cp_session")
	public void authenticationTestWithNonSecureDomain() {
		String roleToUse = "ROLE_ADMIN";
		HttpEntity<AAARequest> entity = createAuthenticatedRequest(USERNAME, PASSWORD, new AAARequest(), roleToUse);
		String url = "/api/unsecure/currentUser";
		ResponseEntity<AAAResponse> result = restTemplate.exchange(url, HttpMethod.POST, entity, AAAResponse.class);
		AAAResponse response = result.getBody();
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getResult().equals(USERNAME));
	}

	@Test
	@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = "delete from cp_session")
	public void shouldNotBlockNotAuthenticatedUserOnUnsecureDomain() {
		HttpEntity<AAARequest> entity = createNonAuthenticatedRequest(USERNAME, PASSWORD, new AAARequest(), null);
		String url = "/api/unsecure/currentUser";
		ResponseEntity<AAAResponse> result = restTemplate.exchange(url, HttpMethod.POST, entity, AAAResponse.class);

		AAAResponse response = result.getBody();
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getResult().equals("OK NO AUTH"));
	}

	@Test
	@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = "delete from cp_session")
	public void authenticationTestWithSecureDomain() {
		HttpEntity<AAARequest> entity = createAuthenticatedRequest(USERNAME, PASSWORD, new AAARequest(), null);
		String url = "/api/secure/admin";
		ResponseEntity<AAAResponse> result = restTemplate.exchange(url, HttpMethod.POST, entity, AAAResponse.class);
		AAAResponse response = result.getBody();
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getResult().equals(USERNAME));
	}

	@Test
	@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = "delete from cp_session")
	public void authenticationTestWithSecureDomainAndUSerRole() {
		HttpEntity<AAARequest> entity = createAuthenticatedRequest(USERNAME, PASSWORD, new AAARequest(), null);
		String url = "/api/secure/user";
		ResponseEntity<AAAResponse> result = restTemplate.exchange(url, HttpMethod.POST, entity, AAAResponse.class);

		Assert.assertTrue(result.getStatusCodeValue() == 403);

	}

	@Test
	@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = "delete from cp_session")
	public void shouldBlockNotAuthenticatedUser() {
		HttpEntity<AAARequest> entity = createNonAuthenticatedRequest(USERNAME, PASSWORD, new AAARequest(), null);
		String url = "/api/secure/user";
		ResponseEntity<AAAResponse> result = restTemplate.exchange(url, HttpMethod.POST, entity, AAAResponse.class);

		Assert.assertTrue(result.getStatusCodeValue() == 403);

	}

	public <R> HttpEntity<R> createNonAuthenticatedRequest(String sUser, String pwd, R request, String... roles) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<R> entity = new HttpEntity<R>(request, headers);
		return entity;
	}

	public <R> HttpEntity<R> createAuthenticatedRequest(String sUser, String pwd, R request, String... roles) {

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("username", sUser);
		map.add("password", pwd);
		String tokenResult = restTemplate.postForObject("/login", map, String.class);
		Map<String, Object> value = jsonParser.parseMap(tokenResult);
		String token = value.get("token").toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		headers.add("Authorization", "Bearer " + token.trim());

		HttpEntity<R> entity = new HttpEntity<R>(request, headers);
		return entity;
	}

}
