package it.eng.areas.ems.sdodaeservices.delegate.model;


import java.io.Serializable;


public class ProfessioneFilter  implements Serializable {

	/**
	 * 
	 */
	
	protected String id;
	
	private String name;
	
		
	public ProfessioneFilter() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	



}
