/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.io.Serializable;

/**
 * @author Bifulco Luigi
 *
 */
public class FirstResponderSubscriptionDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1521823349158761890L;

	private int numIscritti;

	private String aaaammgg;

	/**
	 * @return the numIscritti
	 */
	public int getNumIscritti() {
		return numIscritti;
	}

	/**
	 * @param numIscritti
	 *            the numIscritti to set
	 */
	public void setNumIscritti(int numIscritti) {
		this.numIscritti = numIscritti;
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
		return "FirstResponderSubscriptionDTO [numIscritti=" + numIscritti + ", aaaammgg=" + aaaammgg + "]";
	}

}
