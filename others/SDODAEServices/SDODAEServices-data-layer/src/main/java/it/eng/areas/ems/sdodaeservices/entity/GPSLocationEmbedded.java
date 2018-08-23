package it.eng.areas.ems.sdodaeservices.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class GPSLocationEmbedded {

	private Float latitudine;

	private Float longitudine;

	private Float altitudine;

	private Float error;

	@Column(name = "TIME_STAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;

	public Float getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(Float latitudine) {
		this.latitudine = latitudine;
	}

	public Float getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(Float longitudine) {
		this.longitudine = longitudine;
	}

	public Float getAltitudine() {
		return altitudine;
	}

	public void setAltitudine(Float altitudine) {
		this.altitudine = altitudine;
	}

	public Float getError() {
		return error;
	}

	public void setError(Float error) {
		this.error = error;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

}
