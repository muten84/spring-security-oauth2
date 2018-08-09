package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.type.ForeignKeyDirection;


/**
 * The persistent class for the TS_BOOKING_DPI database table.
 * 
 */
@Entity
@Table(name="TS_BOOKING_DPI")
public class TsBookingDpiDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@Column(name="BOOKING_ID")
	private String bookingId;

	@OneToOne
	@JoinColumn(name="DPI_ID", referencedColumnName="ID")	
	private TsEquipmentDO equipment;

	
	public TsBookingDpiDO() {
	}

	public String getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

//	public String getDpiId() {
//		return this.dpiId;
//	}
//
//	public void setDpiId(String dpiId) {
//		this.dpiId = dpiId;
//	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the equipment
	 */
	public TsEquipmentDO getEquipment() {
		return equipment;
	}

	/**
	 * @param equipment the equipment to set
	 */
	public void setEquipment(TsEquipmentDO equipment) {
		this.equipment = equipment;
	}
	
	

}