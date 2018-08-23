package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_DISPONIBILITA")
public class DisponibileDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DAE")
	private DaeDO dae;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "disponibilita")
	private DisponibilitaSpecificaDO disponibilitaSpecifica;

	@Temporal(TemporalType.DATE)
	private Date da;

	@Temporal(TemporalType.DATE)
	private Date a;

	@Column(name = "ORARIO_DA")
	private String orarioDa;

	@Column(name = "ORARIO_A")
	private String orarioA;

	private boolean deleted;

	public DisponibileDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DaeDO getDae() {
		return dae;
	}

	public void setDae(DaeDO dae) {
		this.dae = dae;
	}

	public Date getDa() {
		return da;
	}

	public void setDa(Date da) {
		this.da = da;
	}

	public Date getA() {
		return a;
	}

	public void setA(Date a) {
		this.a = a;
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

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public DisponibilitaSpecificaDO getDisponibilitaSpecifica() {
		return disponibilitaSpecifica;
	}

	public void setDisponibilitaSpecifica(DisponibilitaSpecificaDO disponibilitaSpecifica) {
		this.disponibilitaSpecifica = disponibilitaSpecifica;
	}

}
