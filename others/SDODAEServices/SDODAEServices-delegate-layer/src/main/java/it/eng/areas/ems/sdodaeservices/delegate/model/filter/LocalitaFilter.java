package it.eng.areas.ems.sdodaeservices.delegate.model.filter;

import io.swagger.annotations.ApiModel;
import it.eng.area118.sdocommon.dao.filter.EntityFilter;

@ApiModel(value = "LocalitaFilter", description = "Modello dati utile alla ricerca delle Localita")
public class LocalitaFilter extends EntityFilter {

	private String id;
	private String name;
	private String codiceIstatComune;
	private String nomeComune;

	public String getCodiceIstatComune() {
		return codiceIstatComune;
	}

	public void setCodiceIstatComune(String codiceIstatComune) {
		this.codiceIstatComune = codiceIstatComune;
	}

	public LocalitaFilter() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNomeComune() {
		return nomeComune;
	}

	public void setNomeComune(String nomeComune) {
		this.nomeComune = nomeComune;
	}

}
