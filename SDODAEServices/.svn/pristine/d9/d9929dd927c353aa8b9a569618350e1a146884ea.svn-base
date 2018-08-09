package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "DAE_MOBILE_TOKEN")
public class DaeMobileTokenDO implements Serializable {

	public enum UserType {
		USER, FR
	}

	@Id
	@Column(name = "TOKEN")
	private String token;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATION_TIMESTAMP")
	private Calendar creationTimeStamp;

	@Column(name = "UTENTE_ID")
	private String utenteId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RENEWED_TIMESTAMP")
	private Calendar renewedTimeStamp;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRED_TIMESTAMP")
	private Calendar expiredTimeStamp;

	public DaeMobileTokenDO() {
		// TODO Auto-generated constructor stub
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Calendar getCreationTimeStamp() {
		return creationTimeStamp;
	}

	public void setCreationTimeStamp(Calendar creationTimeStamp) {
		this.creationTimeStamp = creationTimeStamp;
	}

	public Calendar getRenewedTimeStamp() {
		return renewedTimeStamp;
	}

	public void setRenewedTimeStamp(Calendar renewedTimeStamp) {
		this.renewedTimeStamp = renewedTimeStamp;
	}

	public Calendar getExpiredTimeStamp() {
		return expiredTimeStamp;
	}

	public void setExpiredTimeStamp(Calendar expiredTimeStamp) {
		this.expiredTimeStamp = expiredTimeStamp;
	}

	public String getUtenteId() {
		return utenteId;
	}

	public void setUtenteId(String utenteId) {
		this.utenteId = utenteId;
	}

}
