/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.delegate.print.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import it.eng.area118.sdocommon.print.IPrint;
import it.eng.area118.sdocommon.print.ReportData;
import it.eng.area118.sdocommon.print.excpetions.PrintReportException;
import it.eng.areas.ems.ordinari.service.BookingTransactionalService;
import it.eng.areas.to.sdoto.docservice.delegate.print.PrintBookingsServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.print.model.BookingsPrintDataSource;

/**
 * @author Bifulco Luigi
 *
 */
@Component
public class PrintBookingServiceDelegateImpl implements PrintBookingsServiceDelegate {

	@Autowired
	private IPrint printService;	

	@Value("${print.reportPath}")
	private String reportPath;

	@Override
	public byte[] createBookingDocument(BookingsPrintDataSource ds) throws PrintReportException, FileNotFoundException {
		ReportData<BookingsPrintDataSource> rd = new ReportData<>();
		List<BookingsPrintDataSource> list = new ArrayList<BookingsPrintDataSource>();
		list.add(ds);
		rd.setDatasource(list);
		// rd.setReportSourcePath("src/test/resources/it.eng.area118.sdocommon.print/report.jrxml");
		ClassLoader cl = this.getClass().getClassLoader();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		try {
			Resource[] resources = resolver.getResources(reportPath);
			if (resources == null || resources.length == 0) {
				throw new FileNotFoundException("no file match found for path: " + reportPath);
			}
			rd.setReportSourcePath(resources[0].getFile().getAbsolutePath());
		} catch (IOException e) {
			throw new FileNotFoundException("file not found: " + reportPath);
		}
		ByteArrayOutputStream stream = (ByteArrayOutputStream) printService.printToStream(rd, null);
		return stream.toByteArray();
	}
}
