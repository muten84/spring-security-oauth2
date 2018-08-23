package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TS_SERVICE_ID database table.
 * 
 */
@Embeddable
public class TsServiceIdPKDO implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private long year;

	@Column(name="SERVICE_ID")
	private long serviceId;

	public TsServiceIdPKDO() {
	}
	public long getYear() {
		return this.year;
	}
	public void setYear(long year) {
		this.year = year;
	}
	public long getServiceId() {
		return this.serviceId;
	}
	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TsServiceIdPKDO)) {
			return false;
		}
		TsServiceIdPKDO castOther = (TsServiceIdPKDO)other;
		return 
			(this.year == castOther.year)
			&& (this.serviceId == castOther.serviceId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.year ^ (this.year >>> 32)));
		hash = hash * prime + ((int) (this.serviceId ^ (this.serviceId >>> 32)));
		
		return hash;
	}
}