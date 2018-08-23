package it.eng.areas.ems.ordinari.model;

import java.io.Serializable;

public class WebBookingEquipment implements Serializable {

	private String id;
	private String equipmentCompact;
	private String equipmentId;
	private WebBooking webBooking;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEquipmentCompact() {
		return equipmentCompact;
	}
	public void setEquipmentCompact(String equipmentCompact) {
		this.equipmentCompact = equipmentCompact;
	}
	public String getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}
	public WebBooking getWebBooking() {
		return webBooking;
	}
	public void setWebBooking(WebBooking webBooking) {
		this.webBooking = webBooking;
	}

}