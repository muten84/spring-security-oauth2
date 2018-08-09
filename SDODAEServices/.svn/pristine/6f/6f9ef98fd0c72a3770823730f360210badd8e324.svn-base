package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "GPSLocation", description = "Modello dati che rappresenta una coordinata GPS")
@JsonInclude(Include.NON_NULL)
public class GPSLocation extends Location {

	private String gpsId;

	private Float latitudine;

	private Float longitudine;

	private Float altitudine;

	private Float accuratezza;

	private Float error;

	private Date timeStamp;

	public String getGpsId() {
		return gpsId;
	}

	public void setGpsId(String gpsId) {
		this.gpsId = gpsId;
	}

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

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
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

	public Float getAccuratezza() {
		return accuratezza;
	}

	public void setAccuratezza(Float accuratezza) {
		this.accuratezza = accuratezza;
	}

}
