/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentDO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;
import it.eng.areas.to.sdoto.docservice.entity.DepartmentAdresseeDO;

/**
 * @author Bifulco Luigi
 *
 */
public class DocumentServiceJerseyTestHelper {

	public static BookingDocumentDO createBookingDocumentData(String id, String parking, String bookingCode, BookingDocumentState state, byte[] doc) {
		BookingDocumentDO o = new BookingDocumentDO();
		o.setId(id);
		o.setBookingCode(bookingCode);
		o.setParking(parking);
		o.setDocument(doc);
		o.setState(state);
		o.setCreationDate(Calendar.getInstance().getTime());
		return o;

	}

	public static List<DepartmentAdresseeDO> stubDepartmentAdresses(String parking, String mail) {
		List<DepartmentAdresseeDO> receivers = new ArrayList<DepartmentAdresseeDO>();
		DepartmentAdresseeDO depAdd = createDepAddresse(parking, mail, null);
		receivers.add(depAdd);
		return receivers;
	}

	/**
	 * @return
	 */
	private static DepartmentAdresseeDO createDepAddresse(String depId, String mail, String depName) {
		DepartmentAdresseeDO o = new DepartmentAdresseeDO();
		o.setAddress(mail);
		o.setDepId(depId);
		o.setDescription(depName);
		return o;
	}
}
