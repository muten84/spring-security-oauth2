package it.eng.areas.ems.ordinari.test;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:it.eng.areas.ems.ordinari.test/test-ctx.xml")
public abstract class AbstractOrdinariDAOTests  extends AbstractTransactionalJUnit4SpringContextTests {

	protected final String BASE_PATH = "classpath:it.eng.areas.ems.ordinari.test/";
	protected final String BASE_SQL_PATH = BASE_PATH + "common/sql/";
	protected static String staticBase = "it.eng.areas.ems.ordinari.test/common/sql/";
	protected static String sqlSchema = "schema.sql";
	protected static String sqlInsert = "insert.sql";
	protected static String sqlClean  = "clean.sql";

	
	
	protected static EmbeddedDatabase db;
	
	@BeforeClass
	public static void createCommonDb() {
		
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		builder.setName("testdb");
		db = builder.setType(EmbeddedDatabaseType.HSQL) // .H2
														// or
														// .DERBY
				.setName("testdb")//
				.addScript(staticBase + sqlSchema)//
//				.addScript(staticBase + sqlInsert)//
				.build();
	}

	@Ignore
	@Test
    public void avoidNoRunnableTests() {
		Assert.assertTrue(true); // do nothing;
    }
	

	
	@AfterClass
	public static void shutdownDb() {
		db.shutdown();
	}
	
	
}
