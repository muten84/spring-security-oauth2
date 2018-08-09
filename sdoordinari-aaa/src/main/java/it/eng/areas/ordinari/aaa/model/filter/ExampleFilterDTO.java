/**
 * 
 */
package it.eng.areas.ordinari.aaa.model.filter;

import java.io.Serializable;


public class ExampleFilterDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3321047414940218709L;

	private String type;

	private String fetchRule;

	private String compact;

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

	/**
	 * @return the fetchRule
	 */
	public String getFetchRule() {
		return fetchRule;
	}

	/**
	 * @param fetchRule
	 *            the fetchRule to set
	 */
	public void setFetchRule(String fetchRule) {
		this.fetchRule = fetchRule;
	}

	/**
	 * @return the compact
	 */
	public String getCompact() {
		return compact;
	}

	/**
	 * @param compact
	 *            the compact to set
	 */
	public void setCompact(String compact) {
		this.compact = compact;
	}

}
