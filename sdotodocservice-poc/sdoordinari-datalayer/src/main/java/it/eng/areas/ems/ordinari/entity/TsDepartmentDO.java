package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TS_DEPARTMENT database table.
 * 
 */
@Entity
@Table(name="TS_DEPARTMENT")
public class TsDepartmentDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="AUTHORITY_ID")
	private String authorityId;

	private String compact;

	@Column(name="COST_CENTER")
	private String costCenter;

	private String description;

	@Column(name="HOUSE_NUMBER")
	private String houseNumber;

	private String locality;

	private String manutencoop;

	private String municipality;

	private String note;

	@Column(name="PAVILION_NAME")
	private String pavilionName;

	@Column(name="PAVILION_NUMBER")
	private String pavilionNumber;

	private String province;

	private String reference;

	@Column(name="SERVICE_TYPE")
	private String serviceType;

	@Column(name="STREET_NAME")
	private String streetName;

	public TsDepartmentDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthorityId() {
		return this.authorityId;
	}

	public void setAuthorityId(String authorityId) {
		this.authorityId = authorityId;
	}

	public String getCompact() {
		return this.compact;
	}

	public void setCompact(String compact) {
		this.compact = compact;
	}

	public String getCostCenter() {
		return this.costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHouseNumber() {
		return this.houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getManutencoop() {
		return this.manutencoop;
	}

	public void setManutencoop(String manutencoop) {
		this.manutencoop = manutencoop;
	}

	public String getMunicipality() {
		return this.municipality;
	}

	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPavilionName() {
		return this.pavilionName;
	}

	public void setPavilionName(String pavilionName) {
		this.pavilionName = pavilionName;
	}

	public String getPavilionNumber() {
		return this.pavilionNumber;
	}

	public void setPavilionNumber(String pavilionNumber) {
		this.pavilionNumber = pavilionNumber;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getStreetName() {
		return this.streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

}