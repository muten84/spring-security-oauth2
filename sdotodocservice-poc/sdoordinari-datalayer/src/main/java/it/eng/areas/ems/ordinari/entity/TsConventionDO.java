package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TS_CONVENTION database table.
 * 
 */
@Entity
@Table(name="TS_CONVENTION")
public class TsConventionDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	private String compact;

	private String description;

	private BigDecimal ssn;

	public TsConventionDO() {
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

	public BigDecimal getSsn() {
		return this.ssn;
	}

	public void setSsn(BigDecimal ssn) {
		this.ssn = ssn;
	}

}