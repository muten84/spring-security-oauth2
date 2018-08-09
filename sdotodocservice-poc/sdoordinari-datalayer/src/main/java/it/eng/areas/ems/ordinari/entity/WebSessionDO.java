package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the WEB_SESSIONS database table.
 * 
 */
@Entity
@Table(name="WEB_SESSIONS")
public class WebSessionDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	private String id;

	private String address;

	@Temporal(TemporalType.DATE)
	@Column(name="LOGIN_TIMESTAMP")
	private Date loginTimestamp;

	@Column(name="SESSION_ID")
	private String sessionId;

	@Column(name="WEB_IDENTITY_ID")
	private String webIdentityId;

	public WebSessionDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getLoginTimestamp() {
		return this.loginTimestamp;
	}

	public void setLoginTimestamp(Date loginTimestamp) {
		this.loginTimestamp = loginTimestamp;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getWebIdentityId() {
		return this.webIdentityId;
	}

	public void setWebIdentityId(String webIdentityId) {
		this.webIdentityId = webIdentityId;
	}

}