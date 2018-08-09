package it.eng.areas.ems.sdodaeservices.delegate;

import java.util.List;
import java.util.Map;

import it.eng.areas.ems.sdodaeservices.delegate.model.EntityType;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplate;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTemplateEnum;
import it.eng.areas.ems.sdodaeservices.delegate.model.MailTrace;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.MailTraceFilter;
import it.eng.areas.ems.sdodaeservices.entity.MailTemplateDO;

public interface MailDelegateService {

	public List<MailTemplate> getAllMailTemplate();

	public MailTemplate saveMailTemplate(MailTemplate template);

	public MailTemplate getMailTemplateById(String id);

	public void sendMail(String destinatario, String mail, String object);

	public void sendMail(String destinatario, Map<String, String> valuesMap, MailTemplateEnum param);

	public String prepareMailText(Map<String, String> valuesMap, MailTemplateDO mailTemplateDO);

	public String prepareMailText(Map<String, String> valuesMap, MailTemplateEnum param);

	public List<MailTrace> searchMailTraceByFilter(MailTraceFilter filter);

	/**
	 * Invia una mail inserendo i dati dell'oggetto nel template passato. Le
	 * proprietà nel template devono essere del tipo ${oggetto.valore}
	 * 
	 * @param destinatario
	 *            Email del destinatario
	 * @param obj
	 *            oggetto da cui prendere i dati
	 * @param param
	 *            id del temmplate da caricare da DB
	 * @param type
	 *            Tipo di entità
	 * @param id
	 *            Id dell'entità passata
	 */
	public void sendMail(String destinatario, Object obj, MailTemplateEnum param, EntityType type, String id);

	/**
	 * Prende in input un oggetto e l'id del template in cui sono inserite le
	 * proprietà tipo: ${valore}, ${valore.sottovalore}, ${espressione complessa}
	 * 
	 * @param obj
	 * @param param
	 * @return
	 */
	public String prepareMailText(Object obj, MailTemplateDO mailTemplateDO);

	public String prepareMailText(Object obj, MailTemplateEnum param);

	public String prepareMailText(Object obj, String template);
}
