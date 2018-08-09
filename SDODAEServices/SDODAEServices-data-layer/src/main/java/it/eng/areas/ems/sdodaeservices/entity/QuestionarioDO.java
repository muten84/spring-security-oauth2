package it.eng.areas.ems.sdodaeservices.entity;
import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "DAE_QUESTIONARIO")
public class QuestionarioDO implements Serializable {

	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
   @Column(name= "ID")
   private String id;
	
	@OneToMany(mappedBy = "questionario", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Set<DomandaQuestionarioDO> domandaQuestionario;
	

	private String descrizione;

	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public Set<DomandaQuestionarioDO> getDomandaQuestionario() {
		return domandaQuestionario;
	}



	public void setDomandaQuestionario(
			Set<DomandaQuestionarioDO> domandaQuestionario) {
		this.domandaQuestionario = domandaQuestionario;
	}



	public String getDescrizione() {
		return descrizione;
	}



	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}



	public QuestionarioDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
