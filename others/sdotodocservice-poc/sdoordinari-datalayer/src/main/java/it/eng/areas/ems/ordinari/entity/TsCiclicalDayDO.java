package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TS_CICLICAL_DAYS database table.
 * 
 */
@Entity
@Table(name="TS_CICLICAL_DAYS")
public class TsCiclicalDayDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="CICLICAL_ID")
	private String ciclicalId;

	@Column(name="DAY_NAME")
	private String dayName;

	public TsCiclicalDayDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCiclicalId() {
		return this.ciclicalId;
	}

	public void setCiclicalId(String ciclicalId) {
		this.ciclicalId = ciclicalId;
	}

	public String getDayName() {
		return this.dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

}