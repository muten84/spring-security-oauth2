package it.eng.area118.mdo.jme.data;

import java.io.Serializable;

import org.garret.perst.Persistent;

public class Province extends Persistent implements Geographical, Serializable {
	
	public static String getType() {
		return "Province";
	}


	protected String name;

	protected String code;

	protected Region region;

	public Province() {
	}

	public Province(String name, Region region) {
		this.name = name;
		this.region = region;
	}

	public Province(String code, String name, Region region) {
		this.code = code;
		this.name = name;
		this.region = region;
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

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

}
