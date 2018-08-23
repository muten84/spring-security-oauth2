/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Bifulco Luigi
 *
 */
@Entity
@Table(name = "BOOKING_DOCUMENT")
public class BookingDocumentDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1276403445877880108L;

	@Column(name = "ID")
	@Id
	private String id;

	@Column(name = "DOCUMENT")
	private byte[] document;

	@Column(name = "STATE")
	@Enumerated(EnumType.ORDINAL)
	private BookingDocumentState state;

	@Column(name = "PARKING")
	private String parking;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "OPEN_DATE")
	private Date openDate;

	@Column(name = "CLOSE_DATE")
	private Date closeDate;

	@Column(name = "SENT_DATE")
	private Date sentDate;

	@Column(name = "FAILURE_DATE")
	private Date failureDate;

	@Column(name = "BOOKING_CODE")
	private String bookingCode;

	@Column(name = "FAILURE_CODE")
	private String failureCode;

	@Column(name = "FAILURE_REASON")
	private String failureReason;

	@Column(name = "OPERATION_DATE")
	private Date operationDate;

	@Column(name = "USER_AUDIT_OPEN")
	private String userOpen;

	public BookingDocumentDO() {

	}

	public BookingDocumentDO(String id, String parking, BookingDocumentState state) {
		this.id = id;
		this.parking = parking;
		this.state = state;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the document
	 */
	public byte[] getDocument() {
		return document;
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public void setDocument(byte[] document) {
		this.document = document;
	}

	/**
	 * @return the state
	 */
	public BookingDocumentState getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(BookingDocumentState state) {
		this.state = state;
	}

	/**
	 * @return the parking
	 */
	public String getParking() {
		return parking;
	}

	/**
	 * @param parking
	 *            the parking to set
	 */
	public void setParking(String parking) {
		this.parking = parking;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the openDate
	 */
	public Date getOpenDate() {
		return openDate;
	}

	/**
	 * @param openDate
	 *            the openDate to set
	 */
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	/**
	 * @return the closeDate
	 */
	public Date getCloseDate() {
		return closeDate;
	}

	/**
	 * @param closeDate
	 *            the closeDate to set
	 */
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	/**
	 * @return the bookingCode
	 */
	public String getBookingCode() {
		return bookingCode;
	}

	/**
	 * @param bookingCode
	 *            the bookingCode to set
	 */
	public void setBookingCode(String bookingCode) {
		this.bookingCode = bookingCode;
	}

	/**
	 * @return the sentDate
	 */
	public Date getSentDate() {
		return sentDate;
	}

	/**
	 * @param sentDate
	 *            the sentDate to set
	 */
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	/**
	 * @return the failureCode
	 */
	public String getFailureCode() {
		return failureCode;
	}

	/**
	 * @param failureCode
	 *            the failureCode to set
	 */
	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	/**
	 * @return the operationDate
	 */
	public Date getOperationDate() {
		return operationDate;
	}

	/**
	 * @param operationDate
	 *            the operationDate to set
	 */
	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	/**
	 * @return the failureReason
	 */
	public String getFailureReason() {
		return failureReason;
	}

	/**
	 * @param failureReason
	 *            the failureReason to set
	 */
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	/**
	 * @return the failureDate
	 */
	public Date getFailureDate() {
		return failureDate;
	}

	/**
	 * @param failureDate
	 *            the failureDate to set
	 */
	public void setFailureDate(Date failureDate) {
		this.failureDate = failureDate;
	}

	/**
	 * @return the userOpen
	 */
	public String getUserOpen() {
		return userOpen;
	}

	/**
	 * @param userOpen
	 *            the userOpen to set
	 */
	public void setUserOpen(String userOpen) {
		this.userOpen = userOpen;
	}

}
