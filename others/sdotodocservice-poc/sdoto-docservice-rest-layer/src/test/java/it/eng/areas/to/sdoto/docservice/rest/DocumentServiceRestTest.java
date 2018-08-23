/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.easymock.Mock;
import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.area118.sdocommon.print.excpetions.PrintReportException;
import it.eng.areas.ems.ordinari.entity.ToBookingDO;
import it.eng.areas.ems.ordinari.entity.WebIdentityDO;
import it.eng.areas.ems.ordinari.entity.WebSessionDO;
import it.eng.areas.ems.ordinari.service.BookingTransactionalService;
import it.eng.areas.ems.ordinari.service.WebIdentityService;
import it.eng.areas.sdocommon.test.rest.AbstractRestTest;
import it.eng.areas.to.sdoto.docservice.delegate.model.BookingDocumentDTO;
import it.eng.areas.to.sdoto.docservice.delegate.model.BookingDocumentFilter;
import it.eng.areas.to.sdoto.docservice.delegate.notify.NotifyServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.print.PrintBookingsServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.print.model.BookingsPrintDataSource;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentDO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;
import it.eng.areas.to.sdoto.docservice.entity.filter.DepartmentAdresseeFilter;
import it.eng.areas.to.sdoto.docservice.rest.service.AuthResponse;
import it.eng.areas.to.sdoto.docservice.rest.service.ChangeDocumentStatusRequest;
import it.eng.areas.to.sdoto.docservice.rest.service.Credentials;
import it.eng.areas.to.sdoto.docservice.rest.service.InsertDocumentRequest;
import it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService;
import it.esel.parsley.lang.StringUtils;

/**
 * Questa classe rappresenta una suite di test per il rest layer che utilizza il JerseyTest la calsse {@link JerseyTest}
 * offre molte cose già fatte per creare le Junit per i rest service
 * 
 * @author Bifulco Luigi
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestLayerTestCtx.class)
public class DocumentServiceRestTest extends AbstractRestTest {

	@Autowired
	private DocumentTransactionalService persistenceService;

	@Autowired
	private PrintBookingsServiceDelegate printService;

	@Autowired
	private BookingTransactionalService bookingService;

	@Autowired
	private NotifyServiceDelegate notifierService;

	@Autowired
	private WebIdentityService webIdentityService;

	protected Application configure() {

		MockitoAnnotations.initMocks(this);

		return new JerseyConfig();

	}

	@Test
	public final void testInsertDocument() throws InterruptedException, FileNotFoundException, PrintReportException, DataAccessException {
		// TODO: fininre questo qui
		// https://github.com/jersey/jersey/blob/2.23.2/examples/helloworld-spring-annotations/src/main/java/org/glassfish/jersey/examples/hello/spring/annotations/JerseyConfig.java

		Assert.assertNotNull("DocumentTransactionalService is null", persistenceService);
		Assert.assertNotNull("PrintBookingsServiceDelegate is null", printService);

		/**
		 * istruisco il mock object
		 */
		Mockito.reset(persistenceService);
		MockingDetails det = Mockito.mockingDetails(persistenceService);
		Assert.assertTrue(det.isMock());

		Mockito.when(printService.createBookingDocument(Mockito.any(BookingsPrintDataSource.class))).thenAnswer(new Answer<byte[]>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.mockito.stubbing.Answer#answer(org.mockito.invocation.InvocationOnMock)
			 */
			@Override
			public byte[] answer(InvocationOnMock invocation) throws Throwable {
				return new byte[] { 125 };
			}
		});
		BookingDocumentDO entity = new BookingDocumentDO();
		entity.setParking("CRI LOIANO");
		entity.setBookingCode("17000114");
		entity.setCreationDate(Calendar.getInstance().getTime());
		entity.setState(BookingDocumentState.CREATED);
		entity.setCloseDate(Calendar.getInstance().getTime());
		Mockito.when(persistenceService.createDocument(Mockito.anyString(), Mockito.anyString(), Mockito.any(byte[].class))).thenReturn(entity);
		//

		ToBookingDO booking = new ToBookingDO();
		booking.setCode("17000114");
		booking.setOptionedParking("CRI LOIANO");
		Mockito.when(bookingService.getBookingByCode(Mockito.anyString(), Mockito.anyString())).thenReturn(booking);

		final WebTarget target = target("documentService/createDocument");
		InsertDocumentRequest insert = new InsertDocumentRequest();
		insert.setParking("TEST");
		insert.setDocReference("12345678");
		Entity<InsertDocumentRequest> data = Entity.entity(insert, MediaType.APPLICATION_JSON);
		Response response = target.request().put(data);
		BookingDocumentDTO dataResponse = response.readEntity(BookingDocumentDTO.class);
		Assert.assertTrue(response.getStatus() == 200);
		Mockito.verify(persistenceService, Mockito.atMost(1)).createDocument(Mockito.anyString(), Mockito.any(byte[].class));
		Assert.assertTrue(dataResponse != null);
		Assert.assertTrue(dataResponse.getParking().equals("CRI LOIANO"));
		Assert.assertTrue(dataResponse.getDocReference().equals("17000114"));
		Assert.assertTrue(dataResponse.getState() == BookingDocumentState.CREATED);
	}

	@Test
	public final void testCreateAndSend() throws DataAccessException, FileNotFoundException, PrintReportException {
		Assert.assertNotNull("DocumentTransactionalService is null", persistenceService);
		Assert.assertNotNull("PrintBookingsServiceDelegate is null", printService);

		/**
		 * istruisco il mock object
		 */
		Mockito.reset(persistenceService);
		MockingDetails det = Mockito.mockingDetails(persistenceService);
		Assert.assertTrue(det.isMock());

		Mockito.when(printService.createBookingDocument(Mockito.any(BookingsPrintDataSource.class))).thenAnswer(new Answer<byte[]>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.mockito.stubbing.Answer#answer(org.mockito.invocation.InvocationOnMock)
			 */
			@Override
			public byte[] answer(InvocationOnMock invocation) throws Throwable {
				return new byte[] { 125 };
			}
		});

		BookingDocumentDO entity = DocumentServiceJerseyTestHelper.createBookingDocumentData("TEST", "TEST", "12345678", BookingDocumentState.CREATED, null);
		Mockito.when(persistenceService.createDocument(Mockito.anyString(), Mockito.anyString(), Mockito.any(byte[].class))).thenReturn(entity);
		//

		ToBookingDO booking = new ToBookingDO();
		booking.setCode("12345678");
		booking.setOptionedParking("TEST");
		Mockito.when(bookingService.getBookingByCode(Mockito.anyString(), Mockito.anyString())).thenReturn(booking);

		Mockito.when(persistenceService.searchDepartmentAdressees(Mockito.any(DepartmentAdresseeFilter.class))).thenReturn(DocumentServiceJerseyTestHelper.stubDepartmentAdresses("TEST", "user@domain.com"));

		BookingDocumentDO entity2 = DocumentServiceJerseyTestHelper.createBookingDocumentData("TEST2", "TEST2", "12345678", BookingDocumentState.CLOSED, null);
		List<BookingDocumentDO> listDocs = Arrays.asList(new BookingDocumentDO[] { //
				entity2, //
		});
		Mockito.when(persistenceService.searchBookingDocuments(Mockito.any(it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter.class))).thenReturn(listDocs);

		Mockito.when(notifierService.sendSimpleData(Mockito.any(String[].class), Mockito.any(String.class))).thenReturn(true);

		BookingDocumentDO sent = DocumentServiceJerseyTestHelper.createBookingDocumentData("TEST", "TEST", "12345678", BookingDocumentState.SENT, null);
		Mockito.when(persistenceService.changeDocumentState(Mockito.anyString(), Mockito.any(BookingDocumentState.class), Mockito.any(Date.class), Mockito.anyString())).thenReturn(sent);

		final WebTarget target = target("documentService/createAndSendDocument");

		InsertDocumentRequest insert = new InsertDocumentRequest();
		insert.setParking("TEST");
		insert.setDocReference("12345678");
		Entity<InsertDocumentRequest> data = Entity.entity(insert, MediaType.APPLICATION_JSON);
		String value = "Bearer " + Base64.encodeAsString(UUID.randomUUID().toString());
		Response response = target.request().header("Authorization", value).put(data);
		BookingDocumentDTO dataResponse = response.readEntity(BookingDocumentDTO.class);
		Assert.assertTrue("Response status is: " + response.getStatus(), response.getStatus() == 200);
		Mockito.verify(persistenceService, Mockito.atMost(1)).createDocument(Mockito.anyString(), Mockito.any(byte[].class));
		Assert.assertTrue(dataResponse != null);
		Assert.assertTrue(dataResponse.getParking().equals("TEST"));
		Assert.assertTrue(dataResponse.getDocReference().equals("12345678"));
		Assert.assertTrue(dataResponse.getState() == BookingDocumentState.SENT);
	}

	@Test
	public final void testOpenDocument() {
		// BookingDocumentDO stub = new BookingDocumentDO();
		// stub.setId("TEST");
		// Mockito.reset(persistenceService);
		// Mockito.when(persistenceService.getBookingDocument("TEST")).thenReturn(stub);
		//
		// final WebTarget target = target("documentService/openDocument/TEST");
		// String value = "Bearer " + Base64.encodeAsString("USER:"+UUID.randomUUID().toString());
		// Response response = target.request().header("Authorization", value).get();
		// Assert.assertTrue("Response is: " + response.getStatus(), response.getStatus() == 200);
	}

	@Test
	public final void testGetDocumentById() {
		BookingDocumentDO stub = new BookingDocumentDO();
		stub.setId("TEST");
		Mockito.reset(persistenceService);
		Mockito.when(persistenceService.getBookingDocument("TEST")).thenReturn(stub);

		final WebTarget target = target("documentService/getDocument/TEST");
		Response response = target.request().get();
		Assert.assertTrue("Response is: " + response.getStatus(), response.getStatus() == 200);
		BookingDocumentDTO dto = response.readEntity(BookingDocumentDTO.class);

		Mockito.verify(persistenceService, Mockito.atMost(1)).getBookingDocument("TEST");
		Assert.assertTrue(dto != null);
		Assert.assertTrue(dto.getId().equals("TEST"));
	}

	@Test
	public final void testChangeDocumentStatus() {
		BookingDocumentDO stub = new BookingDocumentDO();
		stub.setId("TEST");
		stub.setState(BookingDocumentState.OPENED);
		Mockito.reset(persistenceService);
		Mockito.when(persistenceService.changeDocumentState(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(stub);

		final WebTarget target = target("documentService/changeDocumentStatus");
		ChangeDocumentStatusRequest request = new ChangeDocumentStatusRequest();
		request.setDocumentId("TEST");
		request.setNewState(BookingDocumentState.OPENED.name());
		Entity<ChangeDocumentStatusRequest> reqEntity = Entity.entity(request, MediaType.APPLICATION_JSON);
		Response response = target.request().post(reqEntity);
		BookingDocumentDTO dto = response.readEntity(BookingDocumentDTO.class);
		Assert.assertTrue(dto != null);
		Assert.assertTrue(dto.getId().equals("TEST"));
		Assert.assertTrue(dto.getState() == BookingDocumentState.OPENED);
	}

	@Test
	public final void testAuthentication() {
		WebIdentityDO w = new WebIdentityDO();
		w.setId("TEST");
		w.setLogin("ADMIN");
		w.setPasswd("ADMIN");

		WebSessionDO s = new WebSessionDO();
		s.setId(w.getId());
		s.setLoginTimestamp(Calendar.getInstance().getTime());
		s.setSessionId("ADMIN:TOKEN");
		s.setWebIdentityId(w.getId());

		Mockito.reset(webIdentityService);
		Mockito.when(webIdentityService.getUser(Mockito.anyString())).thenReturn(w);
		Mockito.when(webIdentityService.createSession(Mockito.anyString(), Mockito.anyString())).thenReturn(s);

		final WebTarget target = target("documentService/authenticateUser");
		Credentials userPass = new Credentials();
		userPass.setUsername("ADMIN");
		userPass.setPassword("ADMIN");
		Entity<Credentials> entityFilter = Entity.entity(userPass, MediaType.APPLICATION_JSON);
		Response response = target.request().post(entityFilter);
		AuthResponse authResp = response.readEntity(AuthResponse.class);
		Assert.assertTrue(authResp.isAuthenticated());
		Assert.assertTrue(!StringUtils.isEmpty(authResp.getToken()));
		Assert.assertNotNull(Base64.decodeAsString(authResp.getToken()).split(":")[0]);
	}

	@Test
	public final void testListDocuments() {
		List<BookingDocumentDO> listDocs = Arrays.asList(new BookingDocumentDO[] { //
				new BookingDocumentDO("TEST", "TEST", BookingDocumentState.CREATED), //
				new BookingDocumentDO("TEST2", "TEST2", BookingDocumentState.OPENED), });
		Mockito.reset(persistenceService);
		Mockito.when(persistenceService.searchBookingDocuments(Mockito.any(it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter.class)))//
				.thenReturn(listDocs);

		final WebTarget target = target("documentService/listDocuments");

		BookingDocumentFilter filter = new BookingDocumentFilter();
		filter.setParking("TEST");
		Entity<BookingDocumentFilter> entityFilter = Entity.entity(filter, MediaType.APPLICATION_JSON);
		Response response = target.request().post(entityFilter);
		List<BookingDocumentDTO> list = response.readEntity(getListGenericType(BookingDocumentDTO.class));
		Assert.assertTrue(!list.isEmpty());
	}

	@Test
	public final void testTestMethod() {
		final WebTarget target = target("documentService/test");
		String value = "Bearer " + Base64.encodeAsString("USER:" + UUID.randomUUID().toString());
		String response = (String) target.request().header("Authorization", value).get().readEntity(String.class);
		Assert.assertTrue(response.equals("Got it: user is USER"));
	}

	public static ResourceConfig createApp() {
		return new ResourceConfig().packages("it.eng.areas.to.sdoto.docservice.rest.service")//
				.register(JacksonJsonProvider.class)//
				.register(JacksonFeature.class);
		// .register(createMoxyJsonResolver());
	}

	// public static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
	// final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
	// Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
	// namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
	// moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
	// return moxyJsonConfig.resolver();
	// }

	// @Override
	// protected void configureClient(ClientConfig config) {
	// config.register(createMoxyJsonResolver());
	// }

}
