package it.eng.areas.ems.sdodaeservices.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_PASSWORD_HISTORY")
public class PasswordHistoryDO {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@Column(name = "PASSWORD_HASH")
	private String passwordHash;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UTENTE_ID")
	private UtenteDO utente;

	@Column(name = "CREATION_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(name = "TEMPORANY")
	private boolean temporany;

	@Column(name = "TYPE")
	private String type;

	public PasswordHistoryDO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UtenteDO getUtente() {
		return utente;
	}

	public void setUtente(UtenteDO utente) {
		this.utente = utente;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean isTemporany() {
		return temporany;
	}

	public void setTemporany(boolean temporany) {
		this.temporany = temporany;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
