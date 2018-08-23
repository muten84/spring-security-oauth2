package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_PROGRAMMA_MANUTENZIONE")
public class ProgrammaManutenzioneDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

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

	@OneToMany(orphanRemoval = true, cascade = { CascadeType.ALL }, mappedBy = "programmaManutenzione")
	Set<ProgrammaManutenzioneHistoryDO> history;

	public ProgrammaManutenzioneDO() {
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

	public Set<ProgrammaManutenzioneHistoryDO> getHistory() {
		return history;
	}

	public void setHistory(Set<ProgrammaManutenzioneHistoryDO> history) {
		this.history = history;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((durata == null) ? 0 : durata.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((intervallotraInterventi == null) ? 0 : intervallotraInterventi.hashCode());
		result = prime * result + ((mailAlert == null) ? 0 : mailAlert.hashCode());
		result = prime * result + ((nota == null) ? 0 : nota.hashCode());
		result = prime * result + ((scadenzaDopo == null) ? 0 : scadenzaDopo.hashCode());
		result = prime * result + ((stato == null) ? 0 : stato.hashCode());
		result = prime * result + ((tipoManutenzione == null) ? 0 : tipoManutenzione.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProgrammaManutenzioneDO other = (ProgrammaManutenzioneDO) obj;
		if (durata == null) {
			if (other.durata != null)
				return false;
		} else if (!durata.equals(other.durata))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (intervallotraInterventi == null) {
			if (other.intervallotraInterventi != null)
				return false;
		} else if (!intervallotraInterventi.equals(other.intervallotraInterventi))
			return false;
		if (mailAlert != other.mailAlert)
			return false;
		if (nota == null) {
			if (other.nota != null)
				return false;
		} else if (!nota.equals(other.nota))
			return false;
		if (scadenzaDopo == null) {
			if (other.scadenzaDopo != null)
				return false;
		} else if (!scadenzaDopo.equals(other.scadenzaDopo))
			return false;
		if (stato == null) {
			if (other.stato != null)
				return false;
		} else if (!stato.equals(other.stato))
			return false;
		if (tipoManutenzione == null) {
			if (other.tipoManutenzione != null)
				return false;
		} else if (!tipoManutenzione.equals(other.tipoManutenzione))
			return false;
		return true;
	}

}
