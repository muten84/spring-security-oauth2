/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.test.delegate;

import java.util.Calendar;

import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentDO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;

/**
 * @author Bifulco Luigi
 *
 */
public class DocumentServiceDelegateTestHelper {

	public static BookingDocumentDO createBookingDocumentData(String parking, byte[] doc) {
		BookingDocumentDO o = new BookingDocumentDO();
		o.setId("TEST");
		o.setParking(parking);
		o.setDocument(doc);
		o.setState(BookingDocumentState.CREATED);
		o.setCreationDate(Calendar.getInstance().getTime());
		return o;

	}

	public static BookingDocumentDO createBookingDocumentData(String parking, String bookingCode, byte[] doc) {
		BookingDocumentDO o = new BookingDocumentDO();
		o.setId("TEST");
		o.setBookingCode(bookingCode);
		o.setParking(parking);
		o.setDocument(doc);
		o.setState(BookingDocumentState.CREATED);
		o.setCreationDate(Calendar.getInstance().getTime());
		return o;

	}
}
