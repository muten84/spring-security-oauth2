package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TS_PATIENT_COMPANION database table.
 * 
 */
@Entity
@Table(name="TS_PATIENT_COMPANION")
public class TsPatientCompanionDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	private String compact;

	private String description;

	@Column(name="WEB_VISIBLE")
	private BigDecimal webVisible;

	public TsPatientCompanionDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompact() {
		return this.compact;
	}

	public void setCompact(String compact) {
		this.compact = compact;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getWebVisible() {
		return this.webVisible;
	}

	public void setWebVisible(BigDecimal webVisible) {
		this.webVisible = webVisible;
	}

}