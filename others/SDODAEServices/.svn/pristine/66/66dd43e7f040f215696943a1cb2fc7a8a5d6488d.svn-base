package it.eng.areas.ems.sdodaeservices.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.areas.ems.sdodaeservices.delegate.GruppoDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.Gruppo;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTGruppoDO;
import it.eng.areas.ems.sdodaeservices.service.GruppoTransactionalService;

@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations =
// "classpath:it/eng/areas/ems/sdogrupposervices/test-context/test-ctx.xml")
@ContextConfiguration(classes = { GruppoDelegateTestCtx.class })
@ActiveProfiles(profiles = "oracle")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GruppoDelegateTest extends AbstractJUnit4SpringContextTests {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	String lastID = "";

	@Autowired
	protected GruppoDelegateService gruppoDelegateService;

	@Autowired
	private GruppoTransactionalService service;

	@Test
	public void testCrudGruppo() {

		Gruppo gruppo = createGruppo();

		Gruppo returnedGruppo = gruppoDelegateService.insertGruppo(gruppo);

		lastID = returnedGruppo.getId();
		Assert.assertNotNull(lastID);

		String id = lastID;
		gruppo = gruppoDelegateService.getGruppoById(id);

		Assert.assertNotNull(gruppo);

		String idfr = "4028dbb557e5f1b40157e5f1cd110005";

		boolean del = gruppoDelegateService.deleteGruppoById(id);
		Assert.assertTrue(del);

	}

	@Test
	public void BgetAllGruppi() {

		List<Gruppo> gruppoList = gruppoDelegateService.getAllGruppo();

		Assert.assertNotNull(gruppoList);
		Assert.assertTrue(!gruppoList.isEmpty());

	}
	/*
	 * @Ignore public void CgetAllVCTDAE() {
	 * 
	 * List<VCTGruppoDO> gruppoList = gruppoDelegateService.getAllVctGruppo();
	 * 
	 * Assert.assertNotNull(gruppoList);
	 * Assert.assertTrue(!gruppoList.isEmpty());
	 * 
	 * }
	 * 
	 */

	private Gruppo createGruppo() {

		Gruppo gruppo = new Gruppo();

		gruppo.setId("123456789123456789");

		return gruppo;
	}

	private Gruppo addFirstResponder(Gruppo gruppo, FirstResponder firstResponder) {

		Set<FirstResponder> firstResponders = new HashSet<FirstResponder>();

		firstResponders.add(firstResponder);

		return gruppo;

	}

	private Set<Gruppo> createGruppi() {

		Gruppo gruppo = new Gruppo();
		Set<Gruppo> gruppi = new HashSet<Gruppo>();
		gruppi.add(gruppo);
		return gruppi;
	}

}
