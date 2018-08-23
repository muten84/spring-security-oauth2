package it.eng.area118.mdo.jme.data;

import java.io.Serializable;

import org.garret.perst.Persistent;

public class Country extends Persistent implements Geographical, Serializable {
	
	public static String getType() {
		return "COUNTRY";
	}

	private String code;

	private String name;

	public Country() {
	}

	public Country(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String toString() {
		String str = "";
		if (this.code != null) {
			str += this.code;
		}
		return str;
	}

}
