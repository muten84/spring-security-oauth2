package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "DAE_CATEGORIA_FR")
public class CategoriaFrDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	private String descrizione;

	@Column(name = "CHILOMETRI_MINUTO")
	private Integer chilometriMinuto;

	@Column(name = "ACCETTATI_PER_INTERVENTO")
	private boolean accettatiPerIntervento;

	@Column(name = "ACCETTA_MINUS_KM")
	private Integer accettaMinusKm;

	@Column(name = "COLORE")
	private String colore;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATA_DA")
	private UtenteDO creataDa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGGIORNATA_DA")
	private UtenteDO aggiornataDa;

	@Column(name = "AGGIORNATA_IL")
	@Temporal(TemporalType.DATE)
	private Date aggiornataIl;

	@Column(name = "VISIBLE_ON_APP")
	private Boolean visibleOnApp;

	@Column(name = "PRIORITY")
	private int priority;

	public CategoriaFrDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Integer getChilometriMinuto() {
		return chilometriMinuto;
	}

	public void setChilometriMinuto(Integer chilometriMinuto) {
		this.chilometriMinuto = chilometriMinuto;
	}

	public boolean isAccettatiPerIntervento() {
		return accettatiPerIntervento;
	}

	public void setAccettatiPerIntervento(boolean accettatiPerIntervento) {
		this.accettatiPerIntervento = accettatiPerIntervento;
	}

	public Integer getAccettaMinusKm() {
		return accettaMinusKm;
	}

	public void setAccettaMinusKm(Integer accettaMinusKm) {
		this.accettaMinusKm = accettaMinusKm;
	}

	public String getColore() {
		return colore;
	}

	public void setColore(String colore) {
		this.colore = colore;
	}

	public UtenteDO getCreataDa() {
		return creataDa;
	}

	public void setCreataDa(UtenteDO creataDa) {
		this.creataDa = creataDa;
	}

	public UtenteDO getAggiornataDa() {
		return aggiornataDa;
	}

	public void setAggiornataDa(UtenteDO aggiornataDa) {
		this.aggiornataDa = aggiornataDa;
	}

	public Date getAggiornataIl() {
		return aggiornataIl;
	}

	public void setAggiornataIl(Date aggiornataIl) {
		this.aggiornataIl = aggiornataIl;
	}

	public Boolean getVisibleOnApp() {
		return visibleOnApp;
	}

	public void setVisibleOnApp(Boolean visibleOnApp) {
		this.visibleOnApp = visibleOnApp;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
