package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TO_PARKING_AUTHORITY database table.
 * 
 */
@Entity
@Table(name="TO_PARKING_AUTHORITY")
public class ToParkingAuthorityDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="HOSPITAL_ID")
	private String hospitalId;

	@Column(name="PARKING_ID")
	private String parkingId;

	private BigDecimal priority;

	public ToParkingAuthorityDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHospitalId() {
		return this.hospitalId;
	}

	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}

	public String getParkingId() {
		return this.parkingId;
	}

	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}

	public BigDecimal getPriority() {
		return this.priority;
	}

	public void setPriority(BigDecimal priority) {
		this.priority = priority;
	}

}