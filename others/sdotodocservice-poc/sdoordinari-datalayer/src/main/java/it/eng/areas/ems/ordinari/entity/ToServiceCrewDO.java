package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TO_SERVICE_CREW database table.
 * 
 */
@Entity
@Table(name="TO_SERVICE_CREW")
public class ToServiceCrewDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="CREW_ID")
	private String crewId;

	@Column(name="CREW_MEMBER")
	private String crewMember;

	@Column(name="CREW_TASK")
	private String crewTask;

	@Column(name="SERVICE_ID")
	private String serviceId;

	public ToServiceCrewDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCrewId() {
		return this.crewId;
	}

	public void setCrewId(String crewId) {
		this.crewId = crewId;
	}

	public String getCrewMember() {
		return this.crewMember;
	}

	public void setCrewMember(String crewMember) {
		this.crewMember = crewMember;
	}

	public String getCrewTask() {
		return this.crewTask;
	}

	public void setCrewTask(String crewTask) {
		this.crewTask = crewTask;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

}