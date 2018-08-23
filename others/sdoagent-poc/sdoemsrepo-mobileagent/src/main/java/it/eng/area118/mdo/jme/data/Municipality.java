package it.eng.area118.mdo.jme.data;

import java.io.Serializable;

import org.garret.perst.Persistent;

public class Municipality extends Persistent implements Geographical, Serializable {
	
	public static String getType() {
		return "Municipality";
	}

	protected String code;

	protected String name;

	protected Province province;

	public Municipality() {
	}

	public Municipality(String code, String name, Province province) {
		this.code = code;
		this.name = name;
		this.province = province;
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

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String toString() {
		return name;
	}

}