package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the TO_BOOKING_RETURN_INFO database table.
 * 
 */
@Entity
@Table(name="TO_BOOKING_RETURN_INFO")
public class ToBookingReturnInfoDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	@Column(name="BOOKING_ID")
	private String bookingId;

	@Temporal(TemporalType.DATE)
	@Column(name="RETURN_WAKEUP_DATE")
	private Date returnWakeupDate;

	@Column(name="RETURN_WAKEUP_USER_ID")
	private String returnWakeupUserId;

	@Column(name="RETURN_WAKEUP_USER_NAME")
	private String returnWakeupUserName;

	public ToBookingReturnInfoDO() {
	}

	public String getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getReturnWakeupDate() {
		return this.returnWakeupDate;
	}

	public void setReturnWakeupDate(Date returnWakeupDate) {
		this.returnWakeupDate = returnWakeupDate;
	}

	public String getReturnWakeupUserId() {
		return this.returnWakeupUserId;
	}

	public void setReturnWakeupUserId(String returnWakeupUserId) {
		this.returnWakeupUserId = returnWakeupUserId;
	}

	public String getReturnWakeupUserName() {
		return this.returnWakeupUserName;
	}

	public void setReturnWakeupUserName(String returnWakeupUserName) {
		this.returnWakeupUserName = returnWakeupUserName;
	}

}