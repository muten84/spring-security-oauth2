package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the TO_BOOKING_HISTORY database table.
 * 
 */
@Entity
@Table(name="TO_BOOKING_HISTORY")
public class ToBookingHistoryDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="BOOKING_ID")
	private String bookingId;

	@Temporal(TemporalType.DATE)
	@Column(name="MODIFICATION_DATE")
	private Date modificationDate;

	@Column(name="MODIFICATION_SYNTHESIS")
	private String modificationSynthesis;

	@Column(name="MODIFICATION_USER")
	private String modificationUser;

	public ToBookingHistoryDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public Date getModificationDate() {
		return this.modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
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