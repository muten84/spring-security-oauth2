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
@Table(name = "DAE_QUEST_FIRST_RESPONDER")
public class QuestionarioFirstResponderDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIRST_RESPONDER")
	private FirstResponderDO firstResponder;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RISP_QUEST_FIRST_RESPONDER")
	private RispostaQuestionarioFirstResponderDO rispostaQuestionarioFirstResponder;

	private boolean deleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FirstResponderDO getFirstResponder() {
		return firstResponder;
	}

	public void setFirstResponder(FirstResponderDO firstResponder) {
		this.firstResponder = firstResponder;
	}

	public RispostaQuestionarioFirstResponderDO getRispostaQuestionarioFirstResponder() {
		return rispostaQuestionarioFirstResponder;
	}

	public void setRispostaQuestionarioFirstResponder(
			RispostaQuestionarioFirstResponderDO rispostaQuestionarioFirstResponder) {
		this.rispostaQuestionarioFirstResponder = rispostaQuestionarioFirstResponder;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public QuestionarioFirstResponderDO() {
		super();
	}

}
