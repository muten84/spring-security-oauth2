/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.io.Serializable;

/**
 * @author Bifulco Luigi
 *
 */
public class DaeEventActivationDTO implements Serializable {

	private int numAttivazioni;

	private String provincia;

	private String aaaammgg;

	/**
	 * @return the numAttivazioni
	 */
	public int getNumAttivazioni() {
		return numAttivazioni;
	}

	/**
	 * @param numAttivazioni
	 *            the numAttivazioni to set
	 */
	public void setNumAttivazioni(int numAttivazioni) {
		this.numAttivazioni = numAttivazioni;
	}

	/**
	 * @return the provincia
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * @param provincia
	 *            the provincia to set
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getAaaammgg() {
		return aaaammgg;
	}

	public void setAaaammgg(String aaaammgg) {
		this.aaaammgg = aaaammgg;
	}

}
