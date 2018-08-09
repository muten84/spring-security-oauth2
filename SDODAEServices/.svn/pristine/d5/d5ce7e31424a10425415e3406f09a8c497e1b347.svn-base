package it.eng.areas.ems.sdodaeservices.delegate.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.delegate.MailDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.config.TemplateParserContext;
import it.eng.areas.ems.sdodaeservices.delegate.model.EntityType;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplate;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplateEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTrace;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.MailTraceFilter;
import it.eng.areas.ems.sdodaeservices.entity.MailTemplateDO;
import it.eng.areas.ems.sdodaeservices.entity.MailTraceDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.MailTraceFilterDO;
import it.eng.areas.ems.sdodaeservices.service.MailTransactionalService;
import it.esel.parsley.lang.StringUtils;

@Component
public class MailDelegateServiceImpl implements MailDelegateService {

	protected static Logger logger = LoggerFactory.getLogger(MailDelegateServiceImpl.class);

	// @Autowired
	// protected MailService mailService;

	@Autowired
	protected MailTransactionalService mailTransactional;

	@Autowired
	protected DTOService dtoService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SimpleMailMessage templateMessage;

	ExecutorService executor = Executors.newFixedThreadPool(5);

	@Override
	public String prepareMailText(Map<String, String> valuesMap, MailTemplateDO mailTemplateDO) {
		String resolvedString = "";
		if (mailTemplateDO != null) {
			String mailTemplate = mailTemplateDO.getData();
			if (valuesMap != null) {
				StrSubstitutor sub = new StrSubstitutor(valuesMap);
				resolvedString = sub.replace(mailTemplate);
			} else {
				resolvedString = mailTemplate;
			}
		}
		return resolvedString;
	}

	@Override
	public String prepareMailText(Object obj, MailTemplateDO mailTemplateDO) {

		if (mailTemplateDO != null) {
			String mailTemplate = mailTemplateDO.getData();
			if (obj != null) {
				return prepareMailText(obj, mailTemplate);
			} else {
				return mailTemplate;
			}
		}
		return "";
	}

	@Override
	public String prepareMailText(Object obj, String template) {
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(template, new TemplateParserContext());
		return (String) exp.getValue(obj);
	}

	@Override
	public List<MailTemplate> getAllMailTemplate() {
		return (List<MailTemplate>) dtoService.convertCollection(mailTransactional.getAllMailTemplate(),
				MailTemplate.class);
	}

	@Override
	public MailTemplate saveMailTemplate(MailTemplate template) {
		MailTemplateDO templateDO = dtoService.convertObject(template, MailTemplateDO.class);
		templateDO = mailTransactional.saveMailTemplate(templateDO);
		return dtoService.convertObject(templateDO, MailTemplate.class);
	}

	@Override
	public MailTemplate getMailTemplateById(String id) {
		MailTemplateDO templateDO = mailTransactional.getMailTemplateById(id);
		return dtoService.convertObject(templateDO, MailTemplate.class);
	}

	@Override
	public void sendMail(String destinatario, String mail, String object) {
		executor.submit(() -> {
			boolean result = false;
			try {
				result = sendMailHTML(destinatario, mail, object);
				if (result) {
					// creo un trace per l'invio della mail
					MailTraceDO trace = new MailTraceDO();
					trace.setDestinatario(destinatario);
					trace.setDataInvio(new Date());

					mailTransactional.saveMailTrace(trace);
				}
			} catch (Exception e) {
				logger.error("", e);
			}
			return result;
		});
	}

	@Override
	public void sendMail(String destinatario, Map<String, String> valuesMap, MailTemplateEnum param) {
		executor.submit(() -> {
			boolean result = false;
			try {
				MailTemplateDO mail = mailTransactional.getMailTemplateById(param.name());
				result = sendMailHTML(destinatario, prepareMailText(valuesMap, mail), mail.getOggetto());
				if (result) {
					// creo un trace per l'invio della mail
					MailTraceDO trace = new MailTraceDO();
					trace.setDestinatario(destinatario);
					trace.setDataInvio(new Date());
					trace.setMailTemplate(mailTransactional.getMailTemplateById(param.name()));

					mailTransactional.saveMailTrace(trace);
				}
			} catch (Exception e) {
				logger.error("", e);
			}
			return result;
		});

	}

	@Override
	public void sendMail(String destinatario, Object obj, MailTemplateEnum param, EntityType type, String id) {
		executor.submit(() -> {
			boolean result = false;
			try {
				MailTemplateDO mail = mailTransactional.getMailTemplateById(param.name());
				result = sendMailHTML(destinatario, prepareMailText(obj, mail), mail.getOggetto());
				if (result) {
					// creo un trace per l'invio della mail
					MailTraceDO trace = new MailTraceDO();
					trace.setDestinatario(destinatario);
					trace.setDataInvio(new Date());
					trace.setMailTemplate(mailTransactional.getMailTemplateById(param.name()));
					trace.setEntityType(type.name());
					trace.setEntityId(id);

					mailTransactional.saveMailTrace(trace);
				}

			} catch (Exception e) {
				logger.error("", e);
			}
			return result;
		});
	}

	@Override
	public List<MailTrace> searchMailTraceByFilter(MailTraceFilter filter) {
		MailTraceFilterDO filterDO = dtoService.convertObject(filter, MailTraceFilterDO.class);
		List<MailTraceDO> traces = mailTransactional.searchMailTraceByFilter(filterDO);
		return (List<MailTrace>) dtoService.convertCollection(traces, MailTrace.class);
	}

	public boolean sendMailHTML(String dest, String html, String subject) throws MessagingException {

		MimeMessageHelper heler = new MimeMessageHelper(mailSender.createMimeMessage());
		heler.setTo(dest);
		heler.setFrom(templateMessage.getFrom());
		heler.setSubject(!StringUtils.isEmpty(subject) ? subject : templateMessage.getSubject());
		heler.setText(html, true);

		this.mailSender.send(heler.getMimeMessage());

		return true;
	}

	@Override
	public String prepareMailText(Map<String, String> valuesMap, MailTemplateEnum param) {
		MailTemplateDO mail = mailTransactional.getMailTemplateById(param.name());
		return prepareMailText(valuesMap, mail);
	}

	@Override
	public String prepareMailText(Object obj, MailTemplateEnum param) {
		MailTemplateDO mail = mailTransactional.getMailTemplateById(param.name());
		return prepareMailText(obj, mail);
	}
}
