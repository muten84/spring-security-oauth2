package it.eng.areas.ems.sdodaeservices;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import it.eng.areas.ems.sdodaeservices.entity.ExampleDO;
import it.eng.areas.ems.sdodaeservices.service.ExampleTransactionalService;

/**
 * @author Bifulco Luigi
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
@TransactionConfiguration(defaultRollback = true)
//@ContextConfiguration(locations = "classpath:it/eng/areas/ems/sdodaeservices/test-context/test-ctx.xml")
public class ExampleTransactionalServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	private static EmbeddedDatabase db;

	@Autowired
	private ExampleTransactionalService service;

	@BeforeClass
	public static void createCommonDb() {
		String staticBase = "it/eng/areas/ems/sdodaeservices/test-context/common/sql/";
		String dynBase = "it/eng/areas/ems/sdodaeservices/test-context/common/sql/";
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		builder.setName("testdb");
		db = builder.setType(EmbeddedDatabaseType.HSQL) // .H2
																			// or
																			// .DERBY
				.setName("testdb")//
				.addScript(staticBase + "schema.sql")//
			//	.addScript(staticBase + "insert.sql")//
				.build();
	}

	@Test
	public final void testGetExample() {
		ExampleDO example = service.getExampleById("TEST");
		//Assert.assertNotNull(example);
	}
	
	@AfterClass
	public static void shutdownDb(){
		db.shutdown();
	}

}
