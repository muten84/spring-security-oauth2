package it.eng.areas.ems.sdodaeservices.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.areas.ems.sdodaeservices.dao.MailTemplateDAO;
import it.eng.areas.ems.sdodaeservices.dao.MailTraceDAO;
import it.eng.areas.ems.sdodaeservices.entity.MailTemplateDO;
import it.eng.areas.ems.sdodaeservices.entity.MailTraceDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.MailTraceFilterDO;
import it.eng.areas.ems.sdodaeservices.service.MailTransactionalService;

@Component
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class MailTransactionalServiceImpl implements MailTransactionalService {

	@Autowired
	private MailTemplateDAO mailTemplateDAO;

	@Autowired
	private MailTraceDAO mailTraceDAO;

	@Override
	public List<MailTemplateDO> getAllMailTemplate() {
		return mailTemplateDAO.getAll();
	}

	@Override
	public MailTemplateDO saveMailTemplate(MailTemplateDO template) {
		return mailTemplateDAO.save(template);
	}

	@Override
	public MailTemplateDO getMailTemplateById(String id) {
		return mailTemplateDAO.get(id);
	}

	@Override
	public MailTraceDO saveMailTrace(MailTraceDO template) {
		return mailTraceDAO.save(template);
	}

	@Override
	public List<MailTraceDO> searchMailTraceByFilter(MailTraceFilterDO filterDO) {
		return mailTraceDAO.searchMailTraceByFilter(filterDO);
	}
}
