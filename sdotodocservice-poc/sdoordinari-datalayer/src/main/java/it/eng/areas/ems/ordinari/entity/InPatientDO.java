package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the IN_PATIENT database table.
 * 
 */
@Entity
@Table(name="IN_PATIENT")
public class InPatientDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Temporal(TemporalType.DATE)
	@Column(name="BIRTH_DATE")
	private Date birthDate;

	@Column(name="DEPARTMENT_ID")
	private String departmentId;

	@Temporal(TemporalType.DATE)
	@Column(name="END_STAY_IN_HOSPITAL")
	private Date endStayInHospital;

	private String name;

	@Column(name="PATIENT_ID")
	private String patientId;

	@Column(name="SANITARY_CODE")
	private String sanitaryCode;

	private String sex;

	@Temporal(TemporalType.DATE)
	@Column(name="START_STAY_IN_HOSPITAL")
	private Date startStayInHospital;

	private String surname;

	@Column(name="TAX_CODE")
	private String taxCode;

	public InPatientDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public Date getEndStayInHospital() {
		return this.endStayInHospital;
	}

	public void setEndStayInHospital(Date endStayInHospital) {
		this.endStayInHospital = endStayInHospital;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPatientId() {
		return this.patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
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

	public Date getStartStayInHospital() {
		return this.startStayInHospital;
	}

	public void setStartStayInHospital(Date startStayInHospital) {
		this.startStayInHospital = startStayInHospital;
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

}