package it.eng.areas.ems.sdodaeservices.delegate.model;

import com.vividsolutions.jts.geom.Point;

public class VctDAE {

	private String id;

	private String descrizione;

	private String matricola;

	protected Point loc;

	private boolean operativo;

	private boolean privato;

	private String noteresponsabile;

	private String stato;

	private String nomeSede;

	private String indirizzo;

	private String ubicazione;

	private String orari;

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

	public Point getLoc() {
		return loc;
	}

	public void setLoc(Point loc) {
		this.loc = loc;
	}

	public boolean isOperativo() {
		return operativo;
	}

	public void setOperativo(boolean operativo) {
		this.operativo = operativo;
	}

	public boolean isPrivato() {
		return privato;
	}

	public void setPrivato(boolean privato) {
		this.privato = privato;
	}

	public String getNoteresponsabile() {
		return noteresponsabile;
	}

	public void setNoteresponsabile(String noteresponsabile) {
		this.noteresponsabile = noteresponsabile;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getNomeSede() {
		return nomeSede;
	}

	public void setNomeSede(String nomeSede) {
		this.nomeSede = nomeSede;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getUbicazione() {
		return ubicazione;
	}

	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}

	public String getOrari() {
		return orari;
	}

	public void setOrari(String orari) {
		this.orari = orari;
	}

	public String getMatricola() {
		return matricola;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}

}
