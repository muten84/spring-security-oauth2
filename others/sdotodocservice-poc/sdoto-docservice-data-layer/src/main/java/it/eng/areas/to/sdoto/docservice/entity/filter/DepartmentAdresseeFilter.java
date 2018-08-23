/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.entity.filter;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;

/**
 * Filtro utile per cercare gli indirizzi abilitati sulle postazioni a ricevere dei documenti
 * 
 * @author Bifulco Luigi
 *
 */
public class DepartmentAdresseeFilter extends EntityFilter {

	private String department;

	private String email;

	private boolean exactDepartmentMatch;

	public DepartmentAdresseeFilter() {

	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the exactDepartmentMatch
	 */
	public boolean isExactDepartmentMatch() {
		return exactDepartmentMatch;
	}

	/**
	 * @param exactDepartmentMatch
	 *            the exactDepartmentMatch to set
	 */
	public void setExactDepartmentMatch(boolean exactDepartmentMatch) {
		this.exactDepartmentMatch = exactDepartmentMatch;
	}

}
