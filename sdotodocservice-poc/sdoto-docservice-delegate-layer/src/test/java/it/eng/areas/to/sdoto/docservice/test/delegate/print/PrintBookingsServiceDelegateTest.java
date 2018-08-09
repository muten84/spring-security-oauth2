/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.test.delegate.print;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.area118.sdocommon.print.excpetions.PrintReportException;
import it.eng.areas.to.sdoto.docservice.delegate.print.PrintBookingsServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.print.model.BookingsPrintDataSource;

/**
 * @author Bifulco Luigi
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PrintBookingsServiceDelegateTestCtx.class)
public class PrintBookingsServiceDelegateTest {

	@Autowired
	private PrintBookingsServiceDelegate delegate;

	@Test
	public final void testCreateBookingDocument() throws PrintReportException, IOException {
		BookingsPrintDataSource ds = new BookingsPrintDataSource();
		ds.setFromPlace("FROM PLACE");
		ds.setToPlace("TO PLACE");
		byte[] bytes = delegate.createBookingDocument(ds);
		Assert.assertNotNull(bytes);
		Assert.assertTrue(bytes.length > 0);
		/* README: UNCOMMMENT THESE LINES IF YOU WANT TO MANUALLY CHECK THE PDF FILE */
		// File file = new File("src/test/resources/it.eng.areas.to.sdoto.docservice.delegate/test.pdf");
		// FileUtils.writeByteArrayToFile(file, bytes);

	}

}
