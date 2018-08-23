package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the WEB_IDENTITY_DEPARTMENTS database table.
 * 
 */
@Entity
@Table(name="WEB_IDENTITY_DEPARTMENTS")
public class WebIdentityDepartmentDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="WEB_DEPARTMENT_ID")
	private String webDepartmentId;

	@Column(name="WEB_IDENTITY_ID")
	private String webIdentityId;

	public WebIdentityDepartmentDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWebDepartmentId() {
		return this.webDepartmentId;
	}

	public void setWebDepartmentId(String webDepartmentId) {
		this.webDepartmentId = webDepartmentId;
	}

	public String getWebIdentityId() {
		return this.webIdentityId;
	}

	public void setWebIdentityId(String webIdentityId) {
		this.webIdentityId = webIdentityId;
	}

}