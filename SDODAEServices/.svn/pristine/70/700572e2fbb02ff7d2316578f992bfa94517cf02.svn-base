package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_COMUNE")
public class ComuneDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@Column(name = "NOME_COMUNE")
	private String nomeComune;

	@Column(name = "CODICE_ISTAT")
	private String codiceIstat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROVINCIA_ID")
	private ProvinciaDO provincia;

	@Column(name = "DELETED")
	private Boolean deleted;

	public ComuneDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNomeComune() {
		return nomeComune;
	}

	public void setNomeComune(String nomeComune) {
		this.nomeComune = nomeComune;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}

	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	public ProvinciaDO getProvincia() {
		return provincia;
	}

	public void setProvincia(ProvinciaDO provincia) {
		this.provincia = provincia;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
