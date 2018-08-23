package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TS_CICLICAL_DAYS_INFO database table.
 * 
 */
@Entity
@Table(name="TS_CICLICAL_DAYS_INFO")
public class TsCiclicalDaysInfoDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@Column(name="CICLICAL_ID")
	private String ciclicalId;

	@Column(name="DATE_FLAG")
	private BigDecimal dateFlag;

	@Temporal(TemporalType.DATE)
	@Column(name="DAY_DATE")
	private Date dayDate;

	@Column(name="DAY_NAME")
	private String dayName;

	@Column(name="END_AUTHORITY_ID")
	private String endAuthorityId;

	@Column(name="END_CC")
	private String endCc;

	@Column(name="END_COMPOUND_ADDRESS")
	private String endCompoundAddress;

	@Column(name="END_CONVENTION")
	private String endConvention;

	@Column(name="END_DEPARTMENT_ID")
	private String endDepartmentId;

	@Column(name="END_DESCRIPTION")
	private String endDescription;

	@Column(name="END_DETAIL")
	private String endDetail;

	@Column(name="END_HOUSE_NUMBER")
	private String endHouseNumber;

	@Column(name="END_LOCALITY")
	private String endLocality;

	@Column(name="END_MUNICIPALITY")
	private String endMunicipality;

	@Column(name="END_PAVILION_NAME")
	private String endPavilionName;

	@Column(name="END_PAVILION_NUMBER")
	private String endPavilionNumber;

	@Column(name="END_PROVINCE")
	private String endProvince;

	@Column(name="END_REFERENCE")
	private String endReference;

	@Column(name="END_STREET_NAME")
	private String endStreetName;

	@Column(name="END_TYPE")
	private String endType;

	@Column(name="END_VAT")
	private String endVat;

	
	private String note;

	private String status;

	public TsCiclicalDaysInfoDO() {
	}

	public String getCiclicalId() {
		return this.ciclicalId;
	}

	public void setCiclicalId(String ciclicalId) {
		this.ciclicalId = ciclicalId;
	}

	public BigDecimal getDateFlag() {
		return this.dateFlag;
	}

	public void setDateFlag(BigDecimal dateFlag) {
		this.dateFlag = dateFlag;
	}

	public Date getDayDate() {
		return this.dayDate;
	}

	public void setDayDate(Date dayDate) {
		this.dayDate = dayDate;
	}

	public String getDayName() {
		return this.dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

	public String getEndAuthorityId() {
		return this.endAuthorityId;
	}

	public void setEndAuthorityId(String endAuthorityId) {
		this.endAuthorityId = endAuthorityId;
	}

	public String getEndCc() {
		return this.endCc;
	}

	public void setEndCc(String endCc) {
		this.endCc = endCc;
	}

	public String getEndCompoundAddress() {
		return this.endCompoundAddress;
	}

	public void setEndCompoundAddress(String endCompoundAddress) {
		this.endCompoundAddress = endCompoundAddress;
	}

	public String getEndConvention() {
		return this.endConvention;
	}

	public void setEndConvention(String endConvention) {
		this.endConvention = endConvention;
	}

	public String getEndDepartmentId() {
		return this.endDepartmentId;
	}

	public void setEndDepartmentId(String endDepartmentId) {
		this.endDepartmentId = endDepartmentId;
	}

	public String getEndDescription() {
		return this.endDescription;
	}

	public void setEndDescription(String endDescription) {
		this.endDescription = endDescription;
	}

	public String getEndDetail() {
		return this.endDetail;
	}

	public void setEndDetail(String endDetail) {
		this.endDetail = endDetail;
	}

	public String getEndHouseNumber() {
		return this.endHouseNumber;
	}

	public void setEndHouseNumber(String endHouseNumber) {
		this.endHouseNumber = endHouseNumber;
	}

	public String getEndLocality() {
		return this.endLocality;
	}

	public void setEndLocality(String endLocality) {
		this.endLocality = endLocality;
	}

	public String getEndMunicipality() {
		return this.endMunicipality;
	}

	public void setEndMunicipality(String endMunicipality) {
		this.endMunicipality = endMunicipality;
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

	public String getEndProvince() {
		return this.endProvince;
	}

	public void setEndProvince(String endProvince) {
		this.endProvince = endProvince;
	}

	public String getEndReference() {
		return this.endReference;
	}

	public void setEndReference(String endReference) {
		this.endReference = endReference;
	}

	public String getEndStreetName() {
		return this.endStreetName;
	}

	public void setEndStreetName(String endStreetName) {
		this.endStreetName = endStreetName;
	}

	public String getEndType() {
		return this.endType;
	}

	public void setEndType(String endType) {
		this.endType = endType;
	}

	public String getEndVat() {
		return this.endVat;
	}

	public void setEndVat(String endVat) {
		this.endVat = endVat;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}