package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the TS_BOOKING_EQUIPMENT database table.
 * 
 */
@Entity
@Table(name = "TS_BOOKING_EQUIPMENT")
public class TsBookingEquipmentDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	@Column(name = "BOOKING_ID")
	private String bookingId;

	// @Column(name="EQUIPMENT_ID")
	// private String equipmentId;

	@OneToOne
	private TsEquipmentDO equipment;

	public TsBookingEquipmentDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	/**
	 * @return the equipment
	 */
	public TsEquipmentDO getEquipment() {
		return equipment;
	}

	/**
	 * @param equipment
	 *            the equipment to set
	 */
	public void setEquipment(TsEquipmentDO equipment) {
		this.equipment = equipment;
	}

}