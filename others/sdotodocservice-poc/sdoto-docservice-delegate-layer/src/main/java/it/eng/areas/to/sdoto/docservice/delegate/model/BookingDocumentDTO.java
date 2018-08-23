/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.delegate.model;

import java.io.Serializable;
import java.util.Date;

import org.dozer.Mapping;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;

/**
 * @author Bifulco Luigi
 *
 */
public class BookingDocumentDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	@Mapping("document")
	@JsonIgnore
	private byte[] bynaryData;

	private BookingDocumentState state;

	private String parking;

	private Date creationDate;

	private Date openDate;

	private Date closeDate;

	private Date sentDate;

	@Mapping("bookingCode")
	private String docReference;

	public BookingDocumentDTO() {

	}

	public BookingDocumentDTO(String id, String parking, BookingDocumentState state) {
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
	 * @return the bynaryData
	 */
	public byte[] getBynaryData() {
		return bynaryData;
	}

	/**
	 * @param bynaryData
	 *            the bynaryData to set
	 */
	public void setBynaryData(byte[] bynaryData) {
		this.bynaryData = bynaryData;
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
	 * @return the docReference
	 */
	public String getDocReference() {
		return docReference;
	}

	/**
	 * @param docReference
	 *            the docReference to set
	 */
	public void setDocReference(String docReference) {
		this.docReference = docReference;
	}

}
