package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "DAE_UBICAZIONE")
public class UbicazioneDO implements Serializable {

   @Id
   @Column(name= "ID")
	private String id;

	private Integer tipo;

	private Integer descrizione;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(int descrizione) {
		this.descrizione = descrizione;
	}

	public UbicazioneDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
}
