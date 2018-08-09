package it.eng.areas.ems.sdodaeservices.delegate.model;


public class RispostaQuestionarioFirstResponder {


	private String id;

	private DomandaQuestionario domandaQuestionario;
	
   

	private String risposta;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public DomandaQuestionario getDomandaQuestionario() {
		return domandaQuestionario;
	}

	public void setDomandaQuestionario(DomandaQuestionario domandaQuestionario) {
		this.domandaQuestionario = domandaQuestionario;
	}



	public String getRisposta() {
		return risposta;
	}

	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}

	
}
