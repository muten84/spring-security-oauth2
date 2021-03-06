/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.entity.filter;

import java.util.Date;

import it.eng.area118.sdocommon.dao.filter.EntityFilter;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentDO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;

/**
 * Filtro utile a cercare dei {@link BookingDocumentDO}
 * 
 * 
 * @author Bifulco Luigi
 *
 */
public class BookingDocumentFilter extends EntityFilter {

	private Date fromDate;

	private Date toDate;

	private BookingDocumentState inState;

	private BookingDocumentState currentState;

	private BookingDocumentState excludeInCurrentState;

	private String parking;

	private boolean exactParkingMatch;

	private String docReference;

	/**
	 * @return the exactParkingMatch
	 */
	public boolean isExactParkingMatch() {
		return exactParkingMatch;
	}

	/**
	 * @param exactParkingMatch
	 *            the exactParkingMatch to set
	 */
	public void setExactParkingMatch(boolean exactParkingMatch) {
		this.exactParkingMatch = exactParkingMatch;
	}

	public BookingDocumentFilter() {

	}

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate
	 *            the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate
	 *            the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the inState
	 */
	public BookingDocumentState getInState() {
		return inState;
	}

	/**
	 * @param inState
	 *            the inState to set
	 */
	public void setInState(BookingDocumentState inState) {
		this.inState = inState;
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

	/**
	 * @return the currentState
	 */
	public BookingDocumentState getCurrentState() {
		return currentState;
	}

	/**
	 * @param currentState
	 *            the currentState to set
	 */
	public void setCurrentState(BookingDocumentState currentState) {
		this.currentState = currentState;
	}

	/**
	 * @return the excludeInCurrentState
	 */
	public BookingDocumentState getExcludeInCurrentState() {
		return excludeInCurrentState;
	}

	/**
	 * @param excludeInCurrentState
	 *            the excludeInCurrentState to set
	 */
	public void setExcludeInCurrentState(BookingDocumentState excludeInCurrentState) {
		this.excludeInCurrentState = excludeInCurrentState;
	}

}
