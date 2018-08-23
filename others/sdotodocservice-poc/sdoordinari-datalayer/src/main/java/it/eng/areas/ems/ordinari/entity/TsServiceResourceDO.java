package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TS_SERVICE_RESOURCES database table.
 * 
 */
@Entity
@Table(name="TS_SERVICE_RESOURCES")
public class TsServiceResourceDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Temporal(TemporalType.DATE)
	@Column(name="ACTIVATION_DATE")
	private Date activationDate;

	@Temporal(TemporalType.DATE)
	@Column(name="CLOSURE_DATE")
	private Date closureDate;

	@Column(name="CLOSURE_REASON")
	private String closureReason;

	@Column(name="KM_CLOSURE")
	private BigDecimal kmClosure;

	@Column(name="KM_START")
	private BigDecimal kmStart;

	@Column(name="PARKING_CODE")
	private String parkingCode;

	@Column(name="PARKING_ID")
	private String parkingId;

	@Column(name="RESOURCE_CODE")
	private String resourceCode;

	@Column(name="RESOURCE_ID")
	private String resourceId;

	@Temporal(TemporalType.DATE)
	@Column(name="RETURN_DATE")
	private Date returnDate;

	@Column(name="SERVICE_ID")
	private String serviceId;

	@Temporal(TemporalType.DATE)
	@Column(name="START_DATE")
	private Date startDate;

	private String status;

	private BigDecimal support;

	public TsServiceResourceDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getActivationDate() {
		return this.activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public Date getClosureDate() {
		return this.closureDate;
	}

	public void setClosureDate(Date closureDate) {
		this.closureDate = closureDate;
	}

	public String getClosureReason() {
		return this.closureReason;
	}

	public void setClosureReason(String closureReason) {
		this.closureReason = closureReason;
	}

	public BigDecimal getKmClosure() {
		return this.kmClosure;
	}

	public void setKmClosure(BigDecimal kmClosure) {
		this.kmClosure = kmClosure;
	}

	public BigDecimal getKmStart() {
		return this.kmStart;
	}

	public void setKmStart(BigDecimal kmStart) {
		this.kmStart = kmStart;
	}

	public String getParkingCode() {
		return this.parkingCode;
	}

	public void setParkingCode(String parkingCode) {
		this.parkingCode = parkingCode;
	}

	public String getParkingId() {
		return this.parkingId;
	}

	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}

	public String getResourceCode() {
		return this.resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Date getReturnDate() {
		return this.returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getSupport() {
		return this.support;
	}

	public void setSupport(BigDecimal support) {
		this.support = support;
	}

}