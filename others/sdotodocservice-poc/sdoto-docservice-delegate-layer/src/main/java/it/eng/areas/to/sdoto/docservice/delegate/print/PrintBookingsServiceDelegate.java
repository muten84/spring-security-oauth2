/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.delegate.print;

import java.io.FileNotFoundException;

import it.eng.area118.sdocommon.print.excpetions.PrintReportException;
import it.eng.areas.to.sdoto.docservice.delegate.print.model.BookingsPrintDataSource;

/**
 * @author Bifulco Luigi
 *
 */
public interface PrintBookingsServiceDelegate {

	/**
	 * @param ds
	 * @return
	 * @throws PrintReportException
	 * @throws FileNotFoundException 
	 */
	byte[] createBookingDocument(BookingsPrintDataSource ds) throws PrintReportException, FileNotFoundException;

}
