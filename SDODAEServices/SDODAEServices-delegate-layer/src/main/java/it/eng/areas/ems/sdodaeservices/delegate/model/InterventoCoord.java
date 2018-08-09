package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.io.Serializable;
import java.util.Date;

public class InterventoCoord implements Serializable {

	private String id;

	private Date dataCreazione;

	private Double latitudine;

	private Double longitudine;

	public InterventoCoord() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
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

}
