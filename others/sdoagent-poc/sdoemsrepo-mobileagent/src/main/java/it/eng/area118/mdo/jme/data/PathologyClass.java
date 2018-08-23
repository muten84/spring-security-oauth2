package it.eng.area118.mdo.jme.data;

import java.io.Serializable;

import org.garret.perst.Persistent;

public class PathologyClass extends Persistent implements Item, Serializable {
	
	public static String getType() {
		return "PathologyClass";
	}

	protected String name;

	protected String code;

	public PathologyClass() {

	}

	public PathologyClass(String name) {
		this.name = name;
	}

	public PathologyClass(String name, String code) {
		this.name = name;
		this.code = code;
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
		if (this.code != null && !this.code.equals("")) {
			str += this.code;
		}
		return str;
	}

	public boolean isEquals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathologyClass other = (PathologyClass) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
