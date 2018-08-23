/**
 * 
 */
package it.eng.areas.ems.ordinari.test;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import it.eng.area118.sdocommon.dao.DataAccessException;
import it.eng.areas.ems.ordinari.dao.rule.BookingDetailRule;
import it.eng.areas.ems.ordinari.entity.ToBookingDO;
import it.eng.areas.ems.ordinari.service.BookingTransactionalService;

/**
 * @author Bifulco Luigi
 *
 */
public class BookingTransactionalServiceTest extends AbstractOrdinariDAOTests {

	
	@Autowired
	private BookingTransactionalService service;

	//Preparazione dati test in AbstractOrdinariDAOTests <-- classe da cui si estende
	@Test
	@Commit
	@Rollback(value=true)
	public final void testGetBookingByCodeWithBookingDetailRule() throws DataAccessException {
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
		executeSqlScript(BASE_SQL_PATH + "insert.sql", false);
		String bookingCode = "16063351";
		String fetchRule = BookingDetailRule.NAME;
		ToBookingDO bookingDO = service.getBookingByCode(bookingCode, fetchRule);
		Assert.assertNotNull(bookingDO);
		Assert.assertNotNull(bookingDO.getBookingPatients());
		Assert.assertTrue(bookingDO.getCode().equals(bookingCode));
		Assert.assertTrue(!bookingDO.getBookingPatients().isEmpty());
		Assert.assertTrue(bookingDO.getBookingPatients().iterator().next().getSurname().equals("PROVA1"));
		Assert.assertTrue(!bookingDO.getBookingEquipments().isEmpty());
		Assert.assertTrue(bookingDO.getBookingEquipments().iterator().next().getEquipment().getDescription().equals("SPINALE"));
	}

}
