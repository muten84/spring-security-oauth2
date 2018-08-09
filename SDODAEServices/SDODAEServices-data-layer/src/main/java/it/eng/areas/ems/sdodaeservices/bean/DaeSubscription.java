/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.bean;

import java.io.Serializable;

/**
 * @author Bifulco Luigi
 *
 */
public class DaeSubscription implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5633564019726773017L;

	private int numDAE;

	private String aaaammgg;

	/**
	 * @return the numDAE
	 */
	public int getNumDAE() {
		return numDAE;
	}

	/**
	 * @param numDAE
	 *            the numDAE to set
	 */
	public void setNumDAE(int numDAE) {
		this.numDAE = numDAE;
	}

	public String getAaaammgg() {
		return aaaammgg;
	}

	public void setAaaammgg(String aaaammgg) {
		this.aaaammgg = aaaammgg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DaeSubscription [numDAE=" + numDAE + ", aaaammgg=" + aaaammgg + "]";
	}

}
