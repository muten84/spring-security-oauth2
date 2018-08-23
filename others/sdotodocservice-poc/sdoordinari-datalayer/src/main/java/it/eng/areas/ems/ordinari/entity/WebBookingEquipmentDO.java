package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the WEB_BOOKING_EQUIPMENT database table.
 * 
 */
@Entity
@Table(name="WEB_BOOKING_EQUIPMENT")
public class WebBookingEquipmentDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="EQUIPMENT_COMPACT")
	private String equipmentCompact;

	@Column(name="EQUIPMENT_ID")
	private String equipmentId;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WEB_BOOKING_ID")
	private WebBookingDO webBooking;

	public WebBookingEquipmentDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEquipmentCompact() {
		return this.equipmentCompact;
	}

	public void setEquipmentCompact(String equipmentCompact) {
		this.equipmentCompact = equipmentCompact;
	}

	public String getEquipmentId() {
		return this.equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}


	public WebBookingDO getWebBooking() {
		return webBooking;
	}

	public void setWebBooking(WebBookingDO webBooking) {
		this.webBooking = webBooking;
	}

	
}