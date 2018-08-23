package it.eng.areas.ems.sdodaeservices.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "DAE_STATO_RICHIESTA")
public class StatoRichiestaDO implements Serializable {

   @Id
   @Column(name= "ID")
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

	public StatoRichiestaDO() {
		super();
		// TODO Auto-generated constructor stub
	}


	

}
