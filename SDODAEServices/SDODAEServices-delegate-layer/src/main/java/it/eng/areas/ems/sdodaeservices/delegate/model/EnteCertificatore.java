/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.io.Serializable;

/**
 * @author Bifulco Luigi
 *
 */
public class EnteCertificatore implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1612325227700917568L;

	private String id;

	private String descrizione;

	private boolean onlyForMed;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public boolean isOnlyForMed() {
		return onlyForMed;
	}

	public void setOnlyForMed(boolean onlyForMed) {
		this.onlyForMed = onlyForMed;
	}


}
