package it.eng.areas.ems.sdodaeservices.delegate.model.filter;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "RuoloFilter", description = "Modello dati utile alla ricerca dei Ruoli")
public class RuoloFilter {

	private String id;

	private String nome;

	private String descrizione;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	
	
	
	public RuoloFilter() {
		// TODO Auto-generated constructor stub
	}


	
	
	
	
}
