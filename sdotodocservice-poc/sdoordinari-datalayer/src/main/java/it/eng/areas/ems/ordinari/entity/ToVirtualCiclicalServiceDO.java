package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TO_VIRTUAL_CICLICAL_SERVICE database table.
 * 
 */
@Entity
@Table(name="TO_VIRTUAL_CICLICAL_SERVICE")
public class ToVirtualCiclicalServiceDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	private String alias;

	@Column(name="CICLICAL_BOOKING_ID")
	private String ciclicalBookingId;

	@Column(name="GROUP_GUID")
	private String groupGuid;

	public ToVirtualCiclicalServiceDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getCiclicalBookingId() {
		return this.ciclicalBookingId;
	}

	public void setCiclicalBookingId(String ciclicalBookingId) {
		this.ciclicalBookingId = ciclicalBookingId;
	}

	public String getGroupGuid() {
		return this.groupGuid;
	}

	public void setGroupGuid(String groupGuid) {
		this.groupGuid = groupGuid;
	}

}