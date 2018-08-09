package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TS_SERVICE database table.
 * 
 */
@Entity
@Table(name="TS_SERVICE")
public class TsServiceDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Temporal(TemporalType.DATE)
	@Column(name="CLOSURE_DATE")
	private Date closureDate;

	@Column(name="CLOSURE_USER")
	private String closureUser;

	private String code;

	@Column(name="COMPANION_NAME")
	private String companionName;

	@Column(name="COMPANION_TYPE")
	private String companionType;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="CREATION_USER")
	private String creationUser;

	@Column(name="END_ADDRESS")
	private String endAddress;

	@Column(name="EXPECTED_CHARGE")
	private String expectedCharge;

	private BigDecimal km;

	@Temporal(TemporalType.DATE)
	@Column(name="MANAGING_DATE")
	private Date managingDate;

	@Column(name="MANAGING_USER")
	private String managingUser;

	private String note;

	private BigDecimal paid;

	private BigDecimal pause;

	@Column(name="PAUSE_MU")
	private String pauseMu;

	private String priority;

	@Column(name="SERVICE_NAME")
	private String serviceName;

	@Column(name="START_ADDRESS")
	private String startAddress;

	private String status;

	@Column(name="TOTAL_CHARGE")
	private String totalCharge;

	@Column(name="TOTAL_KM")
	private BigDecimal totalKm;

	@Temporal(TemporalType.DATE)
	@Column(name="TRANSPORT_DATE")
	private Date transportDate;

	public TsServiceDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getClosureDate() {
		return this.closureDate;
	}

	public void setClosureDate(Date closureDate) {
		this.closureDate = closureDate;
	}

	public String getClosureUser() {
		return this.closureUser;
	}

	public void setClosureUser(String closureUser) {
		this.closureUser = closureUser;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCompanionName() {
		return this.companionName;
	}

	public void setCompanionName(String companionName) {
		this.companionName = companionName;
	}

	public String getCompanionType() {
		return this.companionType;
	}

	public void setCompanionType(String companionType) {
		this.companionType = companionType;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationUser() {
		return this.creationUser;
	}

	public void setCreationUser(String creationUser) {
		this.creationUser = creationUser;
	}

	public String getEndAddress() {
		return this.endAddress;
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}

	public String getExpectedCharge() {
		return this.expectedCharge;
	}

	public void setExpectedCharge(String expectedCharge) {
		this.expectedCharge = expectedCharge;
	}

	public BigDecimal getKm() {
		return this.km;
	}

	public void setKm(BigDecimal km) {
		this.km = km;
	}

	public Date getManagingDate() {
		return this.managingDate;
	}

	public void setManagingDate(Date managingDate) {
		this.managingDate = managingDate;
	}

	public String getManagingUser() {
		return this.managingUser;
	}

	public void setManagingUser(String managingUser) {
		this.managingUser = managingUser;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public BigDecimal getPaid() {
		return this.paid;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	public BigDecimal getPause() {
		return this.pause;
	}

	public void setPause(BigDecimal pause) {
		this.pause = pause;
	}

	public String getPauseMu() {
		return this.pauseMu;
	}

	public void setPauseMu(String pauseMu) {
		this.pauseMu = pauseMu;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getStartAddress() {
		return this.startAddress;
	}

	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotalCharge() {
		return this.totalCharge;
	}

	public void setTotalCharge(String totalCharge) {
		this.totalCharge = totalCharge;
	}

	public BigDecimal getTotalKm() {
		return this.totalKm;
	}

	public void setTotalKm(BigDecimal totalKm) {
		this.totalKm = totalKm;
	}

	public Date getTransportDate() {
		return this.transportDate;
	}

	public void setTransportDate(Date transportDate) {
		this.transportDate = transportDate;
	}

}