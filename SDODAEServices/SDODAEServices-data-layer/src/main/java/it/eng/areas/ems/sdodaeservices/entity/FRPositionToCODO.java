package it.eng.areas.ems.sdodaeservices.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_FR_POSITION_TO_CO")
public class FRPositionToCODO {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne
	@JoinColumn(name = "FIRST_RESPONDER_ID")
	private FirstResponderDO firstResponder;

	private Float latitudine;

	private Float longitudine;

	private Float accuratezza;

	@Column(name = "TIME_STAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar timeStamp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FirstResponderDO getFirstResponder() {
		return firstResponder;
	}

	public void setFirstResponder(FirstResponderDO firstResponder) {
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
