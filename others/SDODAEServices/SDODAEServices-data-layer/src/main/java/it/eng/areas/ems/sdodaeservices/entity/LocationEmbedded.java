package it.eng.areas.ems.sdodaeservices.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class LocationEmbedded {

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

	@Embedded
	private GPSLocationEmbedded gpsLocation;

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

	public GPSLocationEmbedded getGpsLocation() {
		return gpsLocation;
	}

	public void setGpsLocation(GPSLocationEmbedded gpsLocation) {
		this.gpsLocation = gpsLocation;
	}

}
