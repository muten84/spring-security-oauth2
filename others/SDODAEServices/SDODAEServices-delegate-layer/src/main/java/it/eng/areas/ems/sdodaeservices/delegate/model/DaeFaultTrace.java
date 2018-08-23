package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.Date;

import it.eng.areas.ems.sdodaeservices.entity.DaeFaultStatoEnum;

public class DaeFaultTrace {

	private String id;

	private DaeFault fault;

	private Utente utente;

	private String tipologia;

	private String note;

	private Date dataModifica;

	private DaeFaultStatoEnum stato;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DaeFault getFault() {
		return fault;
	}

	public void setFault(DaeFault fault) {
		this.fault = fault;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(Date dataModifica) {
		this.dataModifica = dataModifica;
	}

	public DaeFaultStatoEnum getStato() {
		return stato;
	}

	public void setStato(DaeFaultStatoEnum stato) {
		this.stato = stato;
	}

}
