package it.eng.areas.ems.sdodaeservices.datasource.constant;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlEnum
@XmlAccessorType(XmlAccessType.FIELD)
public enum DataSourceType implements Serializable {

	DEFAULT,DINAMICO, STORICO, CARD, ELICARD, GIS, FIRST_AID;

	private String type;

	DataSourceType() {

	}

	DataSourceType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}

}
