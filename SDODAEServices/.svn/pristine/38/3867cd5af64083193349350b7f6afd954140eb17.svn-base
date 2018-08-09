package it.eng.areas.ems.sdodaeservices.entity.gis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vividsolutions.jts.geom.Point;

@Entity
@Table(name = "vct_dae")
public class VCTDaeDO {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "matricola")
	private String matricola;

	@Column(name = "shape", columnDefinition = "Geometry(Point,4326)")
	private Point location;

	@Column(name = "operativo")
	private boolean operativo;

	@Column(name = "privato")
	private boolean privato;

	@Column(name = "noteresponsabile")
	private String noteresponsabile;

	@Column(name = "stato")
	private String stato;

	@Column(name = "nome_sede")
	private String nomeSede;

	@Column(name = "indirizzo")
	private String indirizzo;

	@Column(name = "ubicazione")
	private String ubicazione;

	@Column(name = "orari")
	private String orari;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMatricola() {
		return matricola;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
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

}
