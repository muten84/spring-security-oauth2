package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TURN_CREW_MEMBER database table.
 * 
 */
@Entity
@Table(name="TURN_CREW_MEMBER")
public class TurnCrewMemberDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="CREW_MEMBER_ID")
	private String crewMemberId;

	private String name;

	private String note;

	@Column(name="PARKING_VEHICLE_TURN_ID")
	private String parkingVehicleTurnId;

	private String surname;

	private String task;

	public TurnCrewMemberDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCrewMemberId() {
		return this.crewMemberId;
	}

	public void setCrewMemberId(String crewMemberId) {
		this.crewMemberId = crewMemberId;
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

	public String getParkingVehicleTurnId() {
		return this.parkingVehicleTurnId;
	}

	public void setParkingVehicleTurnId(String parkingVehicleTurnId) {
		this.parkingVehicleTurnId = parkingVehicleTurnId;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getTask() {
		return this.task;
	}

	public void setTask(String task) {
		this.task = task;
	}

}