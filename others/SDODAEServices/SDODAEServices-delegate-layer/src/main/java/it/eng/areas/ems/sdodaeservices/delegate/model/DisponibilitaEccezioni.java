package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DisponibilitaEccezioni {

	private String id;

	private String idDisponibilita;
	private Calendar data;
	private String motivazione;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdDisponibilita() {
		return idDisponibilita;
	}

	public void setIdDisponibilita(String idDisponibilita) {
		this.idDisponibilita = idDisponibilita;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public String getMotivazione() {
		return motivazione;
	}

	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}

}
