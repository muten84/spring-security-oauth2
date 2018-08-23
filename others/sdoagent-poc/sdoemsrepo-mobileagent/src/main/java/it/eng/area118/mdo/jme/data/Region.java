package it.eng.area118.mdo.jme.data;

import java.io.Serializable;

import org.garret.perst.Persistent;

public class Region extends Persistent implements Geographical, Serializable {
	
	public static String getType() {
		return "Region";
	}


	protected String code;

	protected String name;

	protected Country country;

	public Region() {
	}

	public Region(String code, String name, Country country) {
		this.code = code;
		this.name = name;
		this.country = country;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

}