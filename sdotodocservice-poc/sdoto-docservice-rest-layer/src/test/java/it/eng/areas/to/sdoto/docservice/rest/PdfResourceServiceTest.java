/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.bouncycastle.util.Arrays;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.areas.sdocommon.test.rest.AbstractRestTest;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentDO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;
import it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService;

/**
 * @author Bifulco Luigi
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestLayerTestCtx.class)
public class PdfResourceServiceTest extends AbstractRestTest {

	@Autowired
	private DocumentTransactionalService persistenceService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.sdocommon.test.rest.AbstractRestTest#configure()
	 */
	@Override
	protected Application configure() {
		MockitoAnnotations.initMocks(this);

		return new JerseyConfig();
	}

	@Test
	public final void testGetPdfFromRepository() throws IOException {

		BookingDocumentDO stub = new BookingDocumentDO();
		stub.setId("TEST");
		stub.setState(BookingDocumentState.CREATED);
		stub.setBookingCode("12345678");
		stub.setParking("TEST");
		stub.setDocument(new byte[] { 125, 126, 127 });
		Mockito.when(persistenceService.getBookingDocument("TEST")).thenReturn(stub);
		// EasyMock.expect(persistenceService.getBookingDocument("TEST")).andReturn(stub);
		// EasyMock.replay(persistenceService);

		String docId = "TEST";
		final WebTarget target = target("repository/getPdf/" + docId);
		Response response = target.request().get();
		Assert.assertTrue(response.getStatus() == 200);
		InputStream stream = response.readEntity(InputStream.class);
		byte[] toRead = new byte[3];
		stream.read(toRead);
		Arrays.areEqual(stub.getDocument(), toRead);
	}

};