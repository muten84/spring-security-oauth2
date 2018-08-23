package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_RISP_QUEST_FIRST_RESPONDER")
public class RispostaQuestionarioFirstResponderDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "QUESTIONARIO_FIRST_RESPONDER")
	private QuestionarioFirstResponderDO questionarioFirstResponder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOMANDA_QUESTIONARIO")
	private DomandaQuestionarioDO domandaQuestionario;

	private String risposta;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRisposta() {
		return risposta;
	}

	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}

	public QuestionarioFirstResponderDO getQuestionarioFirstResponder() {
		return questionarioFirstResponder;
	}

	public void setQuestionarioFirstResponder(QuestionarioFirstResponderDO questionarioFirstResponder) {
		this.questionarioFirstResponder = questionarioFirstResponder;
	}

	public DomandaQuestionarioDO getDomandaQuestionario() {
		return domandaQuestionario;
	}

	public void setDomandaQuestionario(DomandaQuestionarioDO domandaQuestionario) {
		this.domandaQuestionario = domandaQuestionario;
	}

	public RispostaQuestionarioFirstResponderDO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
