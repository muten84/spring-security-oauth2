package it.eng.areas.ems.sdodaeservices.delegate.model;
import java.util.Calendar;

public class LogAlertDAE {

	private String id;

	private String idDAE;

	private String type;

	private Integer descrizione;

	private Calendar timestamp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdDAE() {
		return idDAE;
	}

	public void setIdDAE(String idDAE) {
		this.idDAE = idDAE;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(int descrizione) {
		this.descrizione = descrizione;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	
	
}
