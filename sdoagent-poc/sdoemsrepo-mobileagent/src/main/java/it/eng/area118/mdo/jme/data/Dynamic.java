package it.eng.area118.mdo.jme.data;

import java.io.Serializable;

import org.garret.perst.Persistent;

public class Dynamic extends Persistent implements Item, Serializable {
	
	public static String getType() {
		return "Dynamic";
	}

	public final static String ACCIDENT = "ACCIDENT";
	public final static String OTHER = "OTHER";

	protected String code;

	protected String name;

	protected String accident;

	public Dynamic() {

	}

	public Dynamic(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public Dynamic(String code, String name, String accident) {
		this.code = code;
		this.name = name;
		this.accident = accident;
	}

	public boolean isAccident() {
		if (accident == null) {
			return false;
		}
		if (!accident.equals("ACCIDENT")) {
			return false;
		}
		return true;
	}

	public void setAccident() {
		accident = "ACCIDENT";
	}

	public String getAccident() {
		return this.accident;
	}

	public void setAccident(String accident) {
		this.accident = accident;
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

	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dynamic other = (Dynamic) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
