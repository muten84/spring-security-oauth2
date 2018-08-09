/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.delegate.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import it.eng.areas.ems.common.sdo.mail.configuration.SendMailConfiguration;
import it.eng.areas.to.sdoto.docservice.delegate.notify.NotifyServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.notify.impl.EmailNotificationServiceDelegate;

/**
 * @author Bifulco Luigi
 *
 */
@Configuration
@Import(SendMailConfiguration.class)
public class MailSenderConfiguration {

	@Bean
	public NotifyServiceDelegate notifyService() {
		return new EmailNotificationServiceDelegate();
	}

}
