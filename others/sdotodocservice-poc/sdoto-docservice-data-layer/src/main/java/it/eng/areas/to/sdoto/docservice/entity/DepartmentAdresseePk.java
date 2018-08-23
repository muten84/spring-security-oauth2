/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.entity;

import java.io.Serializable;

/**
 * @author Bifulco Luigi
 *
 */
public class DepartmentAdresseePk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3780091494768867941L;

	private String depId;

	private String address;

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

}
