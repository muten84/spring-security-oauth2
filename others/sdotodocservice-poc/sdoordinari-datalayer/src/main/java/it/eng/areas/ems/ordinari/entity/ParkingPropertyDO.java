package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the PARKING_PROPERTIES database table.
 * 
 */
@Entity
@Table(name="PARKING_PROPERTIES")
public class ParkingPropertyDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="CORPSE_QUALIFIED")
	private BigDecimal corpseQualified;

	@Column(name="NO_OPTION_QUALIFIED")
	private BigDecimal noOptionQualified;

	@Column(name="OPTION_QUALIFIED")
	private BigDecimal optionQualified;

	@Column(name="PARKING_ID")
	private String parkingId;

	@Column(name="PERCENTAGE_ZONE")
	private BigDecimal percentageZone;

	@Column(name="TOTAL_REFUSED")
	private BigDecimal totalRefused;

	@Column(name="TOTAL_REMINDER")
	private BigDecimal totalReminder;

	@Column(name="TOTAL_TRANSPORT")
	private BigDecimal totalTransport;

	public ParkingPropertyDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getCorpseQualified() {
		return this.corpseQualified;
	}

	public void setCorpseQualified(BigDecimal corpseQualified) {
		this.corpseQualified = corpseQualified;
	}

	public BigDecimal getNoOptionQualified() {
		return this.noOptionQualified;
	}

	public void setNoOptionQualified(BigDecimal noOptionQualified) {
		this.noOptionQualified = noOptionQualified;
	}

	public BigDecimal getOptionQualified() {
		return this.optionQualified;
	}

	public void setOptionQualified(BigDecimal optionQualified) {
		this.optionQualified = optionQualified;
	}

	public String getParkingId() {
		return this.parkingId;
	}

	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}

	public BigDecimal getPercentageZone() {
		return this.percentageZone;
	}

	public void setPercentageZone(BigDecimal percentageZone) {
		this.percentageZone = percentageZone;
	}

	public BigDecimal getTotalRefused() {
		return this.totalRefused;
	}

	public void setTotalRefused(BigDecimal totalRefused) {
		this.totalRefused = totalRefused;
	}

	public BigDecimal getTotalReminder() {
		return this.totalReminder;
	}

	public void setTotalReminder(BigDecimal totalReminder) {
		this.totalReminder = totalReminder;
	}

	public BigDecimal getTotalTransport() {
		return this.totalTransport;
	}

	public void setTotalTransport(BigDecimal totalTransport) {
		this.totalTransport = totalTransport;
	}

}