/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.service;

import java.util.Date;
import java.util.List;

import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentDO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;
import it.eng.areas.to.sdoto.docservice.entity.DepartmentAdresseeDO;
import it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter;
import it.eng.areas.to.sdoto.docservice.entity.filter.DepartmentAdresseeFilter;

/**
 * Servizio transazionale per le funzionalit� crud sui documenti
 * 
 * @author Bifulco Luigi
 *
 */
public interface DocumentTransactionalService {

	/**
	 * Permette di inserire un documento assegnato alla postazione opzionata
	 * 
	 * @param parking
	 *            l'id o il nome della postazione opzionata a cui � associato il documento
	 * @param pdf
	 *            lo stream di byte da memorizzare sul DB del documento
	 * @return l'oggetto BookingDocumentDO appena salvato
	 */
	BookingDocumentDO createDocument(String parking, byte[] pdf);

	/**
	 * Permette di inserire un documento assegnato alla postazione opzionata, nel caso in cui il documento sia associato
	 * ad un'unica prenotazione
	 * 
	 * @param parking
	 *            postazione opzionata
	 * @param bookingCode
	 *            codice prenotazione
	 * @param pdf
	 * @return
	 */
	BookingDocumentDO createDocument(String parking, String bookingCode, byte[] pdf);

	/**
	 * Permette di cambiare lo stato del documento per informazioni sui possibili stati di un documento fare riferimento
	 * alla enum {@link BookingDocumentState}
	 * 
	 * @param id
	 *            l'id del documento
	 * @param state
	 *            il nuovo stato del documento
	 * @param date
	 *            la data di cambiamento dello stato da memorizzare
	 * @return l'oggetto BookingDocumentDO appena modificato
	 */
	BookingDocumentDO changeDocumentState(String id, BookingDocumentState state, Date date, String user);

	/**
	 * Permette di leggere un documento conoscendone il suo id
	 * 
	 * @param id
	 * @return i dati del documento BookingDocumentDO
	 */
	BookingDocumentDO getBookingDocument(String id);

	/**
	 * Permette di cercare i documenti già inseriti in base ai campi del filtro {@link BookingDocumentFilter}
	 * 
	 * @param filter
	 * @return la lista di documenti con relativo stato e data
	 */
	List<BookingDocumentDO> searchBookingDocuments(BookingDocumentFilter filter);

	/**
	 * Permette di cercare i destinatari dei documenti in base al nome del reparto e in generale in base ai campi del
	 * filtro {@link DepartmentAdresseeFilter}
	 * 
	 * @param filter
	 * @return la lista dei destinatari con relativa mail
	 */
	List<DepartmentAdresseeDO> searchDepartmentAdressees(DepartmentAdresseeFilter filter);

	/**
	 * 
	 * @param docId
	 * @param failureCode
	 * @param reason
	 * @return
	 */
	BookingDocumentDO writeBookingDocumentFailure(String docId, String failureCode, String reason);

	/**
	 * @param userId
	 * @return
	 */
	DepartmentAdresseeDO getUserDepartment(String userId);

}
