package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the TS_CICLICAL_DAYS_INFO_HIST database table.
 * 
 */
@Entity
@Table(name="TS_CICLICAL_DAYS_INFO_HIST")
public class TsCiclicalDaysInfoHistDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@Column(name="CICLICAL_DAY_INFO_ID")
	private String ciclicalDayInfoId;

	@Column(name="CICLICAL_ID")
	private String ciclicalId;


	@Temporal(TemporalType.DATE)
	@Column(name="MODIFICATION_DATE")
	private Date modificationDate;

	@Column(name="MODIFICATION_NOTES")
	private String modificationNotes;

	@Column(name="MODIFICATION_SYNTHESIS")
	private String modificationSynthesis;

	@Column(name="MODIFICATION_USER")
	private String modificationUser;

	public TsCiclicalDaysInfoHistDO() {
	}

	public String getCiclicalDayInfoId() {
		return this.ciclicalDayInfoId;
	}

	public void setCiclicalDayInfoId(String ciclicalDayInfoId) {
		this.ciclicalDayInfoId = ciclicalDayInfoId;
	}

	public String getCiclicalId() {
		return this.ciclicalId;
	}

	public void setCiclicalId(String ciclicalId) {
		this.ciclicalId = ciclicalId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getModificationDate() {
		return this.modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getModificationNotes() {
		return this.modificationNotes;
	}

	public void setModificationNotes(String modificationNotes) {
		this.modificationNotes = modificationNotes;
	}

	public String getModificationSynthesis() {
		return this.modificationSynthesis;
	}

	public void setModificationSynthesis(String modificationSynthesis) {
		this.modificationSynthesis = modificationSynthesis;
	}

	public String getModificationUser() {
		return this.modificationUser;
	}

	public void setModificationUser(String modificationUser) {
		this.modificationUser = modificationUser;
	}

}