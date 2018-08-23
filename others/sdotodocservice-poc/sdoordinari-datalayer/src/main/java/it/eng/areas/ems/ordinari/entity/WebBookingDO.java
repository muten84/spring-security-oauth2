package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the WEB_BOOKING database table.
 * 
 */
@Entity
@Table(name="WEB_BOOKING")
public class WebBookingDO implements Serializable {
	private static final long serialVersionUID = 1L;



//	@Id
//	@GeneratedValue(generator = "system-uuid")
//	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	//  @SequenceGenerator(name="seq",sequenceName="GUID")        
	// @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")      
	//	private String id2;

//	    @SequenceGenerator(name="seq",sequenceName="GUID")        
//	    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")      

//	@GeneratedValue(strategy = GenerationType.IDENTITY ) 
//	@Column(name = "ID",unique=true,columnDefinition="VARCHAR(28)")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private String id;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="DATE_FLAG")
	private BigDecimal dateFlag;

	@Column(name="DEST_DESCR")
	private String destDescr;

	@Column(name="DEST_DETAIL")
	private String destDetail;

	@Column(name="DEST_HOUSE_NUMBER")
	private String destHouseNumber;

	@Column(name="DEST_LOCALITY")
	private String destLocality;

	@Column(name="DEST_MUNICIPALITY")
	private String destMunicipality;

	@Column(name="DEST_PROVINCE")
	private String destProvince;

	@Column(name="DEST_STREET_NAME")
	private String destStreetName;

	@Column(name="DEST_TELEPHONE")
	private String destTelephone;

	@Column(name="DUPLICATED_ID")
	private String duplicatedId;

	private String name;

	
	private String note;

	@Column(name="OPTIONED_PARKING")
	private String optionedParking;

	@Column(name="PAT_STATUS")
	private String patStatus;

	@Column(name="PATIENT_CHARGEABLE")
	private BigDecimal patientChargeable;

	@Column(name="PATIENT_COMPANION")
	private String patientCompanion;

	@Column(name="REQ_DESCR")
	private String reqDescr;

	@Column(name="REQ_DETAIL")
	private String reqDetail;

	@Column(name="REQ_TELEPHONE")
	private String reqTelephone;

	@Column(name="SANITARY_CODE")
	private String sanitaryCode;

	@Column(name="START_DESCR")
	private String startDescr;

	@Column(name="START_DETAIL")
	private String startDetail;

	@Column(name="START_HOUSE_NUMBER")
	private String startHouseNumber;

	@Column(name="START_LOCALITY")
	private String startLocality;

	@Column(name="START_MUNICIPALITY")
	private String startMunicipality;

	@Column(name="START_PROVINCE")
	private String startProvince;

	@Column(name="START_STREET_NAME")
	private String startStreetName;

	@Column(name="START_TELEPHONE")
	private String startTelephone;

	private String status;

	@Column(name="STATUS_REASON")
	private String statusReason;

	/*@Column(name="STATUS_USER")
	private String statusUser;
*/
	private String surname;

	@Column(name="TAX_CODE")
	private String taxCode;

	@Temporal(TemporalType.DATE)
	@Column(name="TRANSPORT_DATE")
	private Date transportDate;

	@Column(name="TRANSPORT_TYPE")
	private String transportType;

	@Column(name="ID_EXTERNAL_REF")
	private String idExternalRef;

	@Column(name="CODE")
	private String code;

	@OneToMany(mappedBy="webBooking", fetch = FetchType.LAZY, cascade = { CascadeType.ALL} , orphanRemoval=true)
	public Set<WebBookingEquipmentDO> webBookingEquipments;


	public WebBookingDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getDestHouseNumber() {
		return this.destHouseNumber;
	}

	public void setDestHouseNumber(String destHouseNumber) {
		this.destHouseNumber = destHouseNumber;
	}

	public String getDestLocality() {
		return this.destLocality;
	}

	public void setDestLocality(String destLocality) {
		this.destLocality = destLocality;
	}

	public String getDestMunicipality() {
		return this.destMunicipality;
	}

	public void setDestMunicipality(String destMunicipality) {
		this.destMunicipality = destMunicipality;
	}

	public String getDestProvince() {
		return this.destProvince;
	}

	public void setDestProvince(String destProvince) {
		this.destProvince = destProvince;
	}

	public String getDestStreetName() {
		return this.destStreetName;
	}

	public void setDestStreetName(String destStreetName) {
		this.destStreetName = destStreetName;
	}

	public String getDestTelephone() {
		return this.destTelephone;
	}

	public void setDestTelephone(String destTelephone) {
		this.destTelephone = destTelephone;
	}

	public String getDuplicatedId() {
		return this.duplicatedId;
	}

	public void setDuplicatedId(String duplicatedId) {
		this.duplicatedId = duplicatedId;
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

	public String getPatStatus() {
		return this.patStatus;
	}

	public void setPatStatus(String patStatus) {
		this.patStatus = patStatus;
	}

	public BigDecimal getPatientChargeable() {
		return this.patientChargeable;
	}

	public void setPatientChargeable(BigDecimal patientChargeable) {
		this.patientChargeable = patientChargeable;
	}

	public String getPatientCompanion() {
		return this.patientCompanion;
	}

	public void setPatientCompanion(String patientCompanion) {
		this.patientCompanion = patientCompanion;
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

	public String getReqTelephone() {
		return this.reqTelephone;
	}

	public void setReqTelephone(String reqTelephone) {
		this.reqTelephone = reqTelephone;
	}

	public String getSanitaryCode() {
		return this.sanitaryCode;
	}

	public void setSanitaryCode(String sanitaryCode) {
		this.sanitaryCode = sanitaryCode;
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

	public String getStartHouseNumber() {
		return this.startHouseNumber;
	}

	public void setStartHouseNumber(String startHouseNumber) {
		this.startHouseNumber = startHouseNumber;
	}

	public String getStartLocality() {
		return this.startLocality;
	}

	public void setStartLocality(String startLocality) {
		this.startLocality = startLocality;
	}

	public String getStartMunicipality() {
		return this.startMunicipality;
	}

	public void setStartMunicipality(String startMunicipality) {
		this.startMunicipality = startMunicipality;
	}

	public String getStartProvince() {
		return this.startProvince;
	}

	public void setStartProvince(String startProvince) {
		this.startProvince = startProvince;
	}

	public String getStartStreetName() {
		return this.startStreetName;
	}

	public void setStartStreetName(String startStreetName) {
		this.startStreetName = startStreetName;
	}

	public String getStartTelephone() {
		return this.startTelephone;
	}

	public void setStartTelephone(String startTelephone) {
		this.startTelephone = startTelephone;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusReason() {
		return this.statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
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

	public String getIdExternalRef() {
		return idExternalRef;
	}

	public void setIdExternalRef(String idExternalRef) {
		this.idExternalRef = idExternalRef;
	}

	public Set<WebBookingEquipmentDO> getWebBookingEquipments() {
		return webBookingEquipments;
	}

	public void setWebBookingEquipments(
			Set<WebBookingEquipmentDO> webBookingEquipments) {
		this.webBookingEquipments = webBookingEquipments;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}