package it.eng.area118.mdo.jme.data;

import java.io.Serializable;

import org.garret.perst.Persistent;

public class Pathology extends Persistent implements Serializable, Item {
	
	public static String getType() {
		return "Pathology";
	}


	protected String name;

	protected String code;

	protected PathologyClass pathologyClass;

	public Pathology() {
	}

	public Pathology(String name, PathologyClass pClass) {
		this.pathologyClass = pClass;
		this.name = name;
	}

	public Pathology(String name, String code, PathologyClass pClass) {
		this.pathologyClass = pClass;
		this.name = name;
		this.code = code;
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

	public PathologyClass getPathologyClass() {
		return pathologyClass;
	}

	public void setPathologyClass(PathologyClass pathologyClass) {
		this.pathologyClass = pathologyClass;
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
		Pathology other = (Pathology) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pathologyClass == null) {
			if (other.pathologyClass != null)
				return false;
		} else if (!pathologyClass.isEquals(other.pathologyClass))
			return false;
		return true;
	}

}
