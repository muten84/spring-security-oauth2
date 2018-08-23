package it.eng.areas.to.sdoto.docservice.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Base64Utils;

import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentDO;
import it.eng.areas.to.sdoto.docservice.entity.BookingDocumentState;
import it.eng.areas.to.sdoto.docservice.entity.DepartmentAdresseeDO;
import it.eng.areas.to.sdoto.docservice.entity.filter.BookingDocumentFilter;
import it.eng.areas.to.sdoto.docservice.entity.filter.DepartmentAdresseeFilter;
import it.eng.areas.to.sdoto.docservice.service.DocumentTransactionalService;
import it.esel.parsley.lang.StringUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceTestCtx.class })
@ActiveProfiles(profiles = "hsqldb") // possible profiles oracle, hsqldb
public class DocumentTransactionalServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static EmbeddedDatabase db;

	private final static String BASE_PATH = "classpath:it/eng/areas/to/sdoto/docservice/test-context/";

	private final static String BASE_SQL_PATH = BASE_PATH + "common/sql/";

	@Autowired
	private DocumentTransactionalService service;

	@BeforeClass
	public static void createCommonDb() {
		String staticBase = "it/eng/areas/to/sdoto/docservice/test-context/common/sql/";
		String dynBase = "it/eng/areas/to/sdoto/docservice/test-context/common/sql/";
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		builder.setName("testdb");
		db = builder.setType(EmbeddedDatabaseType.HSQL) // .H2
														// or
														// .DERBY
				.setName("testdb")//
				.addScript(staticBase + "schema.sql")//
				// .addScript(staticBase + "insert.sql")//
				.build();
	}

	@Test
	@Commit
	public final void testSaveDocument() throws IOException {
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
		String parking = "TEST_PARKING";
		String bPath = "src/test/resources/it/eng/areas/to/sdoto/docservice/test-context/";
		byte[] bytes = DocumentTransactionalServiceTestHelper.readDocumentFromFile(bPath + "pdf/file.pdf");
		BookingDocumentDO document = service.createDocument(parking, bytes);
		File newFile = DocumentTransactionalServiceTestHelper.saveDocument(bPath + "pdf/newFile.pdf", bytes);
		Assert.assertTrue(document != null);
		byte[] bytesNew = DocumentTransactionalServiceTestHelper.readDocumentFromFile(bPath + "pdf/file.pdf");
		Assert.assertTrue(bytes.length == bytesNew.length);
		Assert.assertTrue(!StringUtils.isEmpty(document.getId()));
		Assert.assertTrue(document.getCreationDate() != null);
		Assert.assertTrue(document.getState() == BookingDocumentState.CREATED);
		FileUtils.forceDelete(newFile);

	}

	@Test(expected = IllegalStateException.class)
	@Rollback
	public final void shouldThrowIllegalStateExceptionDuringChangeState() {
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
		executeSqlScript(BASE_SQL_PATH + "insert.sql", false);
		String expectedId = "TEST";
		BookingDocumentDO doc = service.changeDocumentState(expectedId, BookingDocumentState.CREATED, Calendar.getInstance().getTime(), "user");
	}

	@Test
	@Commit
	public final void shouldChangeTheDocumentStateToOpened() {
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
		executeSqlScript(BASE_SQL_PATH + "insert.sql", false);
		String expectedId = "TEST";
		Calendar c = Calendar.getInstance();
		BookingDocumentDO document = service.changeDocumentState(expectedId, BookingDocumentState.OPENED, c.getTime(), "user");
		Assert.assertTrue(!StringUtils.isEmpty(document.getId()));
		Assert.assertTrue(document.getCreationDate() != null);
		Assert.assertTrue(document.getState() == BookingDocumentState.OPENED);
		Assert.assertTrue(document.getOpenDate().equals(c.getTime()));
	}
	
	@Test
	@Commit
	public final void shouldSearchallDocuments() {
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
		executeSqlScript(BASE_SQL_PATH + "insert.sql", false);
		BookingDocumentFilter filter = new BookingDocumentFilter();
		filter.setInState(null);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.add(Calendar.MINUTE, -10);
		Date fromDate = c1.getTime();
		c1.add(Calendar.MINUTE, 30);
		Date toDate = c2.getTime();
		filter.setFromDate(fromDate);
		filter.setToDate(toDate);
		List<BookingDocumentDO> documents = service.searchBookingDocuments(filter);
		Assert.assertTrue(documents.size() > 0);
	}

	@Test
	@Commit
	public final void shouldSearchCurrentDocuments() {
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
		executeSqlScript(BASE_SQL_PATH + "insert.sql", false);
		BookingDocumentFilter filter = new BookingDocumentFilter();
		filter.setInState(BookingDocumentState.CREATED);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.add(Calendar.MINUTE, -10);
		Date fromDate = c1.getTime();
		c1.add(Calendar.MINUTE, 30);
		Date toDate = c2.getTime();
		filter.setFromDate(fromDate);
		filter.setToDate(toDate);
		List<BookingDocumentDO> documents = service.searchBookingDocuments(filter);
		Assert.assertTrue(documents.size() > 0);
		Assert.assertTrue(documents.size() == 1);
		for (BookingDocumentDO bookingDocumentDO : documents) {
			Assert.assertTrue(bookingDocumentDO.getCloseDate() == null);
			Assert.assertTrue(bookingDocumentDO.getOpenDate() == null);
			Assert.assertTrue(bookingDocumentDO.getCreationDate() != null);
			Date date = bookingDocumentDO.getCreationDate();
			// Calendar c = Calendar.getInstance();
			// c.setTime(date);
			Assert.assertTrue(date.after(fromDate) && date.before(toDate));
		}
	}

	@Test
	@Commit
	public final void shouldCreateADocWithBookingIdAssigned() throws IOException {
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
		String parking = "TEST_PARKING";
		String bPath = "src/test/resources/it/eng/areas/to/sdoto/docservice/test-context/";
		byte[] bytes = DocumentTransactionalServiceTestHelper.readDocumentFromFile(bPath + "pdf/file.pdf");
		String bookingCode = "12345678";
		BookingDocumentDO bookingDocument = service.createDocument(parking, bookingCode, bytes);
		Assert.assertTrue(bookingDocument != null);
		Assert.assertTrue(bookingDocument.getBookingCode().equals(bookingCode));

	}

	@Test
	@Commit
	public final void shouldListDepartmentAddresses() {
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
		executeSqlScript(BASE_SQL_PATH + "insert_addressee.sql", false);
		String toCheck = "testdep@cto.test";
		DepartmentAdresseeFilter filter = new DepartmentAdresseeFilter();
		filter.setDepartment("TEST DEP");
		filter.setExactDepartmentMatch(true);
		List<DepartmentAdresseeDO> list = service.searchDepartmentAdressees(filter);
		Assert.assertTrue(!list.isEmpty());
		DepartmentAdresseeDO found = list.iterator().next();
		Assert.assertTrue(found != null);
		Assert.assertTrue(found.getAddress().equals(toCheck));
	}

	@Test
	@Commit
	public final void shouldListZeroDepartmentAddresses() {
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
		executeSqlScript(BASE_SQL_PATH + "insert_addressee.sql", false);
		DepartmentAdresseeFilter filter = new DepartmentAdresseeFilter();
		filter.setDepartment("TEST");
		filter.setExactDepartmentMatch(true);
		List<DepartmentAdresseeDO> list = service.searchDepartmentAdressees(filter);
		Assert.assertTrue(list.isEmpty());
	}

	@Test
	@Commit
	public final void shouldListZeroDepartmentAddressesWithNoExactMatch() {
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
		executeSqlScript(BASE_SQL_PATH + "insert_addressee.sql", false);
		DepartmentAdresseeFilter filter = new DepartmentAdresseeFilter();
		filter.setDepartment("TTEST");
		filter.setExactDepartmentMatch(false);
		List<DepartmentAdresseeDO> list = service.searchDepartmentAdressees(filter);
		Assert.assertTrue(list.isEmpty());
	}

	@Test
	@Commit
	public final void shouldListOneDepartmentAddressesWithNoExactMatch() {
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
		executeSqlScript(BASE_SQL_PATH + "insert_addressee.sql", false);
		String toCheck = "testdep@cto.test";
		DepartmentAdresseeFilter filter = new DepartmentAdresseeFilter();
		filter.setDepartment("TEST");
		filter.setExactDepartmentMatch(false);
		List<DepartmentAdresseeDO> list = service.searchDepartmentAdressees(filter);
		Assert.assertTrue(!list.isEmpty());
		DepartmentAdresseeDO found = list.iterator().next();
		Assert.assertTrue(found != null);
		Assert.assertTrue(found.getAddress().equals(toCheck));
	}

	@Test
	@Commit
	public final void shouldWriteFailure() {
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
		executeSqlScript(BASE_SQL_PATH + "insert.sql", false);
		BookingDocumentDO docDO = service.writeBookingDocumentFailure("TEST", "500", "NullpointerException");
		Assert.assertTrue(docDO != null);
		Assert.assertTrue(docDO.getFailureDate() != null);
		Assert.assertTrue(docDO.getFailureCode().equals("500"));
		Assert.assertTrue(docDO.getFailureReason().equals("NullpointerException"));

	}

	@Test
	public void testBase64Encoding() throws IOException {
		String bPath = "src/test/resources/it/eng/areas/to/sdoto/docservice/test-context/";
		byte[] bytes = DocumentTransactionalServiceTestHelper.readDocumentFromFile(bPath + "pdf/file.pdf");
		byte[] base64Encoded = Base64Utils.encode(bytes);
		System.out.println(new String(base64Encoded));
	}

	@AfterClass
	public static void shutdownDb() {
		db.shutdown();
	}

}
