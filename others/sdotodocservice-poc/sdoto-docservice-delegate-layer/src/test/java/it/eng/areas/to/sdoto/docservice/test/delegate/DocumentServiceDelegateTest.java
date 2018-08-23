/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.test.delegate;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.area118.sdocommon.print.excpetions.PrintReportException;
import it.eng.areas.ems.ordinari.entity.ToBookingDO;
import it.eng.areas.ems.ordinari.service.BookingTransactionalService;
import it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.model.BookingDocumentDTO;
import it.eng.areas.to.sdoto.docservice.delegate.print.model.BookingsPrintDataSource;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentDO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;
import it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter;
import it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService;

/**
 * @author Bifulco Luigi
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DocumentServiceDelegateTestCtx.class)
public class DocumentServiceDelegateTest {

	/* THE SUT */
	@Autowired
	private DocumentServiceDelegate delegate;

	/* MOCKED INJECT OBJECTS TO INSTRUCT */
	@Autowired
	private DocumentTransactionalService service;

	@Autowired
	BookingTransactionalService bookingService;

	@Before
	public void trainMockObject() {

	}

	@Test
	public final void testCreateDocument() throws PrintReportException, FileNotFoundException {
		EasyMock.expect(service.createDocument(EasyMock.anyString(), EasyMock.anyObject(byte[].class))).andReturn(DocumentServiceDelegateTestHelper.createBookingDocumentData("TEST", null));
		EasyMock.replay(service);
		BookingDocumentDTO dto = delegate.createDocument("TEST");
		Assert.assertTrue(dto.getParking().equals("TEST"));
		EasyMock.verify(service);
		EasyMock.reset(service);
	}

	@Test
	public final void testCreateDocumentWithdocReference() throws FileNotFoundException, PrintReportException, DataAccessException {
		String bookingCode = "12345678";
		String parking = "TEST";
		ToBookingDO booking = new ToBookingDO();
		booking.setCode(bookingCode);
		booking.setCompactAddress("DEPARTURE ADDRESS");
		booking.setCompactAddress2("ARRIVAL ADDRESS");
		EasyMock.expect(service.createDocument(EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject(byte[].class))).andReturn(DocumentServiceDelegateTestHelper.createBookingDocumentData("TEST", "12345678", null));
		EasyMock.expect(bookingService.getBookingByCode(EasyMock.anyString(), EasyMock.anyString())).andReturn(booking);
		List<BookingDocumentDO> documents = new ArrayList<>();
		EasyMock.expect(service.searchBookingDocuments(EasyMock.anyObject(BookingDocumentFilter.class))).andReturn(documents);
		EasyMock.replay(service);
		EasyMock.replay(bookingService);
		BookingDocumentDTO dto = delegate.createDocument(parking, bookingCode);
		Assert.assertTrue(dto.getParking().equals(parking));
		Assert.assertTrue(dto.getDocReference().equals(bookingCode));
		EasyMock.verify(service);
		EasyMock.reset(service);
	}

	@Test
	public final void testChangeDocumentStatus() {
		delegate.changeDocumentStatus("TEST", BookingDocumentState.OPENED, "user");
	}

}
