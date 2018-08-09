package it.eng.areas.ems.sdodaeservices.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import it.eng.areas.ems.sdodaeservices.entity.enums.Operation;

@Entity
@Table(name = "DAE_PROG_MAN_HIST")
public class ProgrammaManutenzioneHistoryDO {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROG_MAN_ID")
	private ProgrammaManutenzioneDO programmaManutenzione;

	// Dati dell'history
	@Enumerated(EnumType.STRING)
	@Column(name = "OPERATION")
	private Operation operation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UTENTE_ID")
	private UtenteDO utente;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPERATION_DATE")
	private Date operationDate;

	//
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DAE")
	private DaeDO dae;

	private Integer intervallotraInterventi = 0;

	private Integer durata = 0;

	private Date scadenzaDopo;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "MANUTENTORE")
	private ManutentoreDO responsabile;

	private String stato;

	private String nota;

	private String tipoManutenzione;

	@Enumerated(EnumType.STRING)
	@Column(name = "MAIL_ALERT")
	private ManutenzioneEnum mailAlert = ManutenzioneEnum.NON_INVIATA;

	public ProgrammaManutenzioneHistoryDO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DaeDO getDae() {
		return dae;
	}

	public void setDae(DaeDO dae) {
		this.dae = dae;
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

	public ManutentoreDO getResponsabile() {
		return responsabile;
	}

	public void setResponsabile(ManutentoreDO responsabile) {
		this.responsabile = responsabile;
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

	public ProgrammaManutenzioneDO getProgrammaManutenzione() {
		return programmaManutenzione;
	}

	public void setProgrammaManutenzione(ProgrammaManutenzioneDO programmaManutenzione) {
		this.programmaManutenzione = programmaManutenzione;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public UtenteDO getUtente() {
		return utente;
	}

	public void setUtente(UtenteDO utente) {
		this.utente = utente;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

}
