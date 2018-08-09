package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TS_SERVICE_BOOKING database table.
 * 
 */
@Entity
@Table(name="TS_SERVICE_BOOKING")
public class TsServiceBookingDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	@Column(name="BOOKING_ID")
	private String bookingId;

	@Column(name="SERVICE_ID")
	private String serviceId;

	public TsServiceBookingDO() {
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

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

}