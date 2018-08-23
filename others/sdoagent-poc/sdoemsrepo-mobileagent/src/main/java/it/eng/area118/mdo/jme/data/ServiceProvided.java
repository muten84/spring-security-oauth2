package it.eng.area118.mdo.jme.data;

import java.io.Serializable;

import org.garret.perst.Persistent;

public class ServiceProvided extends Persistent implements Item, Serializable {
	
	public static String getType() {
		return "ServiceProvided";
	}

	protected String code;

	protected String name;

	public ServiceProvided() {

	}

	public ServiceProvided(String code, String name) {
		this.code = code;
		this.name = name;
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
		ServiceProvided other = (ServiceProvided) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int hashCode() {
		System.out.println("ServiceProvided.hashCode()");
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		System.out.println("ServiceProvided.equals()");
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceProvided other = (ServiceProvided) obj;
		System.out.println("ServiceProvided.equals()" + name + " - " + other.name);
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
