package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.Date;
import java.util.Set;

import it.eng.areas.ems.sdodaeservices.entity.DaeFaultStatoEnum;

public class DaeFault {

	private String id;

	private Dae dae;

	private Utente utente;

	private String tipologia;

	private String note;

	private Date dataSegnalazione;

	private DaeFaultStatoEnum statoAttuale;

	private Set<DaeFaultTrace> trace;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Dae getDae() {
		return dae;
	}

	public void setDae(Dae dae) {
		this.dae = dae;
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

	public Date getDataSegnalazione() {
		return dataSegnalazione;
	}

	public void setDataSegnalazione(Date dataSegnalazione) {
		this.dataSegnalazione = dataSegnalazione;
	}

	public DaeFaultStatoEnum getStatoAttuale() {
		return statoAttuale;
	}

	public void setStatoAttuale(DaeFaultStatoEnum statoAttuale) {
		this.statoAttuale = statoAttuale;
	}

	public Set<DaeFaultTrace> getTrace() {
		return trace;
	}

	public void setTrace(Set<DaeFaultTrace> trace) {
		this.trace = trace;
	}

}
