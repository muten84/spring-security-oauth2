package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TS_INTERVENTION database table.
 * 
 */
@Entity
@Table(name="TS_INTERVENTION")
public class TsInterventionDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	private String address;

	@Column(name="ADDRESS_TYPE")
	private String addressType;

	private BigDecimal arrived;

	@Column(name="ASSIGNED_PARKING")
	private String assignedParking;

	@Column(name="BOOKING_CODE")
	private String bookingCode;

	@Column(name="BOOKING_ID")
	private String bookingId;

	private BigDecimal departed;

	private BigDecimal idx;

	@Column(name="PAUSE_TYPE")
	private String pauseType;

	private String priority;

	@Column(name="SERVICE_ID")
	private String serviceId;

	@Column(name="STATUS_COMPANION")
	private String statusCompanion;

	@Temporal(TemporalType.DATE)
	@Column(name="TRANSPORT_DATE")
	private Date transportDate;

	private String transported;

	public TsInterventionDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressType() {
		return this.addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public BigDecimal getArrived() {
		return this.arrived;
	}

	public void setArrived(BigDecimal arrived) {
		this.arrived = arrived;
	}

	public String getAssignedParking() {
		return this.assignedParking;
	}

	public void setAssignedParking(String assignedParking) {
		this.assignedParking = assignedParking;
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

	public BigDecimal getDeparted() {
		return this.departed;
	}

	public void setDeparted(BigDecimal departed) {
		this.departed = departed;
	}

	public BigDecimal getIdx() {
		return this.idx;
	}

	public void setIdx(BigDecimal idx) {
		this.idx = idx;
	}

	public String getPauseType() {
		return this.pauseType;
	}

	public void setPauseType(String pauseType) {
		this.pauseType = pauseType;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getStatusCompanion() {
		return this.statusCompanion;
	}

	public void setStatusCompanion(String statusCompanion) {
		this.statusCompanion = statusCompanion;
	}

	public Date getTransportDate() {
		return this.transportDate;
	}

	public void setTransportDate(Date transportDate) {
		this.transportDate = transportDate;
	}

	public String getTransported() {
		return this.transported;
	}

	public void setTransported(String transported) {
		this.transported = transported;
	}

}