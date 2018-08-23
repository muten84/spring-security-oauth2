/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.delegate.notify.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.common.sdo.mail.MailService;
import it.eng.areas.to.sdoto.docservice.delegate.notify.NotifyServiceDelegate;
import it.esel.parsley.lang.StringUtils;

/**
 * @author Bifulco Luigi
 *
 */
@Component
public class EmailNotificationServiceDelegate implements NotifyServiceDelegate {

	@Autowired
	private MailService mailService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.delegate.notify.NotifyServiceDelegate#sendData(java.lang.String, byte[])
	 */
	@Override
	public boolean sendData(String dest, String text, byte[] data) {
		return mailService.sendMail(dest, text, "prenotazione.pdf", data);
	}

	@Override
	public boolean sendData(String[] dest, String text, byte[] data) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public boolean sendData(String[] dest, byte[] data) {
		return sendData(dest, "", data);
	}

	@Override
	public boolean sendData(String dest, byte[] data) {
		return sendData(dest, "", data);
	}

	@Override
	public boolean sendSimpleData(String dest, String text) {
		return mailService.sendMail(dest, text);
	}

	@Override
	public boolean sendSimpleData(String[] dest, String text) {
		return mailService.sendMail(dest, text);
	}

}
