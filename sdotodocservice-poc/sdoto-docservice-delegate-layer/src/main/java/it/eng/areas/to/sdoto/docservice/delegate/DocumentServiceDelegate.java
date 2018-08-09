/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.delegate;

import java.io.FileNotFoundException;
import java.util.List;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.area118.sdocommon.print.excpetions.PrintReportException;
import it.eng.areas.to.sdoto.docservice.delegate.model.BookingDocumentDTO;
import it.eng.areas.to.sdoto.docservice.delegate.model.BookingDocumentFilter;
import it.eng.areas.to.sdoto.docservice.delegate.model.User;
import it.eng.areas.to.sdoto.docservice.delegate.print.model.BookingsPrintDataSource;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;

/**
 * @author Bifulco Luigi
 *
 */
public interface DocumentServiceDelegate {

	/**
	 * Preleva le prenotazioni assegnate alla postazione in input e ne genera un documento che sar� poi salvato in db
	 * 
	 * @param assigned
	 * @throws PrintReportException
	 * @throws FileNotFoundException
	 */
	public BookingDocumentDTO createDocument(String assigned) throws PrintReportException, FileNotFoundException;

	/**
	 * Permette di cambiare lo stato del documento. Per i possibili stati fare riferimento alla enum
	 * {@link BookingDocumentState}
	 * 
	 * @param docId
	 * @param newSTate
	 * @return
	 */
	public BookingDocumentDTO changeDocumentStatus(String docId, BookingDocumentState newState, String user);

	/**
	 * Con questo metodo è possibile ottenere la lista dei documenti in base ai parametri passati nel filtro
	 * {@link BookingDocumentFilter}
	 * 
	 * @param filter
	 * @return
	 */
	public List<BookingDocumentDTO> listDocumentByFilter(BookingDocumentFilter filter);

	/**
	 * Restituisce un {@link BookingDocumentDTO} conoscendone il suo id
	 * 
	 * @param id
	 * @return
	 */
	public BookingDocumentDTO getDocumentById(String id);

	/**
	 * Permette di inviare un documento conoscendo l'indirizzo email del destinatario e il rifrimento del documento. Il
	 * riferimento del documento può essere il docId oppure il codice della prenotazione.
	 * 
	 * @param adress
	 * @param docReference
	 * @return i dati aggiornati del documento appena inviato, null se non è stato inviato nulla
	 */
	public BookingDocumentDTO sendDocumentByEmail(String address, String docReference);

	/**
	 * Permette di inviare un documento conoscendo il riferimento del destinatario ad esempio il nome del reparto e il
	 * rifrimento del documento. Il riferimento del destinatario può essere il nome del reparto Il riferimento del
	 * documento può essere il docId oppure il codice della prenotazione.
	 * 
	 * @param adress
	 * @param docReference
	 * @return i dati aggiornati del documento appena inviato, null se non è stato inviato nulla
	 */
	public BookingDocumentDTO sendDocumentByAddressee(String addressee, String docReference);

	/**
	 * Preleva la prenotazione assegnata alla postazione in input e ne genera un documento che sarà poi salvato in db Il
	 * docReference dove corrispondere al codice della prenotazione generato dalla centrale
	 * 
	 * 
	 * @param assigned
	 * @param docReference
	 * 
	 * @return
	 * @throws PrintReportException
	 * @throws FileNotFoundException
	 * @throws DataAccessException
	 */
	BookingDocumentDTO createDocument(String assigned, String docReference) throws PrintReportException, FileNotFoundException, DataAccessException;

	/**
	 * @param username
	 * @param password
	 * @return
	 * @throws IllegalAccessException
	 */
	User login(String username, String password);

	/**
	 * @param username
	 * @return
	 */
	User getAuthenticatedUser(String username);

	/**
	 * @param username
	 * @return
	 */
	boolean logout(String username);

	/**
	 * 
	 * @param username
	 * @param days
	 * @return
	 */
	public User needToRenew(String username, int days);
	
	public User changePassword(String username, String password);

}
