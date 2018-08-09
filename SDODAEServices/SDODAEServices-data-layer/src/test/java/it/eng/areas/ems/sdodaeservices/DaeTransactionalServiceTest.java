package it.eng.areas.ems.sdodaeservices;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.areas.ems.sdodaeservices.dao.DaeDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.DaeDO;
import it.eng.areas.ems.sdodaeservices.service.DaeTransactionalService;


@RunWith(SpringJUnit4ClassRunner.class)
//@TransactionConfiguration(defaultRollback = true)
//@ContextConfiguration(locations = "classpath:it/eng/areas/ems/sdodaeservices/test-context/test-ctx.xml")
@ContextConfiguration(classes = {DaeTransactionalService.class, DaeDAO.class} )
@Ignore
public class DaeTransactionalServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	
	
	// private static EmbeddedDatabase db;

	@Autowired
	private DaeTransactionalService service;

	@BeforeClass
	public static void createDb() {
		
		/*
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
		
	*/
		
	
	}

	@Test
	public final void testGetDAE() {
		
		DaeDO dae = service.getDaeById(DaeDeepDepthRule.NAME,"12345678");
		Assert.assertNotNull(dae);
	}
	
	@AfterClass
	public static void shutdownDb(){
	//	db.shutdown();
	}

}
