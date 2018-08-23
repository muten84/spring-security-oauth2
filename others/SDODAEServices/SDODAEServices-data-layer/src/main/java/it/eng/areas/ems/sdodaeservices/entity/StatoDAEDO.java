package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DAE_STATO_DAE")
public class StatoDAEDO implements Serializable {

	@Id
	@Column(name = "ID")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DAE")
	private DaeDO dae;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATO")
	private StatoDO stato;

	@Column(name = "VALIDO_DA")
	private Calendar validoDa;

	@Column(name = "VALIDO_A")
	private Calendar validoA;

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

	public StatoDO getStato() {
		return stato;
	}

	public void setStato(StatoDO stato) {
		this.stato = stato;
	}

	public Calendar getValidoDa() {
		return validoDa;
	}

	public void setValidoDa(Calendar validoDa) {
		this.validoDa = validoDa;
	}

	public Calendar getValidoA() {
		return validoA;
	}

	public void setValidoA(Calendar validoA) {
		this.validoA = validoA;
	}

	public StatoDAEDO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
