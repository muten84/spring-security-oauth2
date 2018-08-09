package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the AVAILABLE_MOBILE_STATION database table.
 * 
 */
@Entity
@Table(name="AVAILABLE_MOBILE_STATION")
public class AvailableMobileStationDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="CREW_MEMBER_ID")
	private String crewMemberId;

	@Column(name="SERVICE_NAME")
	private String serviceName;

	@Column(name="SERVICE_PATH")
	private String servicePath;

	@Column(name="SESSION_ID")
	private String sessionId;

	@Column(name="VEHICLE_ID")
	private String vehicleId;

	public AvailableMobileStationDO() {
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

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServicePath() {
		return this.servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getVehicleId() {
		return this.vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

}