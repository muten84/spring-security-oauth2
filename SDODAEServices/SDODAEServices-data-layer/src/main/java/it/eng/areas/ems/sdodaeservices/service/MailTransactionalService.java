package it.eng.areas.ems.sdodaeservices.service;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.entity.MailTemplateDO;
import it.eng.areas.ems.sdodaeservices.entity.MailTraceDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.MailTraceFilterDO;

public interface MailTransactionalService {

	public List<MailTemplateDO> getAllMailTemplate();

	public MailTemplateDO saveMailTemplate(MailTemplateDO template);

	public MailTemplateDO getMailTemplateById(String id);

	public MailTraceDO saveMailTrace(MailTraceDO template);

	public List<MailTraceDO> searchMailTraceByFilter(MailTraceFilterDO filterDO);
}
