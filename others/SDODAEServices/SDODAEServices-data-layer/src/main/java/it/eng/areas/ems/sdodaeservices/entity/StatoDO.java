package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DAE_STATO")
public class StatoDO implements Serializable {

	@Id
	@Column(name = "ID")
	private String id;

	private String nome;

	private Boolean visible;

	private Boolean visible118;

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

	public StatoDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StatoDO(String id) {
		super();
		this.id = id;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getVisible118() {
		return visible118;
	}

	public void setVisible118(Boolean visible118) {
		this.visible118 = visible118;
	}

}
