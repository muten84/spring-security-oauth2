package it.eng.areas.ems.sdodaeservices.persistence;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.areas.ems.sdodaeservices.dao.DaeDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.DaeDO;
import it.eng.areas.ems.sdodaeservices.service.DaeTransactionalService;

@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
@ContextConfiguration(classes = DaePersistenceTestCtx.class)
public class DaePersistenceServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

	private static EmbeddedDatabase db;

	private final static String BASE_SQL_PATH = "classpath:it/eng/areas/ems/sdodaeservices/test-context/common/sql/";

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Autowired
	private DaeTransactionalService service;

	@Autowired
	private DaeDAO dao;

	public static void createCommonDb() {
		/*
		 * String staticBase =
		 * "it/eng/areas/ems/sdodaeservices/test-context/common/sql/"; String
		 * dynBase = "it/eng/areas/ems/sdodaeservices/test-context/common/sql/";
		 * EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		 * builder.setName("testdb"); db =
		 * builder.setType(EmbeddedDatabaseType.HSQL) // .H2 // or // .DERBY
		 * .setName("testdb")// .addScript(staticBase + "schema.sql")// //
		 * .addScript(staticBase + "insert.sql")// .build();
		 */
	}

	@Test
	@Rollback
	public final void findDuplicate() {
		try {
			DaeDO daeDO = new DaeDO();

			List<String> duplicateDaeFieldList = Arrays.asList("matricola", "modello", "posizione.comune.nomeComune",
					"posizione.indirizzo.name");

			List<DaeDO> daes = dao.findDuplicate(daeDO, duplicateDaeFieldList);
			Assert.assertTrue(daes != null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Test
	@Rollback
	public final void testGetExampleById() {
		// executeSqlScript(BASE_SQL_PATH + "insert.sql", false);

		DaeDO dae = service.getDaeById(DaeDeepDepthRule.NAME, "12345678");
		Assert.assertTrue(dae != null);
		// Assert.assertTrue(dae.getAlias().equals("1"));
	}

	@Test
	@Rollback
	public final void CountAll() {
		// executeSqlScript(BASE_SQL_PATH + "insert.sql", false);

		long num = service.countAll();
		Assert.assertTrue(num > 0);

	}

	@Test
	@Rollback
	@Ignore
	public final void countAllGEO() {
		// executeSqlScript(BASE_SQL_PATH + "insert.sql", false);

		long num = service.getAllVctDae().size();
		Assert.assertTrue(num > 0);

	}

	@AfterClass
	public static void shutdownDb() {
		// db.shutdown();
	}

}
