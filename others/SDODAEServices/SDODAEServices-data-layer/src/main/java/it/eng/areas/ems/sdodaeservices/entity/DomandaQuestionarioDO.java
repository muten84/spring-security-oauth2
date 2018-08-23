package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



@Entity
@Table(name = "DAE_DOMANDA_QUESTIONARIO")
public class DomandaQuestionarioDO implements Serializable {

	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
   @Column(name= "ID")
   private String id;
	
	@OneToMany(mappedBy = "domandaQuestionario", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Set<RispostaQuestionarioFirstResponderDO> risposteQuestionarioFirstResponder;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "QUESTIONARIO")
	private  QuestionarioDO questionario;
	
	private String domanda;
	
	
	@Column(name = "TIPO_RISPOSTA")
	private String TipoRisposta;
	
	public DomandaQuestionarioDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<RispostaQuestionarioFirstResponderDO> getRisposteQuestionarioFirstResponder() {
		return risposteQuestionarioFirstResponder;
	}

	public void setRisposteQuestionarioFirstResponder(
			Set<RispostaQuestionarioFirstResponderDO> risposteQuestionarioFirstResponder) {
		this.risposteQuestionarioFirstResponder = risposteQuestionarioFirstResponder;
	}

	public QuestionarioDO getQuestionario() {
		return questionario;
	}

	public void setQuestionario(QuestionarioDO questionario) {
		this.questionario = questionario;
	}

	public String getDomanda() {
		return domanda;
	}

	public void setDomanda(String domanda) {
		this.domanda = domanda;
	}

	public String getTipoRisposta() {
		return TipoRisposta;
	}

	public void setTipoRisposta(String tipoRisposta) {
		TipoRisposta = tipoRisposta;
	}
	
	
	
}
