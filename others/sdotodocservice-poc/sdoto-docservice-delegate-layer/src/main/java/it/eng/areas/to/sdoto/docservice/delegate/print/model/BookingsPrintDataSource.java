/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.delegate.print.model;

import java.util.Date;

import it.eng.area118.sdocommon.print.CollectionPrintDataSource;

/**
 * @author Bifulco Luigi
 *
 */
public class BookingsPrintDataSource extends CollectionPrintDataSource {

	// TODO: inserire i campi da visualizzare sulla pdf del documento

	// codice
	private String bookingCode;

	// data trasporto
	private Date transportDate;

	// trasportato
	private String transported;

	// data di nascita
	private String birthDate;

	// codice fiscale/tessera sanitaria
	private String ssn;

	// categoria
	private String category;

	// Tipo mezzo
	private String vehicleType;

	// Tipo trasporto
	private String transportType;

	// Deambulazione
	private String deambulation;

	// priorità
	private String priority;

	// Accompagnatore
	private String companion;

	// Convenzione
	private String convention;

	// Richidente del
	private String requestorOf;

	// Da (partenza)
	private String fromPlace;

	// Telefono da
	private String fromPhone;

	// A (destinazione)
	private String toPlace;

	private String toPhone;

	private String equipmentList;

	private String note;

	private String birthPlace;

	private String reqConvention;

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
	 * @return the transportDate
	 */
	public Date getTransportDate() {
		return transportDate;
	}

	/**
	 * @param transportDate
	 *            the transportDate to set
	 */
	public void setTransportDate(Date transportDate) {
		this.transportDate = transportDate;
	}

	/**
	 * @return the transported
	 */
	public String getTransported() {
		return transported;
	}

	/**
	 * @param transported
	 *            the transported to set
	 */
	public void setTransported(String transported) {
		this.transported = transported;
	}

	/**
	 * @return the birthDate
	 */
	public String getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * @param ssn
	 *            the ssn to set
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the vehicleType
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType
	 *            the vehicleType to set
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return the transportType
	 */
	public String getTransportType() {
		return transportType;
	}

	/**
	 * @param transportType
	 *            the transportType to set
	 */
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	/**
	 * @return the deambulation
	 */
	public String getDeambulation() {
		return deambulation;
	}

	/**
	 * @param deambulation
	 *            the deambulation to set
	 */
	public void setDeambulation(String deambulation) {
		this.deambulation = deambulation;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * @return the companion
	 */
	public String getCompanion() {
		return companion;
	}

	/**
	 * @param companion
	 *            the companion to set
	 */
	public void setCompanion(String companion) {
		this.companion = companion;
	}

	/**
	 * @return the convention
	 */
	public String getConvention() {
		return convention;
	}

	/**
	 * @param convention
	 *            the convention to set
	 */
	public void setConvention(String convention) {
		this.convention = convention;
	}

	/**
	 * @return the requestorOf
	 */
	public String getRequestorOf() {
		return requestorOf;
	}

	/**
	 * @param requestorOf
	 *            the requestorOf to set
	 */
	public void setRequestorOf(String requestorOf) {
		this.requestorOf = requestorOf;
	}

	/**
	 * @return the fromPlace
	 */
	public String getFromPlace() {
		return fromPlace;
	}

	/**
	 * @param fromPlace
	 *            the fromPlace to set
	 */
	public void setFromPlace(String fromPlace) {
		this.fromPlace = fromPlace;
	}

	/**
	 * @return the fromPhone
	 */
	public String getFromPhone() {
		return fromPhone;
	}

	/**
	 * @param fromPhone
	 *            the fromPhone to set
	 */
	public void setFromPhone(String fromPhone) {
		this.fromPhone = fromPhone;
	}

	/**
	 * @return the toPlace
	 */
	public String getToPlace() {
		return toPlace;
	}

	/**
	 * @param toPlace
	 *            the toPlace to set
	 */
	public void setToPlace(String toPlace) {
		this.toPlace = toPlace;
	}

	/**
	 * @return the toPhone
	 */
	public String getToPhone() {
		return toPhone;
	}

	/**
	 * @param toPhone
	 *            the toPhone to set
	 */
	public void setToPhone(String toPhone) {
		this.toPhone = toPhone;
	}

	/**
	 * @return the equipmentList
	 */
	public String getEquipmentList() {
		return equipmentList;
	}

	/**
	 * @param equipmentList
	 *            the equipmentList to set
	 */
	public void setEquipmentList(String equipmentList) {
		this.equipmentList = equipmentList;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the birthPlace
	 */
	public String getBirthPlace() {
		return birthPlace;
	}

	/**
	 * @param birthPlace
	 *            the birthPlace to set
	 */
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	/**
	 * @return the reqConvention
	 */
	public String getReqConvention() {
		return reqConvention;
	}

	/**
	 * @param reqConvention
	 *            the reqConvention to set
	 */
	public void setReqConvention(String reqConvention) {
		this.reqConvention = reqConvention;
	}

}
