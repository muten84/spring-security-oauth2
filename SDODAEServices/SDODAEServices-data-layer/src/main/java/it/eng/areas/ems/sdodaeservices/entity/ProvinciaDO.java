package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_PROVINCIA")
public class ProvinciaDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@Column(name = "NOME_PROVINCIA")
	private String nomeProvincia;

	@Column(name = "SIGLA_AUTOMOBILISTICA")
	private String sigla;

	@Column(name = "DELETED")
	private Boolean deleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNomeProvincia() {
		return nomeProvincia;
	}

	public void setNomeProvincia(String nomeProvincia) {
		this.nomeProvincia = nomeProvincia;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public ProvinciaDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla
	 *            the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
