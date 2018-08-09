package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_INTERVENTO_COORD")
public class InterventoCoordDO implements Serializable {

	public enum Type {
		A, U
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INTERVENTO_ID")
	private InterventoDO intervento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS_COORDINATA")
	private Date dataCreazione;

	@Column(name = "LATITUDINE")
	private Double latitudine;

	@Column(name = "LONGITUDINE")
	private Double longitudine;

	@Column(name = "DISTANZA")
	private Double distanza;

	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE")
	private Type type;

	public InterventoCoordDO() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InterventoDO getIntervento() {
		return intervento;
	}

	public void setIntervento(InterventoDO intervento) {
		this.intervento = intervento;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Double getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(Double latitudine) {
		this.latitudine = latitudine;
	}

	public Double getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(Double longitudine) {
		this.longitudine = longitudine;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Double getDistanza() {
		return distanza;
	}

	public void setDistanza(Double distanza) {
		this.distanza = distanza;
	}

}
