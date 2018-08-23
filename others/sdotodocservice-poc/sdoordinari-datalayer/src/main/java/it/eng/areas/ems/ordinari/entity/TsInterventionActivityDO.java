package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TS_INTERVENTION_ACTIVITY database table.
 * 
 */
@Entity
@Table(name="TS_INTERVENTION_ACTIVITY")
public class TsInterventionActivityDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="INTERVENTION_ID")
	private String interventionId;

	@Column(name="KM_ARRIVAL")
	private BigDecimal kmArrival;

	@Column(name="KM_DEPARTURE")
	private BigDecimal kmDeparture;

	private BigDecimal pause;

	@Column(name="PAUSE_MU")
	private String pauseMu;

	@Column(name="RESOURCE_CODE")
	private String resourceCode;

	@Column(name="RESOURCE_ID")
	private String resourceId;

	@Temporal(TemporalType.DATE)
	@Column(name="TIMESTAMP_ARRIVAL")
	private Date timestampArrival;

	@Temporal(TemporalType.DATE)
	@Column(name="TIMESTAMP_DEPARTURE")
	private Date timestampDeparture;

	public TsInterventionActivityDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInterventionId() {
		return this.interventionId;
	}

	public void setInterventionId(String interventionId) {
		this.interventionId = interventionId;
	}

	public BigDecimal getKmArrival() {
		return this.kmArrival;
	}

	public void setKmArrival(BigDecimal kmArrival) {
		this.kmArrival = kmArrival;
	}

	public BigDecimal getKmDeparture() {
		return this.kmDeparture;
	}

	public void setKmDeparture(BigDecimal kmDeparture) {
		this.kmDeparture = kmDeparture;
	}

	public BigDecimal getPause() {
		return this.pause;
	}

	public void setPause(BigDecimal pause) {
		this.pause = pause;
	}

	public String getPauseMu() {
		return this.pauseMu;
	}

	public void setPauseMu(String pauseMu) {
		this.pauseMu = pauseMu;
	}

	public String getResourceCode() {
		return this.resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Date getTimestampArrival() {
		return this.timestampArrival;
	}

	public void setTimestampArrival(Date timestampArrival) {
		this.timestampArrival = timestampArrival;
	}

	public Date getTimestampDeparture() {
		return this.timestampDeparture;
	}

	public void setTimestampDeparture(Date timestampDeparture) {
		this.timestampDeparture = timestampDeparture;
	}

}