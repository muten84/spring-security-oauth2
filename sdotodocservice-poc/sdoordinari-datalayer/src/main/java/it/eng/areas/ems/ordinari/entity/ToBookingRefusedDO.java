package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the TO_BOOKING_REFUSED database table.
 * 
 */
@Entity
@Table(name="TO_BOOKING_REFUSED")
public class ToBookingRefusedDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="BOOKING_ID")
	private String bookingId;

	@Column(name="PARKING_ID")
	private String parkingId;

	@Column(name="REFUSAL_REASON")
	private String refusalReason;

	@Temporal(TemporalType.DATE)
	@Column(name="REFUSE_DATE")
	private Date refuseDate;

	public ToBookingRefusedDO() {
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

	public String getParkingId() {
		return this.parkingId;
	}

	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}

	public String getRefusalReason() {
		return this.refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public Date getRefuseDate() {
		return this.refuseDate;
	}

	public void setRefuseDate(Date refuseDate) {
		this.refuseDate = refuseDate;
	}

}