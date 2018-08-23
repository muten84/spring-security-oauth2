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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_DISPONIBILITA_SPECIFICA")
public class DisponibilitaSpecificaDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISPONIBILITA")
	private DisponibileDO disponibilita;

	@OneToMany(mappedBy = "disponibilitaSpecifica", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	private Set<DisponibilitaGiornalieraDO> disponibilitaGiornaliera;

	public DisponibilitaSpecificaDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DisponibileDO getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(DisponibileDO disponibilita) {
		this.disponibilita = disponibilita;
	}

	public Set<DisponibilitaGiornalieraDO> getDisponibilitaGiornaliera() {
		return disponibilitaGiornaliera;
	}

	public void setDisponibilitaGiornaliera(Set<DisponibilitaGiornalieraDO> disponibilitaGiornaliere) {
		this.disponibilitaGiornaliera = disponibilitaGiornaliere;
	}

}
