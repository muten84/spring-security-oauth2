package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
@Table(name = "DAE_LOCATION")
public class LocationDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMUNE")
	private ComuneDO comune;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INDIRIZZO")
	private StradeDO indirizzo;

	private String civico;

	@Column(name = "LUOGO_PUBBLICO")
	private String luogoPubblico;

	@Column(name = "TIPO_LUOGO_PUBBLICO")
	private String tipoLuogoPubblico;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "GPSLOCATION")
	private GPSLocationDO gpsLocation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCALITA")
	private LocalitaDO localita;

	public ComuneDO getComune() {
		return comune;
	}

	public void setComune(ComuneDO comune) {
		this.comune = comune;
	}

	public StradeDO getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(StradeDO indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getLuogoPubblico() {
		return luogoPubblico;
	}

	public void setLuogoPubblico(String luogoPubblico) {
		this.luogoPubblico = luogoPubblico;
	}

	public String getTipoLuogoPubblico() {
		return tipoLuogoPubblico;
	}

	public void setTipoLuogoPubblico(String tipoLuogoPubblico) {
		this.tipoLuogoPubblico = tipoLuogoPubblico;
	}

	public GPSLocationDO getGpsLocation() {
		return gpsLocation;
	}

	public void setGpsLocation(GPSLocationDO gpsLocation) {
		this.gpsLocation = gpsLocation;
	}

	public LocalitaDO getLocalita() {
		return localita;
	}

	public void setLocalita(LocalitaDO localita) {
		this.localita = localita;
	}

	public LocationDO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
