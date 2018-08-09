/**
 * 
 */
package it.eng.areas.to.sdoto.docservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import it.eng.area118.sdocommon.persistence.configuration.SingleDataSourceConfiguration;
import it.eng.areas.ems.ordinari.dao.ToBookingDAO;
import it.eng.areas.ems.ordinari.dao.TsBookingIdDAO;
import it.eng.areas.ems.ordinari.dao.WebIdentityDAO;
import it.eng.areas.ems.ordinari.dao.WebSessionDAO;
import it.eng.areas.ems.ordinari.dao.impl.ToBookingDAOImpl;
import it.eng.areas.ems.ordinari.dao.impl.TsBookingIdDAOImpl;
import it.eng.areas.ems.ordinari.dao.impl.WebIdentityDAOImpl;
import it.eng.areas.ems.ordinari.dao.impl.WebSessionDAOImpl;
import it.eng.areas.ems.ordinari.service.BookingTransactionalService;
import it.eng.areas.ems.ordinari.service.WebIdentityService;
import it.eng.areas.ems.ordinari.service.impl.BookingTransactionalServiceImpl;
import it.eng.areas.ems.ordinari.service.impl.WebIdentityServiceImpl;
import it.eng.areas.to.sdoto.docservice.dao.BookingDocumentDAO;
import it.eng.areas.to.sdoto.docservice.dao.DepartmentAdresseeDAO;
import it.eng.areas.to.sdoto.docservice.dao.impl.BookingDocumentDAOImpl;
import it.eng.areas.to.sdoto.docservice.dao.impl.DepartmentAddresseeDAOImpl;
import it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService;
import it.eng.areas.to.sdoto.docservice.service.impl.DocumentTransactionalServiceImpl;

/**
 * @author Bifulco Luigi
 *
 */

@Import(SingleDataSourceConfiguration.class)
@Configuration
public class DataLayerDocServiceConfiguration {

	@Bean
	public BookingDocumentDAO bookingDocumentDAO() {
		return new BookingDocumentDAOImpl();
	}

	@Bean
	public DepartmentAdresseeDAO departmentAdresseeDAO() {
		return new DepartmentAddresseeDAOImpl();
	}

	@Bean
	public DocumentTransactionalService documentService() {
		return new DocumentTransactionalServiceImpl();
	}

	@Bean
	public BookingTransactionalService bookingTransactionalService() {
		return new BookingTransactionalServiceImpl();
	}

	@Bean
	public ToBookingDAO toBookingDAO() {
		return new ToBookingDAOImpl();
	}

	@Bean
	public TsBookingIdDAO tsBookingIdDAO() {
		return new TsBookingIdDAOImpl();
	}

	@Bean
	public WebIdentityService webIdentityService() {
		return new WebIdentityServiceImpl();
	}

	@Bean
	public WebIdentityDAO webIdentityDAO() {
		return new WebIdentityDAOImpl();
	}

	@Bean
	public WebSessionDAO webSessionDAO() {
		return new WebSessionDAOImpl();
	}

}
