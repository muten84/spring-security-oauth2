/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentDO;
import it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter;

/**
 * @author Bifulco Luigi
 *
 */
@Repository
public interface BookingDocumentDAO extends EntityDAO<BookingDocumentDO, String> {

	public List<BookingDocumentDO> searchBookingDocument(BookingDocumentFilter filter);

}
