package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.io.Serializable;
import java.util.Date;

public class CategoriaFr implements Serializable {

	private String id;
	private String descrizione;
	private Integer chilometriMinuto;
	private boolean accettatiPerIntervento;
	private Integer accettaMinusKm;
	private String colore;
	private Utente creataDa;
	private Utente aggiornataDa;
	private Date aggiornataIl;

	private int priority;

	public CategoriaFr() {
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

	public Utente getCreataDa() {
		return creataDa;
	}

	public void setCreataDa(Utente creataDa) {
		this.creataDa = creataDa;
	}

	public Utente getAggiornataDa() {
		return aggiornataDa;
	}

	public void setAggiornataDa(Utente aggiornataDa) {
		this.aggiornataDa = aggiornataDa;
	}

	public Date getAggiornataIl() {
		return aggiornataIl;
	}

	public void setAggiornataIl(Date aggiornataIl) {
		this.aggiornataIl = aggiornataIl;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
