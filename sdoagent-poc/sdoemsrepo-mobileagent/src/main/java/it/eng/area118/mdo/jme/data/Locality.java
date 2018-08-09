package it.eng.area118.mdo.jme.data;

import java.io.Serializable;

public class Locality implements Geographical, Serializable {
	
	public static String getType() {
		return "Locality";
	}

	protected String name;

	protected Municipality municipality;

	public Locality() {
	}

	public Locality(String name, Municipality municipality) {
		this.name = name;
		this.municipality = municipality;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Municipality getMunicipality() {
		return municipality;
	}

	public void setMunicipality(Municipality municipality) {
		this.municipality = municipality;
	}

	public String toString() {
		return name;
	}

}
