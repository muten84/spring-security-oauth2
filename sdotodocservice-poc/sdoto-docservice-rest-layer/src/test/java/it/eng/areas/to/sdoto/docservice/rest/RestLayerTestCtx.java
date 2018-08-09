/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest;

import org.mockito.Mockito;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import it.eng.areas.ems.common.sdo.dto.configuration.DTOConfiguration;
import it.eng.areas.ems.ordinari.service.BookingTransactionalService;
import it.eng.areas.ems.ordinari.service.WebIdentityService;
import it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.impl.DocumentServiceDelegateImpl;
import it.eng.areas.to.sdoto.docservice.delegate.notify.NotifyServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.print.PrintBookingsServiceDelegate;
import it.eng.areas.to.sdoto.docservice.rest.configuration.RestLayerConfiguration;
import it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService;

/**
 * @author Bifulco Luigi
 *
 */
@Configuration
@Import({ DTOConfiguration.class, RestLayerConfiguration.class })
public class RestLayerTestCtx {

	// @Bean
	// public DocumentServiceResource getResource() {
	// return new DocumentServiceResource();
	// }

	@Bean
	public DocumentServiceDelegate getDelegate() {
		// return EasyMock.createNiceMock(DocumentServiceDelegate.class);
		return new DocumentServiceDelegateImpl();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public PrintBookingsServiceDelegate getMockedPrintService() {
		// return EasyMock.createMock(MockType.STRICT, PrintBookingsServiceDelegate.class);
		return Mockito.mock(PrintBookingsServiceDelegate.class, Mockito.RETURNS_DEFAULTS);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public DocumentTransactionalService getmockedDbService() throws NoSuchMethodException, SecurityException {
		return Mockito.mock(DocumentTransactionalService.class, Mockito.RETURNS_DEFAULTS);
		// return EasyMock.createMock(MockType.STRICT, DocumentTransactionalService.class);

	}

	@Bean
	public WebIdentityService webIdentityService() {
		return Mockito.mock(WebIdentityService.class, Mockito.RETURNS_DEFAULTS);
	}
	

	@Bean
	public NotifyServiceDelegate notifyServiceDelegate() {
		return Mockito.mock(NotifyServiceDelegate.class, Mockito.RETURNS_DEFAULTS);
	}

	@Bean
	public BookingTransactionalService mockBookingsTransactionalService() {
		return Mockito.mock(BookingTransactionalService.class);
	}

}
