package it.eng.areas.ems.sdodaeservices.rest.model;

import java.util.HashMap;
import java.util.Map;

public class Feature {

	private Geometry geometry;

	private String type;

	private Map<String, Object> properties;

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getProperties() {
		if (properties == null) {
			properties = new HashMap<>();
		}
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
