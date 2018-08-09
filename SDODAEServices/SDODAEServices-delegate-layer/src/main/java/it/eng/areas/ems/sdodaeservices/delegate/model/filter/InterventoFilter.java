package it.eng.areas.ems.sdodaeservices.delegate.model.filter;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class InterventoFilter extends EntityFilter implements Serializable {

	/**
	 * 
	 */

	
	private String coRiferimento;

	private String cartellinoEvento;
	protected String id;
	protected String firstResponder;

	private String eventId;

	private Boolean accepted;

	public InterventoFilter() {
		super();
	}

	
	
	
	public String getCoRiferimento() {
		return coRiferimento;
	}




	public void setCoRiferimento(String coRiferimento) {
		this.coRiferimento = coRiferimento;
	}




	public String getCartellinoEvento() {
		return cartellinoEvento;
	}




	public void setCartellinoEvento(String cartellinoEvento) {
		this.cartellinoEvento = cartellinoEvento;
	}




	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public String getFirstResponder() {
		return firstResponder;
	}

	public void setFirstResponder(String firstResponder) {
		this.firstResponder = firstResponder;
	}

}
