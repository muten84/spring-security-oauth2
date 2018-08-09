package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TS_BOOKING_ID database table.
 * 
 */
@Entity
@Table(name="TS_BOOKING_ID")
public class TsBookingIdDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TsBookingIdPKDO id;

	public TsBookingIdDO() {
	}

	public TsBookingIdPKDO getId() {
		return this.id;
	}

	public void setId(TsBookingIdPKDO id) {
		this.id = id;
	}

}