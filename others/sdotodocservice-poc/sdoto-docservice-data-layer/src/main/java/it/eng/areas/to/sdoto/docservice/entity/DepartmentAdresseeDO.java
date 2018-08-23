/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @author Bifulco Luigi
 *
 */

@Entity
@Table(name = "DEPARTMENT_ADDRESSEE")
@IdClass(DepartmentAdresseePk.class)
public class DepartmentAdresseeDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4691871535788828671L;

	@Id
	@Column(name = "DEPARTMENT_ID", nullable = false)
	private String depId;

	@Column
	private String description;

	@Id
	@Column(name = "ADDRESS", nullable = false)
	private String address;

	@Column(name = "WEB_IDENTITY_ID")
	private String userId;

	public DepartmentAdresseeDO() {

	}

	/**
	 * @return the depId
	 */
	public String getDepId() {
		return depId;
	}

	/**
	 * @param depId
	 *            the depId to set
	 */
	public void setDepId(String depId) {
		this.depId = depId;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
