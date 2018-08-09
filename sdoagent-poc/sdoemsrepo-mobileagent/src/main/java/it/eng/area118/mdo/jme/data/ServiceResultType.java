package it.eng.area118.mdo.jme.data;

import java.io.Serializable;

import org.garret.perst.Persistent;

public class ServiceResultType extends Persistent implements Serializable, Item {
	
	public static String getType() {
		return "ServiceResultType";
	}


	protected String name;

	protected String code;

	protected String refCode;

	public ServiceResultType() {

	}

	/**
	 * @param name
	 * @param code
	 * @param refCode
	 */
	public ServiceResultType(String name, String code, String refCode) {
		this.name = name;
		this.code = code;
		this.refCode = refCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public String getRefCode() {
		return refCode;
	}

	public void setRefCode(String refCode) {
		this.refCode = refCode;
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

}
