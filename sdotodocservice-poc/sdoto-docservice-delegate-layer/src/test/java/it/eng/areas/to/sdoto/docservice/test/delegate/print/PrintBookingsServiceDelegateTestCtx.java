/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.test.delegate.print;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import it.eng.area118.sdocommon.print.PrintServiceConfiguration;
import it.eng.areas.to.sdoto.docservice.delegate.print.PrintBookingsServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.print.impl.PrintBookingServiceDelegateImpl;

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
@Import(PrintServiceConfiguration.class)
public class PrintBookingsServiceDelegateTestCtx {

	@Bean
	public PrintBookingsServiceDelegate printBookingsServiceDelegate() {
		return new PrintBookingServiceDelegateImpl();
	}
}
