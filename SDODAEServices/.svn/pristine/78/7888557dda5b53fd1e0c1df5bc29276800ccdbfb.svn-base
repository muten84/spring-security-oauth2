package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import it.eng.areas.ems.sdodaeservices.entity.NotificheTypeEnum;

@ApiModel(value = "Event", description = "Modello dati che rappresenta una emergenza di tipo cardiaco da poter asservire")
public class NotificheEvento implements Serializable {

	public static enum Type {
		COORD, COMUNE, SUPER
	}

	private String id;

	private FirstResponder firstResponder;

	private Event event;

	private Date timestamp;

	private String esito;

	private NotificheTypeEnum tipoNotifica;

	private Double latitudine;

	private Double longitudine;

	private Date coordTimestamp;

	private Type tipoSelezione;

	public NotificheEvento() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FirstResponder getFirstResponder() {
		return firstResponder;
	}

	public void setFirstResponder(FirstResponder firstResponder) {
		this.firstResponder = firstResponder;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
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
