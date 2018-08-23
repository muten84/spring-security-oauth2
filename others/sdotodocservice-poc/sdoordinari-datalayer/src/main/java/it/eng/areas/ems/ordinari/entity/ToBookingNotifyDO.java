package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the TO_BOOKING_NOTIFY database table.
 * 
 */
@Entity
@Table(name="TO_BOOKING_NOTIFY")
public class ToBookingNotifyDO implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	
	@Column(name="BOOKING_CODE")
	private String bookingCode;

	@Column(name="BOOKING_ID")
	private String bookingId;

	@Column(name="NOTIFY_CODE")
	private String notifyCode;

	@Temporal(TemporalType.DATE)
	@Column(name="NOTIFY_DATE")
	private Date notifyDate;

	@Column(name="NOTIFY_DEST")
	private String notifyDest;

	@Column(name="NOTIFY_SYS")
	private String notifySys;

	@Column(name="NOTIFY_USER")
	private String notifyUser;

	public ToBookingNotifyDO() {
	}

	public String getBookingCode() {
		return this.bookingCode;
	}

	public void setBookingCode(String bookingCode) {
		this.bookingCode = bookingCode;
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

	public String getNotifyCode() {
		return this.notifyCode;
	}

	public void setNotifyCode(String notifyCode) {
		this.notifyCode = notifyCode;
	}

	public Date getNotifyDate() {
		return this.notifyDate;
	}

	public void setNotifyDate(Date notifyDate) {
		this.notifyDate = notifyDate;
	}

	public String getNotifyDest() {
		return this.notifyDest;
	}

	public void setNotifyDest(String notifyDest) {
		this.notifyDest = notifyDest;
	}

	public String getNotifySys() {
		return this.notifySys;
	}

	public void setNotifySys(String notifySys) {
		this.notifySys = notifySys;
	}

	public String getNotifyUser() {
		return this.notifyUser;
	}

	public void setNotifyUser(String notifyUser) {
		this.notifyUser = notifyUser;
	}

}