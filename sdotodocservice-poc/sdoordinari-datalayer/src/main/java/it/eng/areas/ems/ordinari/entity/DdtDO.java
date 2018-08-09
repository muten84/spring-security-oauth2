package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the DDT database table.
 * 
 */
@Entity
@Table(name="DDT")
public class DdtDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="BOOKING_ID")
	private String bookingId;

	private String code;


	public DdtDO() {
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

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}