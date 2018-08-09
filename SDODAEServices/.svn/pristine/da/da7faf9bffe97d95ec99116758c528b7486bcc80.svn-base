/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.DaeDO;
import it.eng.areas.ems.sdodaeservices.service.AnotherTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.DaeTransactionalService;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestCtx.class)
public class TransactionManagementConfigurationTest extends AbstractTransactionalJUnit4SpringContextTests {

	private final static String BASE_SQL_PATH = "classpath:/it/eng/areas/ems/common/sdo/test-context/common/sql/";

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Autowired
	private DaeTransactionalService service;

	@Autowired
	private AnotherTransactionalService anotherService;

	private static EmbeddedDatabase db;

	@BeforeClass
	public static void createCommonDb() {

		/*
		 * String staticBase =
		 * "it/eng/areas/ems/common/sdo/test-context/common/sql/"; String
		 * dynBase = "it/eng/areas/ems/common/sdo/test-context/common/sql/";
		 * EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		 * builder.setName("testdb"); db =
		 * builder.setType(EmbeddedDatabaseType.HSQL) // .H2 // or // .DERBY
		 * .setName("testdb")// .addScript(staticBase + "schema.sql")// //
		 * .addScript(staticBase + "insert.sql")// .build();
		 */
	}

	@Before

	public final void prepareTestData() {
		// executeSqlScript(BASE_SQL_PATH + "insert.sql", false);
	}

	@After
	public final void cleanTestData() {
		// executeSqlScript(BASE_SQL_PATH + "delete.sql", false);
	}

	@Test
	@Commit
	public final void testGetExampleById() {
		// executeSqlScript(BASE_SQL_PATH + "insert.sql", true);
		// String id = "test";
		DaeDO dae = service.getDaeById(DaeDeepDepthRule.NAME, "12345678");
		Assert.assertTrue(dae != null);
		// Assert.assertTrue(example.getCol1().equals("TEST"));
		// executeSqlScript(BASE_SQL_PATH + "delete.sql", false);
	}

	@Test
	@Commit
	public final void testUpdateExample() throws InterruptedException {
		// executeSqlScript(BASE_SQL_PATH + "insert.sql", true);
		// int count = countRowsInTable("EXAMPLE");
		// Assert.assertTrue(count > 0);

		DaeDO dae = service.getDaeById(DaeDeepDepthRule.NAME, "12345678");
		Assert.assertTrue(dae != null);
		dae.setAlias("UPDATE");
		dae = anotherService.update(dae);
		Assert.assertTrue(dae != null);
		Assert.assertTrue(dae.getAlias().equals("UPDATE"));
	}

	@AfterClass
	public static void shutdownDb() {
		// db.shutdown();
	}

}
