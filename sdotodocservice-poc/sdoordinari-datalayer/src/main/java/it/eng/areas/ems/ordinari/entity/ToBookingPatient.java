package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TO_BOOKING_PATIENT database table.
 * 
 */
@Entity
@Table(name="TO_BOOKING_PATIENT")
@NamedQuery(name="ToBookingPatient.findAll", query="SELECT t FROM ToBookingPatient t")
public class ToBookingPatient implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String accessibility;

	@Column(name="ADDITIONAL_PATIENT_ID")
	private String additionalPatientId;

	@Temporal(TemporalType.DATE)
	@Column(name="BIRTH_DATE")
	private Date birthDate;

	@Column(name="BOOKING_ID")
	private String bookingId;

	@Column(name="FIRST_TELEPHONE")
	private String firstTelephone;

	@Column(name="HOUSE_NUMBER")
	private String houseNumber;

	private String locality;

	private String municipality;

	private String name;

	private String province;

	@Column(name="SANITARY_CODE")
	private String sanitaryCode;

	@Column(name="SECOND_TELEPHONE")
	private String secondTelephone;

	private String sex;

	@Column(name="STREET_NAME")
	private String streetName;

	private String surname;

	@Column(name="TAX_CODE")
	private String taxCode;

	private BigDecimal ticket;

	@Column(name="\"TYPE\"")
	private String type;

	public ToBookingPatient() {
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

	public String getAdditionalPatientId() {
		return this.additionalPatientId;
	}

	public void setAdditionalPatientId(String additionalPatientId) {
		this.additionalPatientId = additionalPatientId;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public String getFirstTelephone() {
		return this.firstTelephone;
	}

	public void setFirstTelephone(String firstTelephone) {
		this.firstTelephone = firstTelephone;
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

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getSanitaryCode() {
		return this.sanitaryCode;
	}

	public void setSanitaryCode(String sanitaryCode) {
		this.sanitaryCode = sanitaryCode;
	}

	public String getSecondTelephone() {
		return this.secondTelephone;
	}

	public void setSecondTelephone(String secondTelephone) {
		this.secondTelephone = secondTelephone;
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

	public BigDecimal getTicket() {
		return this.ticket;
	}

	public void setTicket(BigDecimal ticket) {
		this.ticket = ticket;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}