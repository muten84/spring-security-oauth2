package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TS_CICLICAL_BOOKING database table.
 * 
 */
@Entity
@Table(name="TS_CICLICAL_BOOKING")
public class TsCiclicalBookingDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Temporal(TemporalType.DATE)
	@Column(name="ARRIVE_DATE")
	private Date arriveDate;

	@Column(name="ASSIGNED_PARKING")
	private String assignedParking;

	@Column(name="ASSIGNED_PARKING_ID")
	private String assignedParkingId;

	@Column(name="AUTHORITY_ID1")
	private String authorityId1;

	@Column(name="AUTHORITY_ID2")
	private String authorityId2;

	@Temporal(TemporalType.DATE)
	@Column(name="BIRTH_DATE")
	private Date birthDate;

	private BigDecimal blood;

	@Column(name="BOOKING_ID")
	private String bookingId;

	private String category;

	private String charge;

	@Column(name="CICLICAL_ID")
	private String ciclicalId;

	private String code;

	@Column(name="COMPACT_ADDRESS")
	private String compactAddress;

	@Column(name="COMPACT_ADDRESS2")
	private String compactAddress2;

	private String companion;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="DATE_FLAG")
	private BigDecimal dateFlag;

	@Column(name="DELETE_REASON")
	private String deleteReason;

	@Temporal(TemporalType.DATE)
	@Column(name="DELETE_SYS_DATE")
	private Date deleteSysDate;

	@Column(name="DELETE_USR_NAME")
	private String deleteUsrName;

	@Column(name="DEPARTMENT_ID1")
	private String departmentId1;

	@Column(name="DEPARTMENT_ID2")
	private String departmentId2;

	@Column(name="DEST_CC")
	private String destCc;

	@Column(name="DEST_CONVENTION")
	private String destConvention;

	@Column(name="DEST_DESCR")
	private String destDescr;

	@Column(name="DEST_DETAIL")
	private String destDetail;

	@Column(name="DEST_REFERENCE")
	private String destReference;

	@Column(name="DEST_TELEPHONE")
	private String destTelephone;

	@Column(name="DEST_TYPE")
	private String destType;

	@Column(name="DEST_VAT")
	private String destVat;

	@Column(name="DOCTOR_COST_CENTER")
	private String doctorCostCenter;

	@Column(name="DOCTOR_ID")
	private String doctorId;

	@Column(name="DOCTOR_NAME")
	private String doctorName;

	@Column(name="END_PAVILION_NAME")
	private String endPavilionName;

	@Column(name="END_PAVILION_NUMBER")
	private String endPavilionNumber;

	@Column(name="HISTORY_FLAG")
	private BigDecimal historyFlag;

	@Column(name="HOUSE_NUMBER")
	private String houseNumber;

	@Column(name="HOUSE_NUMBER2")
	private String houseNumber2;

	private String locality;

	private String locality2;

	@Column(name="MEDICAL_AUTHORIZATION")
	private BigDecimal medicalAuthorization;

	@Column(name="MEDICAL_REQUEST")
	private BigDecimal medicalRequest;

	@Column(name="MODIFIED_BY")
	private String modifiedBy;

	private String municipality;

	private String municipality2;

	private String name;

	private String note;

	@Column(name="OPTIONED_PARKING")
	private String optionedParking;

	@Column(name="OPTIONED_PARKING_ID")
	private String optionedParkingId;

	@Column(name="ORGANS_FLAG")
	private BigDecimal organsFlag;

	@Column(name="PAT_STATUS")
	private String patStatus;

	private String priority;

	@Column(name="PROTECT_RESOURCE_REQUEST")
	private BigDecimal protectResourceRequest;

	private String province;

	private String province2;

	@Column(name="REQ_CC")
	private String reqCc;

	@Column(name="REQ_CONVENTION")
	private String reqConvention;

	@Column(name="REQ_DESCR")
	private String reqDescr;

	@Column(name="REQ_DETAIL")
	private String reqDetail;

	@Column(name="REQ_REF")
	private String reqRef;

	@Column(name="REQ_TELEPHONE")
	private String reqTelephone;

	@Column(name="REQ_TYPE")
	private String reqType;

	@Column(name="REQ_VAT")
	private String reqVat;

	@Temporal(TemporalType.DATE)
	@Column(name="RESCUE_DATE")
	private Date rescueDate;

	@Column(name="RESOURCE_ID")
	private String resourceId;

	@Column(name="RESOURCE_TYPE")
	private String resourceType;

	@Temporal(TemporalType.DATE)
	@Column(name="RETURN_DATE")
	private Date returnDate;

	@Column(name="RETURN_READY")
	private BigDecimal returnReady;

	@Column(name="SANITARY_CODE")
	private String sanitaryCode;

	private String sex;

	@Column(name="SPECIAL_EQUIPMENT_FLAG")
	private BigDecimal specialEquipmentFlag;

	@Column(name="START_AUTHORITY_ID")
	private String startAuthorityId;

	@Column(name="START_CC")
	private String startCc;

	@Column(name="START_CONVENTION")
	private String startConvention;

	@Column(name="START_DEPARTMENT_ID")
	private String startDepartmentId;

	@Column(name="START_DESCR")
	private String startDescr;

	@Column(name="START_DETAIL")
	private String startDetail;

	@Column(name="START_PAVILION_NAME")
	private String startPavilionName;

	@Column(name="START_PAVILION_NUMBER")
	private String startPavilionNumber;

	@Column(name="START_REFERENCE")
	private String startReference;

	@Column(name="START_TELEPHONE")
	private String startTelephone;

	@Column(name="START_TYPE")
	private String startType;

	@Column(name="START_VAT")
	private String startVat;

	private String status;

	@Column(name="STREET_NAME")
	private String streetName;

	@Column(name="STREET_NAME2")
	private String streetName2;

	private String surname;

	@Column(name="TAX_CODE")
	private String taxCode;

	private String telephone;

	private BigDecimal ticket;

	@Temporal(TemporalType.DATE)
	@Column(name="TRANSPORT_DATE")
	private Date transportDate;

	@Column(name="TRANSPORT_TYPE")
	private String transportType;

	@Column(name="VARIOUS_DETAIL")
	private String variousDetail;

	@Column(name="WEB_BOOKING_ID")
	private String webBookingId;

	public TsCiclicalBookingDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getArriveDate() {
		return this.arriveDate;
	}

	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}

	public String getAssignedParking() {
		return this.assignedParking;
	}

	public void setAssignedParking(String assignedParking) {
		this.assignedParking = assignedParking;
	}

	public String getAssignedParkingId() {
		return this.assignedParkingId;
	}

	public void setAssignedParkingId(String assignedParkingId) {
		this.assignedParkingId = assignedParkingId;
	}

	public String getAuthorityId1() {
		return this.authorityId1;
	}

	public void setAuthorityId1(String authorityId1) {
		this.authorityId1 = authorityId1;
	}

	public String getAuthorityId2() {
		return this.authorityId2;
	}

	public void setAuthorityId2(String authorityId2) {
		this.authorityId2 = authorityId2;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public BigDecimal getBlood() {
		return this.blood;
	}

	public void setBlood(BigDecimal blood) {
		this.blood = blood;
	}

	public String getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCharge() {
		return this.charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getCiclicalId() {
		return this.ciclicalId;
	}

	public void setCiclicalId(String ciclicalId) {
		this.ciclicalId = ciclicalId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCompactAddress() {
		return this.compactAddress;
	}

	public void setCompactAddress(String compactAddress) {
		this.compactAddress = compactAddress;
	}

	public String getCompactAddress2() {
		return this.compactAddress2;
	}

	public void setCompactAddress2(String compactAddress2) {
		this.compactAddress2 = compactAddress2;
	}

	public String getCompanion() {
		return this.companion;
	}

	public void setCompanion(String companion) {
		this.companion = companion;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public BigDecimal getDateFlag() {
		return this.dateFlag;
	}

	public void setDateFlag(BigDecimal dateFlag) {
		this.dateFlag = dateFlag;
	}

	public String getDeleteReason() {
		return this.deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

	public Date getDeleteSysDate() {
		return this.deleteSysDate;
	}

	public void setDeleteSysDate(Date deleteSysDate) {
		this.deleteSysDate = deleteSysDate;
	}

	public String getDeleteUsrName() {
		return this.deleteUsrName;
	}

	public void setDeleteUsrName(String deleteUsrName) {
		this.deleteUsrName = deleteUsrName;
	}

	public String getDepartmentId1() {
		return this.departmentId1;
	}

	public void setDepartmentId1(String departmentId1) {
		this.departmentId1 = departmentId1;
	}

	public String getDepartmentId2() {
		return this.departmentId2;
	}

	public void setDepartmentId2(String departmentId2) {
		this.departmentId2 = departmentId2;
	}

	public String getDestCc() {
		return this.destCc;
	}

	public void setDestCc(String destCc) {
		this.destCc = destCc;
	}

	public String getDestConvention() {
		return this.destConvention;
	}

	public void setDestConvention(String destConvention) {
		this.destConvention = destConvention;
	}

	public String getDestDescr() {
		return this.destDescr;
	}

	public void setDestDescr(String destDescr) {
		this.destDescr = destDescr;
	}

	public String getDestDetail() {
		return this.destDetail;
	}

	public void setDestDetail(String destDetail) {
		this.destDetail = destDetail;
	}

	public String getDestReference() {
		return this.destReference;
	}

	public void setDestReference(String destReference) {
		this.destReference = destReference;
	}

	public String getDestTelephone() {
		return this.destTelephone;
	}

	public void setDestTelephone(String destTelephone) {
		this.destTelephone = destTelephone;
	}

	public String getDestType() {
		return this.destType;
	}

	public void setDestType(String destType) {
		this.destType = destType;
	}

	public String getDestVat() {
		return this.destVat;
	}

	public void setDestVat(String destVat) {
		this.destVat = destVat;
	}

	public String getDoctorCostCenter() {
		return this.doctorCostCenter;
	}

	public void setDoctorCostCenter(String doctorCostCenter) {
		this.doctorCostCenter = doctorCostCenter;
	}

	public String getDoctorId() {
		return this.doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return this.doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getEndPavilionName() {
		return this.endPavilionName;
	}

	public void setEndPavilionName(String endPavilionName) {
		this.endPavilionName = endPavilionName;
	}

	public String getEndPavilionNumber() {
		return this.endPavilionNumber;
	}

	public void setEndPavilionNumber(String endPavilionNumber) {
		this.endPavilionNumber = endPavilionNumber;
	}

	public BigDecimal getHistoryFlag() {
		return this.historyFlag;
	}

	public void setHistoryFlag(BigDecimal historyFlag) {
		this.historyFlag = historyFlag;
	}

	public String getHouseNumber() {
		return this.houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getHouseNumber2() {
		return this.houseNumber2;
	}

	public void setHouseNumber2(String houseNumber2) {
		this.houseNumber2 = houseNumber2;
	}

	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getLocality2() {
		return this.locality2;
	}

	public void setLocality2(String locality2) {
		this.locality2 = locality2;
	}

	public BigDecimal getMedicalAuthorization() {
		return this.medicalAuthorization;
	}

	public void setMedicalAuthorization(BigDecimal medicalAuthorization) {
		this.medicalAuthorization = medicalAuthorization;
	}

	public BigDecimal getMedicalRequest() {
		return this.medicalRequest;
	}

	public void setMedicalRequest(BigDecimal medicalRequest) {
		this.medicalRequest = medicalRequest;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getMunicipality() {
		return this.municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	public String getMunicipality2() {
		return this.municipality2;
	}

	public void setMunicipality2(String municipality2) {
		this.municipality2 = municipality2;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOptionedParking() {
		return this.optionedParking;
	}

	public void setOptionedParking(String optionedParking) {
		this.optionedParking = optionedParking;
	}

	public String getOptionedParkingId() {
		return this.optionedParkingId;
	}

	public void setOptionedParkingId(String optionedParkingId) {
		this.optionedParkingId = optionedParkingId;
	}

	public BigDecimal getOrgansFlag() {
		return this.organsFlag;
	}

	public void setOrgansFlag(BigDecimal organsFlag) {
		this.organsFlag = organsFlag;
	}

	public String getPatStatus() {
		return this.patStatus;
	}

	public void setPatStatus(String patStatus) {
		this.patStatus = patStatus;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public BigDecimal getProtectResourceRequest() {
		return this.protectResourceRequest;
	}

	public void setProtectResourceRequest(BigDecimal protectResourceRequest) {
		this.protectResourceRequest = protectResourceRequest;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince2() {
		return this.province2;
	}

	public void setProvince2(String province2) {
		this.province2 = province2;
	}

	public String getReqCc() {
		return this.reqCc;
	}

	public void setReqCc(String reqCc) {
		this.reqCc = reqCc;
	}

	public String getReqConvention() {
		return this.reqConvention;
	}

	public void setReqConvention(String reqConvention) {
		this.reqConvention = reqConvention;
	}

	public String getReqDescr() {
		return this.reqDescr;
	}

	public void setReqDescr(String reqDescr) {
		this.reqDescr = reqDescr;
	}

	public String getReqDetail() {
		return this.reqDetail;
	}

	public void setReqDetail(String reqDetail) {
		this.reqDetail = reqDetail;
	}

	public String getReqRef() {
		return this.reqRef;
	}

	public void setReqRef(String reqRef) {
		this.reqRef = reqRef;
	}

	public String getReqTelephone() {
		return this.reqTelephone;
	}

	public void setReqTelephone(String reqTelephone) {
		this.reqTelephone = reqTelephone;
	}

	public String getReqType() {
		return this.reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getReqVat() {
		return this.reqVat;
	}

	public void setReqVat(String reqVat) {
		this.reqVat = reqVat;
	}

	public Date getRescueDate() {
		return this.rescueDate;
	}

	public void setRescueDate(Date rescueDate) {
		this.rescueDate = rescueDate;
	}

	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Date getReturnDate() {
		return this.returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public BigDecimal getReturnReady() {
		return this.returnReady;
	}

	public void setReturnReady(BigDecimal returnReady) {
		this.returnReady = returnReady;
	}

	public String getSanitaryCode() {
		return this.sanitaryCode;
	}

	public void setSanitaryCode(String sanitaryCode) {
		this.sanitaryCode = sanitaryCode;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public BigDecimal getSpecialEquipmentFlag() {
		return this.specialEquipmentFlag;
	}

	public void setSpecialEquipmentFlag(BigDecimal specialEquipmentFlag) {
		this.specialEquipmentFlag = specialEquipmentFlag;
	}

	public String getStartAuthorityId() {
		return this.startAuthorityId;
	}

	public void setStartAuthorityId(String startAuthorityId) {
		this.startAuthorityId = startAuthorityId;
	}

	public String getStartCc() {
		return this.startCc;
	}

	public void setStartCc(String startCc) {
		this.startCc = startCc;
	}

	public String getStartConvention() {
		return this.startConvention;
	}

	public void setStartConvention(String startConvention) {
		this.startConvention = startConvention;
	}

	public String getStartDepartmentId() {
		return this.startDepartmentId;
	}

	public void setStartDepartmentId(String startDepartmentId) {
		this.startDepartmentId = startDepartmentId;
	}

	public String getStartDescr() {
		return this.startDescr;
	}

	public void setStartDescr(String startDescr) {
		this.startDescr = startDescr;
	}

	public String getStartDetail() {
		return this.startDetail;
	}

	public void setStartDetail(String startDetail) {
		this.startDetail = startDetail;
	}

	public String getStartPavilionName() {
		return this.startPavilionName;
	}

	public void setStartPavilionName(String startPavilionName) {
		this.startPavilionName = startPavilionName;
	}

	public String getStartPavilionNumber() {
		return this.startPavilionNumber;
	}

	public void setStartPavilionNumber(String startPavilionNumber) {
		this.startPavilionNumber = startPavilionNumber;
	}

	public String getStartReference() {
		return this.startReference;
	}

	public void setStartReference(String startReference) {
		this.startReference = startReference;
	}

	public String getStartTelephone() {
		return this.startTelephone;
	}

	public void setStartTelephone(String startTelephone) {
		this.startTelephone = startTelephone;
	}

	public String getStartType() {
		return this.startType;
	}

	public void setStartType(String startType) {
		this.startType = startType;
	}

	public String getStartVat() {
		return this.startVat;
	}

	public void setStartVat(String startVat) {
		this.startVat = startVat;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStreetName() {
		return this.streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetName2() {
		return this.streetName2;
	}

	public void setStreetName2(String streetName2) {
		this.streetName2 = streetName2;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getTaxCode() {
		return this.taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public BigDecimal getTicket() {
		return this.ticket;
	}

	public void setTicket(BigDecimal ticket) {
		this.ticket = ticket;
	}

	public Date getTransportDate() {
		return this.transportDate;
	}

	public void setTransportDate(Date transportDate) {
		this.transportDate = transportDate;
	}

	public String getTransportType() {
		return this.transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	public String getVariousDetail() {
		return this.variousDetail;
	}

	public void setVariousDetail(String variousDetail) {
		this.variousDetail = variousDetail;
	}

	public String getWebBookingId() {
		return this.webBookingId;
	}

	public void setWebBookingId(String webBookingId) {
		this.webBookingId = webBookingId;
	}

}