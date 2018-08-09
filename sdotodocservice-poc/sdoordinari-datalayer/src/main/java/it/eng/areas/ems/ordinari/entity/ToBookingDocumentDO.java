package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the TO_BOOKING_DOCUMENT database table.
 * 
 */
@Entity
@Table(name="TO_BOOKING_DOCUMENT")
public class ToBookingDocumentDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	
	@Lob
	@Column(name="BINARY_FIELD")
	private byte[] binaryField;

	@Column(name="BOOKING_ID")
	private String bookingId;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="CREATION_USR_NAME")
	private String creationUsrName;

	private String description;



	public ToBookingDocumentDO() {
	}

	public byte[] getBinaryField() {
		return this.binaryField;
	}

	public void setBinaryField(byte[] binaryField) {
		this.binaryField = binaryField;
	}

	public String getBookingId() {
		return this.bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationUsrName() {
		return this.creationUsrName;
	}

	public void setCreationUsrName(String creationUsrName) {
		this.creationUsrName = creationUsrName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

}