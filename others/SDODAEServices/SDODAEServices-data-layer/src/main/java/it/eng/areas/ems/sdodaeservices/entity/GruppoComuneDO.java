package it.eng.areas.ems.sdodaeservices.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_GRUPPO_COMUNE")
public class GruppoComuneDO {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne
	@JoinColumn(name = "GRUPPO_ID")
	private GruppoDO gruppo;

	@Column(name = "NOME_COMUNE")
	private String nomeComune;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GruppoDO getGruppo() {
		return gruppo;
	}

	public void setGruppo(GruppoDO gruppo) {
		this.gruppo = gruppo;
	}

	public String getNomeComune() {
		return nomeComune;
	}

	public void setNomeComune(String nomeComune) {
		this.nomeComune = nomeComune;
	}

}
