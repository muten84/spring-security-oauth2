package it.eng.areas.ems.sdodaeservices.delegate.model;

public class QuestionarioFirstResponder {

	private String id;

	private boolean deleted;

	
	private RispostaQuestionarioFirstResponder rispostaQuestionarioFirstResponder;


	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}



	public RispostaQuestionarioFirstResponder getRispostaQuestionarioFirstResponder() {
		return rispostaQuestionarioFirstResponder;
	}

	public void setRispostaQuestionarioFirstResponder(
			RispostaQuestionarioFirstResponder rispostaQuestionarioFirstResponder) {
		this.rispostaQuestionarioFirstResponder = rispostaQuestionarioFirstResponder;
	}

	
	
}
