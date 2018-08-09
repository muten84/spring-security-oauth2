/**
 * 
 */
package it.eng.areas.ems.ordinari.test.webidentity;

import it.eng.areas.ems.ordinari.dao.WebSessionDAO;
import it.eng.areas.ems.ordinari.entity.WebIdentityDO;
import it.eng.areas.ems.ordinari.entity.WebSessionDO;
import it.eng.areas.ems.ordinari.service.WebIdentityService;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Bifulco Luigi
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebIdentityPersistenceTestCtx.class })
@ActiveProfiles(profiles = "hsqldb") // possible profiles oracle, hsqldb
public class WebIdentityServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	private final static String BASE_PATH = "classpath:it.eng.areas.ems.ordinari.test/";

	private final static String BASE_SQL_PATH = BASE_PATH + "common/sql/";

	private static EmbeddedDatabase db;

	@Autowired
	private WebIdentityService service;

	@Autowired
	private WebSessionDAO sessionDAO;

	@BeforeClass
	public static void createCommonDb() {
		String staticBase = "it.eng.areas.ems.ordinari.test/common/sql/";
		String dynBase = "it.eng.areas.ems.ordinari.test/common/sql/";
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		builder.setName("testdb2");
		db = builder.setType(EmbeddedDatabaseType.HSQL) // .H2
														// or
														// .DERBY
				.setName("testdb2")//
				.addScript(staticBase + "schema.sql")//
				// .addScript(staticBase + "insert.sql")//
				.build();
	}

	@Test
	@Commit
	public final void testGetUser() {
		executeSqlScript(BASE_SQL_PATH + "insert_web_identity.sql", false);
		WebIdentityDO user = service.getUser("ADMIN");
		Assert.assertNotNull(user);
		Assert.assertTrue(user.getLogin().equals("ADMIN"));
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
	}

	@Test
	@Commit
	public final void testGetSession() {
		executeSqlScript(BASE_SQL_PATH + "insert_web_identity.sql", false);
		executeSqlScript(BASE_SQL_PATH + "insert_web_session.sql", false);
		String token = service.getUserSession("ADMIN");
		Assert.assertNotNull(token);
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
	}

	@Test
	@Commit
	public final void testCreateSession() {
		executeSqlScript(BASE_SQL_PATH + "insert_web_identity.sql", false);
		WebSessionDO session = service.createSession("ADMIN", "NEW_TOKEN");
		Assert.assertNotNull(session);
		Assert.assertNotNull(session.getSessionId());
		Assert.assertTrue(session.getSessionId().equals("NEW_TOKEN"));
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
	}

	@Test
	@Commit
	public final void testRecreateSession() {
		executeSqlScript(BASE_SQL_PATH + "insert_web_identity.sql", false);
		executeSqlScript(BASE_SQL_PATH + "insert_web_session.sql", false);
		sessionDAO.flushAndClear();
		WebSessionDO session = service.createSession("ADMIN", "NEW_TOKEN");
		Assert.assertNotNull(session);
		Assert.assertNotNull(session.getSessionId());
		Assert.assertTrue(session.getSessionId().equals("NEW_TOKEN"));
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
	}

	@Test
	@Commit
	public final void shouldNotReturnAneedToRenewIdentity() {
		executeSqlScript(BASE_SQL_PATH + "insert_web_identity.sql", false);
		WebIdentityDO identity = service.needToRenewPassword("ADMIN", 10);
		Assert.assertNotNull(identity);
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
	}
	
	@Test
	@Commit
	public final void shouldReturnAneedToRenewIdentity() {
		executeSqlScript(BASE_SQL_PATH + "insert_web_identity_renew.sql", false);
		WebIdentityDO identity = service.needToRenewPassword("ADMIN", 10);
		Assert.assertNotNull(identity);
		executeSqlScript(BASE_SQL_PATH + "clean.sql", false);
	}

	@AfterClass
	public static void shutdownDb() {
		db.shutdown();
	}

}
