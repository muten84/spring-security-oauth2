/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.test.delegate;

import org.easymock.EasyMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import it.eng.area118.sdocommon.print.PrintServiceConfiguration;
import it.eng.areas.ems.common.sdo.dto.configuration.DTOConfiguration;
import it.eng.areas.ems.ordinari.service.BookingTransactionalService;
import it.eng.areas.ems.ordinari.service.WebIdentityService;
import it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.impl.DocumentServiceDelegateImpl;
import it.eng.areas.to.sdoto.docservice.delegate.notify.NotifyServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.print.PrintBookingsServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.print.impl.PrintBookingServiceDelegateImpl;
import it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService;

/**
 * @author Bifulco Luigi
 *
 */
@PropertySources(//
		value = { //
				@PropertySource(ignoreResourceNotFound = false, //
						value = "classpath:/it.eng.areas.to.sdoto.docservice.delegate/cfg-print.properties") //
		})
@Configuration
@Import({ DTOConfiguration.class, PrintServiceConfiguration.class })
public class DocumentServiceDelegateTestCtx {

	@Bean
	public DocumentServiceDelegate getDocumentServiceDelegate() {
		return new DocumentServiceDelegateImpl();
	}

	@Bean
	public PrintBookingsServiceDelegate getPrintBookingsServiceDelegate() {
		return new PrintBookingServiceDelegateImpl();
	}

	@Bean
	public DocumentTransactionalService getDocumentTransactionalService() {
		return EasyMock.createMock(DocumentTransactionalService.class);
	}

	@Bean
	public BookingTransactionalService bookingTransactionalService() {
		return EasyMock.createMock(BookingTransactionalService.class);
	}

	@Bean
	public NotifyServiceDelegate notifyServiceDelegate() {
		return EasyMock.createMock(NotifyServiceDelegate.class);
	}
	
	@Bean
	public WebIdentityService webIdentityService() {
		return EasyMock.createMock(WebIdentityService.class);
	}

}
