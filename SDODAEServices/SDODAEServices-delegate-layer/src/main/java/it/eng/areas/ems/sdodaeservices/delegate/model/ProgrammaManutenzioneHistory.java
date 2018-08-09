package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.Date;

import it.eng.areas.ems.sdodaeservices.entity.enums.Operation;

public class ProgrammaManutenzioneHistory {

	private String id;

	private Operation operation;

	private String utenteLogon;

	private Date operationDate;

	private Integer intervallotraInterventi = 0;

	private Integer durata = 0;

	private Date scadenzaDopo;

	private String stato;

	private String nota;

	private String tipoManutenzione;

	private ManutenzioneEnum mailAlert = ManutenzioneEnum.NON_INVIATA;

	public ProgrammaManutenzioneHistory() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getIntervallotraInterventi() {
		return intervallotraInterventi;
	}

	public void setIntervallotraInterventi(Integer intervallotraInterventi) {
		this.intervallotraInterventi = intervallotraInterventi;
	}

	public Integer getDurata() {
		return durata;
	}

	public void setDurata(Integer durata) {
		this.durata = durata;
	}

	public Date getScadenzaDopo() {
		return scadenzaDopo;
	}

	public void setScadenzaDopo(Date scadenzaDopo) {
		this.scadenzaDopo = scadenzaDopo;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public String getTipoManutenzione() {
		return tipoManutenzione;
	}

	public void setTipoManutenzione(String tipoManutenzione) {
		this.tipoManutenzione = tipoManutenzione;
	}

	public ManutenzioneEnum getMailAlert() {
		return mailAlert;
	}

	public void setMailAlert(ManutenzioneEnum mailAlert) {
		this.mailAlert = mailAlert;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public String getUtenteLogon() {
		return utenteLogon;
	}

	public void setUtenteLogon(String utenteLogon) {
		this.utenteLogon = utenteLogon;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

}
