package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "DAE_NOTIFICHE_EVENTI")
public class NotificheEventoDO implements Serializable {

	public static enum Type {
		COORD, COMUNE, SUPER
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIRST_RESPONDER")
	private FirstResponderDO firstResponder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVENT")
	private EventDO event;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_STAMP")
	private Date timestamp;

	@Column(name = "TIPO_NOTIFICA")
	@Enumerated(EnumType.STRING)
	private NotificheTypeEnum tipoNotifica;

	@Column(name = "ESITO")
	private String esito;

	@Column(name = "LATITUDINE")
	private Double latitudine;

	@Column(name = "LONGITUDINE")
	private Double longitudine;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "COORD_TIME_STAMP")
	private Date coordTimestamp;

	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_SELEZIONE")
	private Type tipoSelezione;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FirstResponderDO getFirstResponder() {
		return firstResponder;
	}

	public void setFirstResponder(FirstResponderDO firstResponder) {
		this.firstResponder = firstResponder;
	}

	public EventDO getEvent() {
		return event;
	}

	public void setEvent(EventDO event) {
		this.event = event;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getEsito() {
		return esito;
	}

	public void setEsito(String esito) {
		this.esito = esito;
	}

	public NotificheTypeEnum getTipoNotifica() {
		return tipoNotifica;
	}

	public void setTipoNotifica(NotificheTypeEnum tipoNotifica) {
		this.tipoNotifica = tipoNotifica;
	}

	public Double getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(Double latitudine) {
		this.latitudine = latitudine;
	}

	public Double getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(Double longitudine) {
		this.longitudine = longitudine;
	}

	public Date getCoordTimestamp() {
		return coordTimestamp;
	}

	public void setCoordTimestamp(Date coordTimestamp) {
		this.coordTimestamp = coordTimestamp;
	}

	public Type getTipoSelezione() {
		return tipoSelezione;
	}

	public void setTipoSelezione(Type tipoSelezione) {
		this.tipoSelezione = tipoSelezione;
	}

}
