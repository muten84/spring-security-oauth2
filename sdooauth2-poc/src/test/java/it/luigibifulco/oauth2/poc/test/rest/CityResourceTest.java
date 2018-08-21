package it.luigibifulco.oauth2.poc.test.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Lists;

import it.luigibifulco.oauth2.poc.Sdooauth2PocApplication;
import it.luigibifulco.oauth2.poc.jpa.domain.City;
import it.luigibifulco.oauth2.poc.jpa.service.CityRepository;
import it.luigibifulco.oauth2.poc.jpa.service.ICityService;
import it.luigibifulco.oauth2.poc.jpa.service.TransactionalService;

@RunWith(SpringRunner.class)
//@WebMvcTest(CityResource.class)
@WebAppConfiguration
@SpringBootTest(classes = Sdooauth2PocApplication.class)
//@ActiveProfiles("mvc")
/**
 * Usefuel to test a controller or a resource to check if return what expected
 * only the web layer is started not whole server and context so the other
 * layers have to be mocked and these tests has mocked dao and transactional
 * service
 * 
 * @author luigib
 *
 */
public class CityResourceTest {

	@MockBean
	private ICityService service;

	@MockBean
	private CityRepository mockrepo;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	private static final String CLIENT_ID = " SampleClientId";
	private static final String CLIENT_SECRET = "secret";

	private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

//	@Before
//	public void setup() {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilterChain).build();
//	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
	}

	/*
	 * questa classe serve per istanzaire tutti i bean che servono al test contest e
	 * che non si vuole dichiare nel contesto definitivo ad esempio potrei voler
	 * dichiarer dei bean solamente nel test e non nella spring boot app
	 */
	@TestConfiguration
	static class ContextTestContextConfiguration {

		@Bean
		public ICityService transactionalService() {
			return new TransactionalService();
		}
	}

	@Test
	public void unathorized() throws Exception {
		when(service.startsWith("")).thenReturn(null);
		this.mockMvc.perform(get("/dev/cities/startsWith/TEST")).andDo(print()).andExpect(status().is(401));
	}

	@Test
	public void startsWithTest() throws Exception {
		final String accessToken = this.getAccessToken("john", "123");
		// final String accessToken =
		// "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJqb2huIiwic2NvcGUiOlsicmVhZCJdLCJvcmdhbml6YXRpb24iOiJqb2huRUtaTiIsImV4cCI6MTUzNDc4NDYxNiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjFhMTcxMDU5LTQxZTItNDJmOS05ZDQ4LThmZGU1MDM0ZjgwOCIsImNsaWVudF9pZCI6IlNhbXBsZUNsaWVudElkIn0.IdecKOLymwMkOpCYV7xWeLkhe8VBknSiYmrldNrrVQQ";
		// obtainAccessToken("user1", "pass");
		System.out.println("token:" + accessToken);
		when(service.startsWith("TEST")).thenReturn(Lists.newArrayList(new City("TEST", "TEST")));
		this.mockMvc.perform(get("/cities/startsWith/TEST").header("Authorization", "Bearer " + accessToken))
				.andDo(print()).andExpect(status().isOk());
	}

	private String getAccessToken(String username, String password) throws Exception {
		String authorization = "Basic "
				+ new String(Base64Utils.encode(("" + CLIENT_ID + ":" + CLIENT_SECRET).getBytes()));
		String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

		// @formatter:off
		String content = mockMvc
				.perform(post("/oauth/token").header("Authorization", authorization)
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).param("username", username)
						.param("password", password).param("grant_type", "password").param("scope", "read write")
						.param("client_id", CLIENT_ID).param("client_secret", CLIENT_SECRET))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.access_token", is(notNullValue())))
				.andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
				.andExpect(jsonPath("$.refresh_token", is(notNullValue())))
				.andExpect(jsonPath("$.expires_in", is(greaterThan(4000))))
				.andExpect(jsonPath("$.scope", is(equalTo("read write")))).andReturn().getResponse()
				.getContentAsString();

		// @formatter:on

		return content.substring(17, 53);
	}

	private String obtainAccessToken(String username, String password) throws Exception {
		final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "password");
		params.add("client_id", CLIENT_ID);
		params.add("username", username);
		params.add("password", password);

		// @formatter:off

		ResultActions result = mockMvc
				.perform(post("/oauth/token").params(params).with(httpBasic(CLIENT_ID, CLIENT_SECRET))
						.accept(CONTENT_TYPE))
				.andExpect(status().isOk()).andExpect(content().contentType(CONTENT_TYPE));

		// @formatter:on

		String resultString = result.andReturn().getResponse().getContentAsString();

		JacksonJsonParser jsonParser = new JacksonJsonParser();
		return jsonParser.parseMap(resultString).get("access_token").toString();
	}

}
