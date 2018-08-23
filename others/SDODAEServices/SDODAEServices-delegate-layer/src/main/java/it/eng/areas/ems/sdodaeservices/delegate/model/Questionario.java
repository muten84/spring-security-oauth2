package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.Set;

public class Questionario {

	private String id;
	private Set<DomandaQuestionario> domandaQuestionario;
	private String descrizione;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Set<DomandaQuestionario> getDomandaQuestionario() {
		return domandaQuestionario;
	}
	public void setDomandaQuestionario(Set<DomandaQuestionario> domandaQuestionario) {
		this.domandaQuestionario = domandaQuestionario;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	
}
