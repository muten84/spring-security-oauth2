package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the WEB_IDENTITY database table.
 * 
 */
@Entity
@Table(name = "WEB_IDENTITY")
public class WebIdentityDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	private String description;

	private String email;

	private String login;

	private String passwd;

	@Column(name = "TABLE_ID")
	private String tableId;

	@Column(name = "TABLE_TYPE")
	private String tableType;

	@Column(name = "PASSWORD_MODIFY_DATE")
	private Date passwdModifyDate;

//	@Column(name = "NEW_FLAG")
//	private Boolean isNew;

	public WebIdentityDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getTableId() {
		return this.tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTableType() {
		return this.tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	/**
	 * @return the passwdModifyDate
	 */
	public Date getPasswdModifyDate() {
		return passwdModifyDate;
	}

	/**
	 * @param passwdModifyDate
	 *            the passwdModifyDate to set
	 */
	public void setPasswdModifyDate(Date passwdModifyDate) {
		this.passwdModifyDate = passwdModifyDate;
	}

//	/**
//	 * @return the isNew
//	 */
//	public Boolean getIsNew() {
//		if(isNew == null){
//			return false;
//		}
//		return isNew;
//	}
//
//	/**
//	 * @param isNew
//	 *            the isNew to set
//	 */
//	public void setIsNew(Boolean isNew) {
//		this.isNew = isNew;
//	}

}