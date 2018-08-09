package it.eng.areas.ems.sdodaeservices.delegate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "Location", description = "Modello dati che rappresenta una posizione geografica")
@JsonInclude(Include.NON_NULL)
public class Location {

	private String id;
	private Comune comune;
	private Strade indirizzo;

	private Localita localita;

	private String civico;
	private String luogoPubblico;
	private String tipoLuogoPubblico;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Comune getComune() {
		return comune;
	}

	public void setComune(Comune comune) {
		this.comune = comune;
	}

	public Strade getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(Strade indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getLuogoPubblico() {
		return luogoPubblico;
	}

	public void setLuogoPubblico(String luogoPubblico) {
		this.luogoPubblico = luogoPubblico;
	}

	public String getTipoLuogoPubblico() {
		return tipoLuogoPubblico;
	}

	public void setTipoLuogoPubblico(String tipoLuogoPubblico) {
		this.tipoLuogoPubblico = tipoLuogoPubblico;
	}

	public Localita getLocalita() {
		return localita;
	}

	public void setLocalita(Localita localita) {
		this.localita = localita;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", comune=" + comune + ", indirizzo=" + indirizzo + ", civico=" + civico
				+ ", luogoPubblico=" + luogoPubblico + ", tipoLuogoPubblico=" + tipoLuogoPubblico + "]";
	}

}
