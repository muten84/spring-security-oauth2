/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.eng.areas.to.sdoto.docservice.dao.BookingDocumentDAO;
import it.eng.areas.to.sdoto.docservice.dao.DepartmentAdresseeDAO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentDO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;
import it.eng.areas.to.sdoto.docservice.entity.DepartmentAdresseeDO;
import it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter;
import it.eng.areas.to.sdoto.docservice.entity.filter.DepartmentAdresseeFilter;
import it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService;
import it.esel.parsley.lang.StringUtils;

/**
 * @author Bifulco Luigi
 *
 */
@Component
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DocumentTransactionalServiceImpl implements DocumentTransactionalService {

	@Autowired
	private BookingDocumentDAO bookingDocumentDAO;

	@Autowired
	private DepartmentAdresseeDAO departmentAddressDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService#createDocument(java.lang.String,
	 * byte[])
	 */
	@Override
	public BookingDocumentDO createDocument(String parking, byte[] pdf) {
		return createDocument(parking, null, pdf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService#createDocument(java.lang.String,
	 * java.lang.String, byte[])
	 */
	@Override
	public BookingDocumentDO createDocument(String parking, String bookingCode, byte[] pdf) {
		BookingDocumentDO newDoc = new BookingDocumentDO();
		newDoc.setId(UUID.randomUUID().toString());
		newDoc.setDocument(pdf);
		newDoc.setState(BookingDocumentState.CREATED);
		newDoc.setParking(parking);
		newDoc.setCreationDate(Calendar.getInstance().getTime());
		newDoc.setBookingCode(bookingCode);
		return bookingDocumentDAO.save(newDoc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService#changeDocumentState(java.lang.String,
	 * it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState)
	 */
	@Override
	public BookingDocumentDO changeDocumentState(String id, BookingDocumentState state, Date date, String user) {
		BookingDocumentDO bdoc = bookingDocumentDAO.get(id);
		BookingDocumentState currState = bdoc.getState();
		if (currState == state) {
			throw new IllegalStateException("Cannot write same state:  " + state);
		}
		if (state == BookingDocumentState.FAIL) {
			bdoc.setFailureDate(date);
		} else if (currState.ordinal() < state.ordinal()) {
			switch (state) {
			case CLOSED:
				bdoc.setCloseDate(date);
				break;
			case OPENED:
				if (!StringUtils.isEmpty(user)) {
					bdoc.setUserOpen(user);
				}
				bdoc.setOpenDate(date);
				break;
			case SENT:
				bdoc.setSentDate(date);
				break;
			default:
				throw new IllegalStateException("Unknown state to process: " + state);
			}
		}
		bdoc.setState(state);
		return bdoc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService#getBookingDocument(java.lang.String)
	 */
	@Override
	public BookingDocumentDO getBookingDocument(String id) {
		return bookingDocumentDAO.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService#searchBookingDocuments(it.eng.areas.to.
	 * sdoto.docservice.entity.filter.BookingDocumentFilter)
	 */
	@Override
	public List<BookingDocumentDO> searchBookingDocuments(BookingDocumentFilter filter) {
		return bookingDocumentDAO.searchBookingDocument(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService#searchDepartmentAdressees(it.eng.areas.to.
	 * sdoto.docservice.entity.filter.DepartmentAdresseeFilter)
	 */
	@Override
	public List<DepartmentAdresseeDO> searchDepartmentAdressees(DepartmentAdresseeFilter filter) {
		return departmentAddressDAO.searchDepartmentAddresses(filter);
	}

	@Override
	public DepartmentAdresseeDO getUserDepartment(String userId) {
		return departmentAddressDAO.getUserDepartment(userId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService#writeBookingDocumentFailure(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	@Override
	public BookingDocumentDO writeBookingDocumentFailure(String docId, String failureCode, String reason) {
		BookingDocumentDO docDO = bookingDocumentDAO.get(docId);
		changeDocumentState(docId, BookingDocumentState.FAIL, Calendar.getInstance().getTime(), null);
		docDO.setFailureCode(failureCode);
		docDO.setFailureReason(reason);
		return bookingDocumentDAO.save(docDO);
	}

}
