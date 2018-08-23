package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_DISPONIBILITA_GIORNALIERA")
public class DisponibilitaGiornalieraDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISPONIBILITA_SPECIFICA")
	private DisponibilitaSpecificaDO disponibilitaSpecifica;

	@Column(name = "ORARIO_DA")
	private String orarioDa;

	@Column(name = "ORARIO_A")
	private String orarioA;

	@Column(name = "GIORNO_SETTIMANA")
	@Enumerated(EnumType.STRING)
	private GiornoSettimanaEnum giornoSettimana;

	public DisponibilitaGiornalieraDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DisponibilitaSpecificaDO getDisponibilitaSpecifica() {
		return disponibilitaSpecifica;
	}

	public void setDisponibilitaSpecifica(DisponibilitaSpecificaDO disponibilitaSpecifica) {
		this.disponibilitaSpecifica = disponibilitaSpecifica;
	}

	public GiornoSettimanaEnum getGiornoSettimana() {
		return giornoSettimana;
	}

	public void setGiornoSettimana(GiornoSettimanaEnum giornoSettimana) {
		this.giornoSettimana = giornoSettimana;
	}

	public String getOrarioDa() {
		return orarioDa;
	}

	public void setOrarioDa(String orarioDa) {
		this.orarioDa = orarioDa;
	}

	public String getOrarioA() {
		return orarioA;
	}

	public void setOrarioA(String orarioA) {
		this.orarioA = orarioA;
	}

}
