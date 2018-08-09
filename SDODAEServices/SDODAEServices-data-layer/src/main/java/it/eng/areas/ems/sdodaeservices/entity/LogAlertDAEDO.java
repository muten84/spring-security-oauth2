package it.eng.areas.ems.sdodaeservices.entity;
import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "DAE_LOG_ALERT_DAE")
public class LogAlertDAEDO implements Serializable {

	   @Id
	   @Column(name= "ID")
	private String id;

	private String idDAE;

	private String type;

	private Integer descrizione;

	private Calendar timestamp;

	
	public LogAlertDAEDO() {
		super();
		// TODO Auto-generated constructor stub
	}

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
