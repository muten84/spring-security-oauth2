package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the TS_RESOURCE_PARKING database table.
 * 
 */
@Entity
@Table(name="TS_RESOURCE_PARKING")
public class TsResourceParkingDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	

	@Column(name="NEXT_PARKING_CODE")
	private String nextParkingCode;

	@Column(name="NEXT_PARKING_ID")
	private String nextParkingId;

	@Column(name="RESOURCE_ID")
	private String resourceId;

	@Temporal(TemporalType.DATE)
	@Column(name="STOP_LOCATION_DATE")
	private Date stopLocationDate;

	public TsResourceParkingDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNextParkingCode() {
		return this.nextParkingCode;
	}

	public void setNextParkingCode(String nextParkingCode) {
		this.nextParkingCode = nextParkingCode;
	}

	public String getNextParkingId() {
		return this.nextParkingId;
	}

	public void setNextParkingId(String nextParkingId) {
		this.nextParkingId = nextParkingId;
	}

	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Date getStopLocationDate() {
		return this.stopLocationDate;
	}

	public void setStopLocationDate(Date stopLocationDate) {
		this.stopLocationDate = stopLocationDate;
	}

}