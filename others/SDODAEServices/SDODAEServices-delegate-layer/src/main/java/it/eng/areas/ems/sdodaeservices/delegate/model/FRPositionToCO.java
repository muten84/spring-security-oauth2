package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.Calendar;

public class FRPositionToCO {

	private String id;

	private FirstResponder firstResponder;

	private Float latitudine;

	private Float longitudine;

	private Float accuratezza;

	private Calendar timeStamp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FirstResponder getFirstResponder() {
		return firstResponder;
	}

	public void setFirstResponder(FirstResponder firstResponder) {
		this.firstResponder = firstResponder;
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

	public Calendar getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Calendar timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Float getAccuratezza() {
		return accuratezza;
	}

	public void setAccuratezza(Float accuratezza) {
		this.accuratezza = accuratezza;
	}

}
