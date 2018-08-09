package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TS_PATIENT_ARCHIVE database table.
 * 
 */
@Entity
@Table(name="TS_PATIENT_ARCHIVE")
public class TsPatientArchiveDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	private String accessibility;

	private BigDecimal age;

	private String asl;

	@Temporal(TemporalType.DATE)
	@Column(name="BIRTH_DATE")
	private Date birthDate;

	@Column(name="BIRTH_PLACE")
	private String birthPlace;

	@Column(name="BIRTH_PROVINCE")
	private String birthProvince;

	@Column(name="DOCTOR_TEL")
	private String doctorTel;

	@Column(name="FAMILY_DOCTOR")
	private String familyDoctor;

	private String guardian;

	@Column(name="GUARDIAN_TEL")
	private String guardianTel;

	@Column(name="HOUSE_NUMBER")
	private String houseNumber;

	private String locality;

	private String municipality;

	private String name;

	private String note;

	private String province;

	private String region;

	@Column(name="SANITARY_CODE")
	private String sanitaryCode;

	private String sex;

	@Column(name="STREET_NAME")
	private String streetName;

	private String surname;

	@Column(name="TAX_CODE")
	private String taxCode;

	private String telephone;

	private BigDecimal ticket;

	public TsPatientArchiveDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccessibility() {
		return this.accessibility;
	}

	public void setAccessibility(String accessibility) {
		this.accessibility = accessibility;
	}

	public BigDecimal getAge() {
		return this.age;
	}

	public void setAge(BigDecimal age) {
		this.age = age;
	}

	public String getAsl() {
		return this.asl;
	}

	public void setAsl(String asl) {
		this.asl = asl;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthPlace() {
		return this.birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getBirthProvince() {
		return this.birthProvince;
	}

	public void setBirthProvince(String birthProvince) {
		this.birthProvince = birthProvince;
	}

	public String getDoctorTel() {
		return this.doctorTel;
	}

	public void setDoctorTel(String doctorTel) {
		this.doctorTel = doctorTel;
	}

	public String getFamilyDoctor() {
		return this.familyDoctor;
	}

	public void setFamilyDoctor(String familyDoctor) {
		this.familyDoctor = familyDoctor;
	}

	public String getGuardian() {
		return this.guardian;
	}

	public void setGuardian(String guardian) {
		this.guardian = guardian;
	}

	public String getGuardianTel() {
		return this.guardianTel;
	}

	public void setGuardianTel(String guardianTel) {
		this.guardianTel = guardianTel;
	}

	public String getHouseNumber() {
		return this.houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getMunicipality() {
		return this.municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
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

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
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

	public String getStreetName() {
		return this.streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
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

}