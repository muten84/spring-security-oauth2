package it.eng.areas.ems.ordinari.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class WebBooking implements Serializable {

	private String id;
	private Date creationDate;
	private BigDecimal dateFlag;
	private String destDescr;
	private String destDetail;
	private String destHouseNumber;
	private String destLocality;
	private String destMunicipality;
	private String destProvince;
	private String destStreetName;
	private String destTelephone;
	private String duplicatedId;
	private String name;
	private String note;
	private String optionedParking;
	private String patStatus;
	private BigDecimal patientChargeable;
	private String patientCompanion;
	private String reqDescr;
	private String reqDetail;
	private String reqTelephone;
	private String sanitaryCode;
	private String startDescr;
	private String startDetail;
	private String startHouseNumber;
	private String startLocality;
	private String startMunicipality;
	private String startProvince;
	private String startStreetName;
	private String startTelephone;
	private String status;
	private String statusReason;
	private String surname;
	private String taxCode;
	private Date transportDate;
	private String transportType;
	private String idExternalRef;
	private String code;
	

	public Set<WebBookingEquipment> webBookingEquipments;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public BigDecimal getDateFlag() {
		return dateFlag;
	}
	public void setDateFlag(BigDecimal dateFlag) {
		this.dateFlag = dateFlag;
	}
	public String getDestDescr() {
		return destDescr;
	}
	public void setDestDescr(String destDescr) {
		this.destDescr = destDescr;
	}
	public String getDestDetail() {
		return destDetail;
	}
	public void setDestDetail(String destDetail) {
		this.destDetail = destDetail;
	}
	public String getDestHouseNumber() {
		return destHouseNumber;
	}
	public void setDestHouseNumber(String destHouseNumber) {
		this.destHouseNumber = destHouseNumber;
	}
	public String getDestLocality() {
		return destLocality;
	}
	public void setDestLocality(String destLocality) {
		this.destLocality = destLocality;
	}
	public String getDestMunicipality() {
		return destMunicipality;
	}
	public void setDestMunicipality(String destMunicipality) {
		this.destMunicipality = destMunicipality;
	}
	public String getDestProvince() {
		return destProvince;
	}
	public void setDestProvince(String destProvince) {
		this.destProvince = destProvince;
	}
	public String getDestStreetName() {
		return destStreetName;
	}
	public void setDestStreetName(String destStreetName) {
		this.destStreetName = destStreetName;
	}
	public String getDestTelephone() {
		return destTelephone;
	}
	public void setDestTelephone(String destTelephone) {
		this.destTelephone = destTelephone;
	}
	public String getDuplicatedId() {
		return duplicatedId;
	}
	public void setDuplicatedId(String duplicatedId) {
		this.duplicatedId = duplicatedId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getOptionedParking() {
		return optionedParking;
	}
	public void setOptionedParking(String optionedParking) {
		this.optionedParking = optionedParking;
	}
	public String getPatStatus() {
		return patStatus;
	}
	public void setPatStatus(String patStatus) {
		this.patStatus = patStatus;
	}
	public BigDecimal getPatientChargeable() {
		return patientChargeable;
	}
	public void setPatientChargeable(BigDecimal patientChargeable) {
		this.patientChargeable = patientChargeable;
	}
	public String getPatientCompanion() {
		return patientCompanion;
	}
	public void setPatientCompanion(String patientCompanion) {
		this.patientCompanion = patientCompanion;
	}
	public String getReqDescr() {
		return reqDescr;
	}
	public void setReqDescr(String reqDescr) {
		this.reqDescr = reqDescr;
	}
	public String getReqDetail() {
		return reqDetail;
	}
	public void setReqDetail(String reqDetail) {
		this.reqDetail = reqDetail;
	}
	public String getReqTelephone() {
		return reqTelephone;
	}
	public void setReqTelephone(String reqTelephone) {
		this.reqTelephone = reqTelephone;
	}
	public String getSanitaryCode() {
		return sanitaryCode;
	}
	public void setSanitaryCode(String sanitaryCode) {
		this.sanitaryCode = sanitaryCode;
	}
	public String getStartDescr() {
		return startDescr;
	}
	public void setStartDescr(String startDescr) {
		this.startDescr = startDescr;
	}
	public String getStartDetail() {
		return startDetail;
	}
	public void setStartDetail(String startDetail) {
		this.startDetail = startDetail;
	}
	public String getStartHouseNumber() {
		return startHouseNumber;
	}
	public void setStartHouseNumber(String startHouseNumber) {
		this.startHouseNumber = startHouseNumber;
	}
	public String getStartLocality() {
		return startLocality;
	}
	public void setStartLocality(String startLocality) {
		this.startLocality = startLocality;
	}
	public String getStartMunicipality() {
		return startMunicipality;
	}
	public void setStartMunicipality(String startMunicipality) {
		this.startMunicipality = startMunicipality;
	}
	public String getStartProvince() {
		return startProvince;
	}
	public void setStartProvince(String startProvince) {
		this.startProvince = startProvince;
	}
	public String getStartStreetName() {
		return startStreetName;
	}
	public void setStartStreetName(String startStreetName) {
		this.startStreetName = startStreetName;
	}
	public String getStartTelephone() {
		return startTelephone;
	}
	public void setStartTelephone(String startTelephone) {
		this.startTelephone = startTelephone;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusReason() {
		return statusReason;
	}
	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}

	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	public Date getTransportDate() {
		return transportDate;
	}
	public void setTransportDate(Date transportDate) {
		this.transportDate = transportDate;
	}
	public String getTransportType() {
		return transportType;
	}
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}
	public Set<WebBookingEquipment> getWebBookingEquipments() {
		return webBookingEquipments;
	}
	public void setWebBookingEquipments(
			Set<WebBookingEquipment> webBookingEquipments) {
		this.webBookingEquipments = webBookingEquipments;
	}
	public String getIdExternalRef() {
		return idExternalRef;
	}
	public void setIdExternalRef(String idExternalRef) {
		this.idExternalRef = idExternalRef;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	
	
}