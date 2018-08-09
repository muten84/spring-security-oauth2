package it.eng.areas.ems.sdodaeservices.entity;

import java.util.Date;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_DAE_FAULT")
public class DaeFaultDO {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DAE_ID")
	private DaeDO dae;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UTENTE_ID")
	private UtenteDO utente;

	@Column(name = "TIPOLOGIA")
	private String tipologia;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "DATA_SEGNALAZIONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataSegnalazione;

	@Column(name = "STATO_ATTUALE")
	@Enumerated(EnumType.STRING)
	private DaeFaultStatoEnum statoAttuale;

	@OneToMany(mappedBy = "fault", cascade = { CascadeType.ALL })
	private Set<DaeFaultTraceDO> trace;

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

	public Set<DaeFaultTraceDO> getTrace() {
		return trace;
	}

	public void setTrace(Set<DaeFaultTraceDO> trace) {
		this.trace = trace;
	}

}
