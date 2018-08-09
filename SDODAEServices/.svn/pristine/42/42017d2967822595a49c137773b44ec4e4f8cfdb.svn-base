package it.eng.areas.ems.sdodaeservices.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_MAIL_TRACE")
public class MailTraceDO {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	private String destinatario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MAIL_TEMPLATE_ID")
	private MailTemplateDO mailTemplate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_INVIO")
	private Date dataInvio;

	@Column(name = "ENTITY_TYPE")
	private String entityType;

	@Column(name = "ENTITY_ID")
	private String entityId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public MailTemplateDO getMailTemplate() {
		return mailTemplate;
	}

	public void setMailTemplate(MailTemplateDO mailTemplate) {
		this.mailTemplate = mailTemplate;
	}

	public Date getDataInvio() {
		return dataInvio;
	}

	public void setDataInvio(Date dataInvio) {
		this.dataInvio = dataInvio;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

}
