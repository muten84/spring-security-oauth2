package it.eng.areas.ems.sdodaeservices.entity;

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
@Table(name = "DAE_DAE_FAULT_TRACE")
public class DaeFaultTraceDO {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne
	@JoinColumn(name = "FAULT_ID")
	private DaeFaultDO fault;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UTENTE_ID")
	private UtenteDO utente;

	@Column(name = "TIPOLOGIA")
	private String tipologia;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "DATA_MODIFICA")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataModifica;

	@Column(name = "STATO")
	@Enumerated(EnumType.STRING)
	private DaeFaultStatoEnum stato;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DaeFaultDO getFault() {
		return fault;
	}

	public void setFault(DaeFaultDO fault) {
		this.fault = fault;
	}

	public UtenteDO getUtente() {
		return utente;
	}

	public void setUtente(UtenteDO utente) {
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
