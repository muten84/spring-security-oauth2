/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.delegate.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import it.eng.area118.sdocommon.print.PrintServiceConfiguration;
import it.eng.areas.ems.common.sdo.dto.configuration.DTOConfiguration;
import it.eng.areas.to.sdoto.docservice.DataLayerDocServiceConfiguration;
import it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.impl.DocumentServiceDelegateImpl;
import it.eng.areas.to.sdoto.docservice.delegate.notify.NotifyServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.notify.impl.EmailNotificationServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.print.PrintBookingsServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.print.impl.PrintBookingServiceDelegateImpl;

/**
 * @author Bifulco Luigi
 *
 */
@Configuration
@Import({ DTOConfiguration.class, DataLayerDocServiceConfiguration.class, PrintServiceConfiguration.class })
public class DocServiceDelegateConfiguration {



	@Bean
	public DocumentServiceDelegate getDocumentServiceDelegate() {
		return new DocumentServiceDelegateImpl();
	}

	@Bean
	public PrintBookingsServiceDelegate getPrintBookingsServiceDelegate() {
		return new PrintBookingServiceDelegateImpl();
	}

	// @Bean
	// public NotifyServiceDelegate notifyServiceDelegate() {
	// return new EmailNotificationServiceDelegate();
	// }



}
