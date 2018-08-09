package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TS_DELETE_BOOKING_REASON database table.
 * 
 */
@Entity
@Table(name="TS_DELETE_BOOKING_REASON")
public class TsDeleteBookingReasonDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	private String compact;

	private String description;

	public TsDeleteBookingReasonDO() {
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

}