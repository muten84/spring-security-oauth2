package it.eng.areas.ems.sdodaeservices.test;

import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.eng.areas.ems.sdodaeservices.dao.impl.rule.StradeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.CategoriaFr;
import it.eng.areas.ems.sdodaeservices.delegate.model.Comune;
import it.eng.areas.ems.sdodaeservices.delegate.model.Config;
import it.eng.areas.ems.sdodaeservices.delegate.model.EnteCertificatore;
import it.eng.areas.ems.sdodaeservices.delegate.model.Professione;
import it.eng.areas.ems.sdodaeservices.delegate.model.Provincia;
import it.eng.areas.ems.sdodaeservices.delegate.model.Ruolo;
import it.eng.areas.ems.sdodaeservices.delegate.model.Stato;
import it.eng.areas.ems.sdodaeservices.delegate.model.Strade;
import it.eng.areas.ems.sdodaeservices.delegate.model.TipoStruttura;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.ComuneFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.StradeFilter;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTComuniProvinceDO;
import it.eng.areas.ems.sdodaeservices.service.AnagraficheTransactionalService;

@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations =
// "classpath:it/eng/areas/ems/sdoanagraficaservices/test-context/test-ctx.xml")
@ContextConfiguration(classes = { AnagraficheDelegateTestCtx.class })
@ActiveProfiles(profiles = "oracle")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AnagraficheDelegateTest extends AbstractJUnit4SpringContextTests {

	String lastID = "";

	@Autowired
	protected AnagraficheDelegateService anagraficaDelegateService;

	@Autowired
	private AnagraficheTransactionalService service;

	@Test
	public void testProvinceByAreaForEmilia() {
		String[] area = new String[] { "BO", "MO", "FE", "PC", "PR", "FC", "RA", "RE", "RI" };

		List<Provincia> list = anagraficaDelegateService.getProvinceByArea(area);
		Assert.assertTrue("Size is: " + list.size(), list.size() == area.length);

	}

	@Test
	public void testConfig() {
		String param = anagraficaDelegateService.getParameter("DISTANCE_THREESHOLD");
		Assert.assertTrue(param.length() > 0);
	}

	@Test
	public void AgetComuniByFilter() {

		ComuneFilter comuneFilter = new ComuneFilter();

		comuneFilter.setNomeComune("cona");

		List<Comune> comuniList = anagraficaDelegateService.getComuniByFilter(comuneFilter);

		Assert.assertNotNull(comuniList);
		Assert.assertTrue(!comuniList.isEmpty());

	}

	@Test
	public void A1getAllCategoria() {

		List<CategoriaFr> comuniList = anagraficaDelegateService.getAllCategoriaFR();

		Assert.assertNotNull(comuniList);
		Assert.assertTrue(!comuniList.isEmpty());

	}

	@Test
	public void BgetAllProvince() {

		List<Provincia> stradeList = anagraficaDelegateService.getAllProvince();

		Assert.assertNotNull(stradeList);
		Assert.assertTrue(!stradeList.isEmpty());

	}

	@Test
	public void CgetAllTipoStruttura() {

		List<TipoStruttura> tipoStrutturaList = anagraficaDelegateService.getAllTipoStruttura();

		Assert.assertNotNull(tipoStrutturaList);
		Assert.assertTrue(!tipoStrutturaList.isEmpty());

	}

	@Test
	public void DgetStrdeByFilter() {

		StradeFilter stradeFilter = new StradeFilter();

		stradeFilter.setCodiceIstatComune("040036");
		stradeFilter.setName("Nazionale");
		stradeFilter.setFetchRule(StradeDeepDepthRule.NAME);

		List<Strade> stradeList = anagraficaDelegateService.searchStradeByFilter(stradeFilter);

		Assert.assertNotNull(stradeList);
		Assert.assertTrue(!stradeList.isEmpty());

	}

	@Test
	public void EgetAllStradeBologna() {

		StradeFilter filter = new StradeFilter();
		filter.setCodiceIstatComune("037011");
		filter.setFetchRule(StradeDeepDepthRule.NAME);
		List<Strade> stradeList = anagraficaDelegateService.searchStradeByFilter(filter);

		Assert.assertNotNull(stradeList);
		Assert.assertTrue(!stradeList.isEmpty());

	}

	@Test
	public void FtestCrudRuolo() {

		Ruolo ruolo = new Ruolo();
		ruolo.setId("3");
		ruolo.setNome("VISUALIZZATORE");
		ruolo.setDescrizione("Visualizzatore");

		Ruolo returnedRuolo = anagraficaDelegateService.insertRuolo(ruolo);

		lastID = returnedRuolo.getId();
		Assert.assertNotNull(lastID);

		String id = lastID;
		ruolo = anagraficaDelegateService.getRuoloById(id);

		Assert.assertNotNull(ruolo);

		boolean del = anagraficaDelegateService.deleteRuoloById(id);
		Assert.assertTrue(del);

	}

	@Test
	public void GgetAllRuolo() {

		List<Ruolo> ruoloList = anagraficaDelegateService.getAllRuolo();

		Assert.assertNotNull(ruoloList);
		Assert.assertTrue(!ruoloList.isEmpty());

	}

	@Test
	public void HgetAllProfessione() {
		List<Professione> professioneList = anagraficaDelegateService.getAllProfessione();

		Assert.assertNotNull(professioneList);
		Assert.assertTrue(!professioneList.isEmpty());

	}

	@Test
	public void IgetAllEnteCertificatore() {

		List<EnteCertificatore> enteCertificatoreList = anagraficaDelegateService.getAllEnteCertificatore();

		Assert.assertNotNull(enteCertificatoreList);
		Assert.assertTrue(!enteCertificatoreList.isEmpty());

	}

	@Test
	public void KgetAllStato() {

		List<Stato> statoList = anagraficaDelegateService.getAllStato();

		Assert.assertNotNull(statoList);
		Assert.assertTrue(!statoList.isEmpty());

	}

	@Test
	public void getAllConfiguration() {
		try {
			List<Config> configs = anagraficaDelegateService.getAllConfiguration();

			Assert.assertNotNull(configs);
			Assert.assertTrue(!configs.isEmpty());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
