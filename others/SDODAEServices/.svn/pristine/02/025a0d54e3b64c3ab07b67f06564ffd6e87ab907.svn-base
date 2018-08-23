package it.eng.areas.ems.sdodaeservices.delegate.model.filter;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "EventFilter", description = "Modello dati utile alla ricerca degli Eventi")
public class EventFilter extends TerritorialFilter {

	private String id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Calendar dataDA;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Calendar dataA;

	private String comune;

	private String indirizzo;

	private String cartellinoEvento;

	private String nomeFR;

	private String cognomeFR;

	private String frID;

	private String categoria;

	private boolean accepted;

	private boolean managed;

	public EventFilter() {
	}

	public Calendar getDataDA() {
		return dataDA;
	}

	public void setDataDA(Calendar dataDA) {
		this.dataDA = dataDA;
	}

	public Calendar getDataA() {

		return dataA;
	}

	public void setDataA(Calendar dataA) {
		this.dataA = dataA;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getCartellinoEvento() {
		return cartellinoEvento;
	}

	public void setCartellinoEvento(String cartellinoEvento) {
		this.cartellinoEvento = cartellinoEvento;
	}

	public String getNomeFR() {
		return nomeFR;
	}

	public void setNomeFR(String nomeFR) {
		this.nomeFR = nomeFR;
	}

	public String getCognomeFR() {
		return cognomeFR;
	}

	public void setCognomeFR(String cognomeFR) {
		this.cognomeFR = cognomeFR;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrID() {
		return frID;
	}

	public void setFrID(String frID) {
		this.frID = frID;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public boolean isManaged() {
		return managed;
	}

	public void setManaged(boolean managed) {
		this.managed = managed;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

}
