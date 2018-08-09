package it.eng.areas.ems.sdodaeservices.test;

import java.util.Date;
import java.util.List;

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

import it.eng.areas.ems.sdodaeservices.delegate.CategoriaFrDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.CategoriaFr;
import it.eng.areas.ems.sdodaeservices.delegate.model.Colore;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTCategoriaFrDO;
import it.eng.areas.ems.sdodaeservices.service.CategoriaFrTransactionalService;

@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations =
// "classpath:it/eng/areas/ems/sdocategoriaFrservices/test-context/test-ctx.xml")
@ContextConfiguration(classes = { CategoriaFrDelegateTestCtx.class })
@ActiveProfiles(profiles = "oracle")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoriaFrDelegateTest extends AbstractJUnit4SpringContextTests {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	String lastID = "";

	@Autowired
	protected CategoriaFrDelegateService categoriaFrDelegateService;

	
	@Autowired
	private CategoriaFrTransactionalService service;

	
	@Test
	public void testCrudCategoriaFr() {

		CategoriaFr categoriaFr = createCategoriaFr();

		CategoriaFr returnedCategoriaFr = categoriaFrDelegateService.insertCategoriaFr(categoriaFr);

		lastID = returnedCategoriaFr.getId();
		Assert.assertNotNull(lastID);

		String id = lastID;
		categoriaFr = categoriaFrDelegateService.getCategoriaFrById(id);

		Assert.assertNotNull(categoriaFr);
		
		boolean del = categoriaFrDelegateService.deleteCategoriaFrById(id);
		Assert.assertTrue(del);

	}

	@Test
	public void BgetAllCategorie() {

		List<CategoriaFr> categoriaFrList = categoriaFrDelegateService.getAllCategoriaFr();

		Assert.assertNotNull(categoriaFrList);
		Assert.assertTrue(!categoriaFrList.isEmpty());

	}
/*
	@Ignore
	public void CgetAllVCTDAE() {

		List<VCTCategoriaFrDO> categoriaFrList = categoriaFrDelegateService.getAllVctCategoriaFr();

		Assert.assertNotNull(categoriaFrList);
		Assert.assertTrue(!categoriaFrList.isEmpty());

	}

	*/

	
	private CategoriaFr createCategoriaFr() {

		CategoriaFr categoriaFr = new CategoriaFr();

	
	Colore colore =  new Colore();
	colore.setNomeColore("Verde");
		
	categoriaFr.setColore("GREEN");	
	categoriaFr.setAccettaMinusKm(10);
	categoriaFr.setAccettatiPerIntervento(true);
	categoriaFr.setAggiornataIl(new Date());
	categoriaFr.setDescrizione("categoria DAE Luoghi pubblici");
	categoriaFr.setId("12345567891234567890");
			
		return categoriaFr;
	}
	

 	

	

}
