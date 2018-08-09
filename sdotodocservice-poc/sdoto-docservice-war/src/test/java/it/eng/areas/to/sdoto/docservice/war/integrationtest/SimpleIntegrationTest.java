/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.war.integrationtest;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.icegreen.greenmail.util.GreenMail;

import it.eng.areas.to.sdoto.docservice.delegate.model.BookingDocumentDTO;
import it.eng.areas.to.sdoto.docservice.rest.service.InsertDocumentRequest;

/**
 * @author Bifulco Luigi
 *
 */

// @RunWith(JunitServerRunner.class)
public class SimpleIntegrationTest {

	private static String baseUrl = "http://127.0.0.1:14545/sdoto-docservice-war/rest";

	@Autowired
	private JavaMailSender emailSender;

	private GreenMail testSmtp;

	// RestTemplate restTemplate = new RestTemplate();

	@Before
	public void testSmtpInit() {
		// testSmtp = new GreenMail(ServerSetupTest.SMTP);
		// testSmtp.start();
		//
		// // don't forget to set the test port!
		// ((JavaMailSenderImpl) emailSender).setPort(3025);
		// ((JavaMailSenderImpl) emailSender).setHost("localhost");
	}

	@Test
	public final void testInvoke() throws RestClientException, URISyntaxException {
		// Assert.assertTrue(false);
//		final String url = String.format("%s/documentService/createDocument", baseUrl);
//
//		RestTemplate restTemplate = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//		InsertDocumentRequest request = new InsertDocumentRequest();
//		request.setDocReference("16063354");
//		request.setParking("CRI LOIANO");
//		HttpEntity<InsertDocumentRequest> requestEntity = new HttpEntity<InsertDocumentRequest>(request, headers);
//		HttpEntity<BookingDocumentDTO> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, BookingDocumentDTO.class);
//		BookingDocumentDTO dto = response.getBody();
//		Assert.assertNotNull(dto);
//		

		// Message[] messages = testSmtp.getReceivedMessages();
		// Assert.assertEquals(1, messages.length);
	}

	// @After
	// public void cleanup() {
	// testSmtp.stop();
	// }

}
