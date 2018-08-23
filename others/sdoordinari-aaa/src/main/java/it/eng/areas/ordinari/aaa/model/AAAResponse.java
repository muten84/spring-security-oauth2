/**
 * 
 */
package it.eng.areas.ordinari.aaa.model;

import java.io.Serializable;

/**
 * @author Bifulco Luigi
 *
 */
public class AAAResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8032678556138063447L;
	private String result;

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

}
