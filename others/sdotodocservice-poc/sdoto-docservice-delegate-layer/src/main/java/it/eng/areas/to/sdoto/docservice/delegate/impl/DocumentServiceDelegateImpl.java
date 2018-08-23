/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.delegate.impl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.area118.sdocommon.print.excpetions.PrintReportException;
import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.ordinari.dao.rule.BookingDetailRule;
import it.eng.areas.ems.ordinari.entity.ToBookingDO;
import it.eng.areas.ems.ordinari.entity.WebIdentityDO;
import it.eng.areas.ems.ordinari.entity.WebSessionDO;
import it.eng.areas.ems.ordinari.service.BookingTransactionalService;
import it.eng.areas.ems.ordinari.service.WebIdentityService;
import it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.model.BookingDocumentDTO;
import it.eng.areas.to.sdoto.docservice.delegate.model.BookingDocumentFilter;
import it.eng.areas.to.sdoto.docservice.delegate.model.User;
import it.eng.areas.to.sdoto.docservice.delegate.notify.NotifyServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.print.PrintBookingsServiceDelegate;
import it.eng.areas.to.sdoto.docservice.delegate.print.model.BookingsPrintDataSource;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentDO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;
import it.eng.areas.to.sdoto.docservice.entity.DepartmentAdresseeDO;
import it.eng.areas.to.sdoto.docservice.entity.filter.DepartmentAdresseeFilter;
import it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService;
import it.esel.parsley.lang.StringUtils;

/**
 * @author Bifulco Luigi
 *
 */
@Component
public class DocumentServiceDelegateImpl implements DocumentServiceDelegate {

	@Value("${notifier.mail.linkToDoc}")
	private String baseURL;

	@Value("${notifier.mail.text}")
	private String text;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private PrintBookingsServiceDelegate printDelegate;

	@Autowired
	private DocumentTransactionalService persistenceService;

	@Autowired
	private NotifyServiceDelegate notifierService;

	@Autowired
	private BookingTransactionalService bookingDetailService;

	@Autowired
	private WebIdentityService webIdentityService;

	@Override
	public BookingDocumentDTO createDocument(String assigned) throws PrintReportException, FileNotFoundException {
		// TODO: ds fill data source with bookings data
		if (StringUtils.isEmpty(assigned)) {
			throw new IllegalArgumentException();
		}
		BookingsPrintDataSource ds = null;
		// TODO: ricavare le prenotazioni dalla postazione e creare un documento con la lista delle prenotazioni
		// associate
		byte[] doc = printDelegate.createBookingDocument(ds);
		/* inserisco in db */
		BookingDocumentDO createdDocument = persistenceService.createDocument(assigned, doc);
		return dtoService.convertObject(createdDocument, BookingDocumentDTO.class);
	}

	@Override
	public BookingDocumentDTO createDocument(String assigned, String docReference) throws PrintReportException, FileNotFoundException, DataAccessException {

		if (StringUtils.isAlpha(docReference) || StringUtils.isEmpty(assigned)) {
			throw new IllegalArgumentException();
		}

		BookingDocumentFilter filter = new BookingDocumentFilter();
		filter.setDocReference(docReference);
		// filter.setParking(assigned);
		filter.setExactParkingMatch(false);
		Calendar from = Calendar.getInstance();
		from.set(Calendar.HOUR, 0);
		from.set(Calendar.MINUTE, 0);
		from.set(Calendar.SECOND, 0);

		filter.setFromDate(from.getTime());
		filter.setToDate(Calendar.getInstance().getTime());

		/* verifico se ci sono documenti pendenti per la stessa prenotazione */
		List<BookingDocumentDTO> dtos = listDocumentByFilter(filter);
		if (dtos != null && dtos.size() > 0) {
			for (BookingDocumentDTO bookingDocumentDTO : dtos) {
				BookingDocumentState state = bookingDocumentDTO.getState();
				if (state != BookingDocumentState.CLOSED) {
					throw new IllegalStateException("Document for booking: " + docReference + "not closed. To create a new document you have to close pending documents with changeDocumentStatus method");
				}
			}
		}

		ToBookingDO bookingDetail = bookingDetailService.getBookingByCode(docReference, BookingDetailRule.NAME);
		BookingsPrintDataSource ds = DocumentServiceDelegateImplHelper.convertBookingsPrintDataSource(bookingDetail);
		/* creo ikl documento e lo tratto come stream di bytes */
		byte[] doc = printDelegate.createBookingDocument(ds);
		/* inserisco in db */
		BookingDocumentDO createdDocument = persistenceService.createDocument(assigned, docReference, doc);
		return dtoService.convertObject(createdDocument, BookingDocumentDTO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate#changeDocumentStatus(java.lang.String,
	 * it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState)
	 */
	@Override
	public BookingDocumentDTO changeDocumentStatus(String docId, BookingDocumentState newState, String user) {
		BookingDocumentDO bdo = persistenceService.changeDocumentState(docId, newState, Calendar.getInstance().getTime(), user);
		return dtoService.convertObject(bdo, BookingDocumentDTO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate#listDocumentByFilter(it.eng.areas.to.sdoto.
	 * docservice.delegate.model.BookingDocumentFilter)
	 */
	@Override
	public List<BookingDocumentDTO> listDocumentByFilter(BookingDocumentFilter filter) {
		it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter filterDO = dtoService.convertObject(filter, it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter.class);
		List<BookingDocumentDO> bdos = persistenceService.searchBookingDocuments(filterDO);
		List<BookingDocumentDTO> toRet = new ArrayList<>();
		dtoService.convertCollection(bdos, toRet, BookingDocumentDTO.class);
		return toRet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate#getDocumentById(java.lang.String)
	 */
	@Override
	public BookingDocumentDTO getDocumentById(String id) {
		BookingDocumentDO document = persistenceService.getBookingDocument(id);
		return dtoService.convertObject(document, BookingDocumentDTO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate#sendDocumentByEmail(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public BookingDocumentDTO sendDocumentByEmail(String address, String docReference) {
		DepartmentAdresseeFilter filter = new DepartmentAdresseeFilter();
		filter.setEmail(address);
		List<DepartmentAdresseeDO> list = persistenceService.searchDepartmentAdressees(filter);
		it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter filter2 = new it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter();
		filter2.setDocReference(docReference);
		List<BookingDocumentDO> documents = persistenceService.searchBookingDocuments(filter2);
		BookingDocumentDO doc = documents.iterator().next();
		String[] receivers = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			// TODO: check if notification was sent and signals exception
			receivers[i] = list.get(i).getAddress();
		}
		boolean sent = notifierService.sendSimpleData(receivers, "");
		if (sent) {
			doc = persistenceService.changeDocumentState(doc.getId(), BookingDocumentState.SENT, Calendar.getInstance().getTime(), null);
		}
		return dtoService.convertObject(doc, BookingDocumentDTO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate#sendDocumentByAddressee(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public BookingDocumentDTO sendDocumentByAddressee(String addressee, String docReference) {
		DepartmentAdresseeFilter filter = new DepartmentAdresseeFilter();
		filter.setDepartment(addressee);
		filter.setExactDepartmentMatch(true);
		List<DepartmentAdresseeDO> list = persistenceService.searchDepartmentAdressees(filter);
		it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter filter2 = new it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter();
		filter2.setDocReference(docReference);

		Calendar from = Calendar.getInstance();
		from.add(Calendar.DAY_OF_MONTH, -3);
		filter2.setFromDate(from.getTime());

		Calendar to = Calendar.getInstance();
		filter2.setToDate(to.getTime());

		filter2.setExcludeInCurrentState(BookingDocumentState.CLOSED);

		List<BookingDocumentDO> documents = persistenceService.searchBookingDocuments(filter2);
		BookingDocumentDO doc = documents.iterator().next();
		String[] receivers = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			// TODO: check if notification was sent and signals exception
			receivers[i] = list.get(i).getAddress();
		}
		// String text = "Prenotazione " + docReference + " inviata dalla centrale collegarsi al portale per scaricare
		// il dettaglio: "+baseURL;
		String text = this.text.replaceAll("_BOOKING_CODE_", docReference).replaceAll("_LINK_", baseURL);
		boolean sent = notifierService.sendSimpleData(receivers, text);
		if (sent) {
			doc = persistenceService.changeDocumentState(doc.getId(), BookingDocumentState.SENT, Calendar.getInstance().getTime(), null);
		}
		return dtoService.convertObject(doc, BookingDocumentDTO.class);
	}

	@Override
	public boolean logout(String username) {
		return webIdentityService.destroySession(username);
	}

	@Override
	public User getAuthenticatedUser(String username) {
		WebIdentityDO userDO = webIdentityService.getUser(username);
		String session = webIdentityService.getUserSession(username);
		DepartmentAdresseeDO dep = persistenceService.getUserDepartment(userDO.getId());
		if (!StringUtils.isEmpty(session)) {
			User user = new User();
			user.setUsername(username);
			user.setToken(session);
			user.addDetail("parking", dep.getDescription());
			return user;
		}
		return null;
	}

	@Override
	public User login(String username, String password) {
		WebIdentityDO userDO = webIdentityService.getUser(username);
		if(userDO == null)
			 return null;
		String rand = UUID.randomUUID().toString().substring(0, 9);
		String token = username + ":" + rand;
		WebSessionDO session = webIdentityService.createSession(username, token);
		if (session == null) {
			throw new RuntimeException("session was not generated correctly");
		}
		DepartmentAdresseeDO dep = persistenceService.getUserDepartment(userDO.getId());
		String parking = "";
		
		if(dep == null){
			return null;
		}
		
		if (dep != null) {
			parking = dep.getDescription();
		}
		User user = new User();
		if (userDO.getPasswd().equals(password)) {
			user.setUsername(userDO.getLogin());
			user.setToken(session.getSessionId());
			user.addDetail("parking", parking);
			return user;
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate#needToRenew(java.lang.String, int)
	 */
	@Override
	public User needToRenew(String username, int days) {
		WebIdentityDO identity = webIdentityService.needToRenewPassword(username, days);
		if (identity != null) {
			User user = new User();
			user.setUsername(identity.getLogin());
			return user;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.delegate.DocumentServiceDelegate#changePassword(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public User changePassword(String username, String password) {
		WebIdentityDO identity = webIdentityService.changePassword(username, password);
		if (identity != null) {
			User user = new User();
			user.setUsername(identity.getLogin());
			return user;
		}
		return null;

	}

	// public static void main(String[] args) {
	// String text = "PROVA REPLACE \\{BASE_URL\\}";
	// String s = text.replaceFirst("\\{BASE_URL\\}", "http://");
	// System.out.println(s);
	// }
}
