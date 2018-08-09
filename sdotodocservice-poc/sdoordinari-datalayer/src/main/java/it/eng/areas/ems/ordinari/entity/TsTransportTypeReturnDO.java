package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the TS_TRANSPORT_TYPE_RETURN database table.
 * 
 */
@Entity
@Table(name="TS_TRANSPORT_TYPE_RETURN")
public class TsTransportTypeReturnDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;


	@Column(name="TRANSPORT_TYPE_ID")
	private String transportTypeId;

	@Column(name="TRANSPORT_TYPE_RET_ID")
	private String transportTypeRetId;

	public TsTransportTypeReturnDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransportTypeId() {
		return this.transportTypeId;
	}

	public void setTransportTypeId(String transportTypeId) {
		this.transportTypeId = transportTypeId;
	}

	public String getTransportTypeRetId() {
		return this.transportTypeRetId;
	}

	public void setTransportTypeRetId(String transportTypeRetId) {
		this.transportTypeRetId = transportTypeRetId;
	}

}