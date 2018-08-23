package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TS_BOOKING_ID database table.
 * 
 */
@Embeddable
public class TsBookingIdPKDO implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private long year;

	@Column(name="BOOKING_ID")
	private long bookingId;

	public TsBookingIdPKDO() {
	}
	public long getYear() {
		return this.year;
	}
	public void setYear(long year) {
		this.year = year;
	}
	public long getBookingId() {
		return this.bookingId;
	}
	public void setBookingId(long bookingId) {
		this.bookingId = bookingId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TsBookingIdPKDO)) {
			return false;
		}
		TsBookingIdPKDO castOther = (TsBookingIdPKDO)other;
		return 
			(this.year == castOther.year)
			&& (this.bookingId == castOther.bookingId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.year ^ (this.year >>> 32)));
		hash = hash * prime + ((int) (this.bookingId ^ (this.bookingId >>> 32)));
		
		return hash;
	}
}