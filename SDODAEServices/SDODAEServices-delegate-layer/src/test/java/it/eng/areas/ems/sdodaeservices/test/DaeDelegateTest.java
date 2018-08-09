package it.eng.areas.ems.sdodaeservices.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

import it.eng.areas.ems.sdodaeservices.dao.GruppoDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.GruppoDAOImpl;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.DaeDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.CertificatoDae;
import it.eng.areas.ems.sdodaeservices.delegate.model.Comune;
import it.eng.areas.ems.sdodaeservices.delegate.model.Dae;
import it.eng.areas.ems.sdodaeservices.delegate.model.Disponibile;
import it.eng.areas.ems.sdodaeservices.delegate.model.DisponibilitaEccezioni;
import it.eng.areas.ems.sdodaeservices.delegate.model.DisponibilitaGiornaliera;
import it.eng.areas.ems.sdodaeservices.delegate.model.DisponibilitaSpecifica;
import it.eng.areas.ems.sdodaeservices.delegate.model.GPSLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.Image;
import it.eng.areas.ems.sdodaeservices.delegate.model.Manutentore;
import it.eng.areas.ems.sdodaeservices.delegate.model.ProgrammaManutenzione;
import it.eng.areas.ems.sdodaeservices.delegate.model.Provincia;
import it.eng.areas.ems.sdodaeservices.delegate.model.Responsabile;
import it.eng.areas.ems.sdodaeservices.delegate.model.Strade;
import it.eng.areas.ems.sdodaeservices.delegate.model.TipoStruttura;
import it.eng.areas.ems.sdodaeservices.delegate.model.VctDAE;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.DaeFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.LocationFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.StradeFilter;
import it.eng.areas.ems.sdodaeservices.entity.DaeStatoEnum;
import it.eng.areas.ems.sdodaeservices.entity.GiornoSettimanaEnum;
import it.eng.areas.ems.sdodaeservices.entity.gis.VctDaeDODistanceBean;
import it.eng.areas.ems.sdodaeservices.exception.DaeDuplicateException;
import it.eng.areas.ems.sdodaeservices.service.DaeTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.GruppoTransactionalService;
import it.eng.areas.ems.sdodaeservices.service.impl.GruppoTransactionalServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DaeDelegateTestCtx.class })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DaeDelegateTest extends AbstractJUnit4SpringContextTests {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	String lastID = "";

	@Autowired
	protected DaeDelegateService daeDelegateService;

	@Autowired
	protected AnagraficheDelegateService anagraficaDelegateService;

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private DaeTransactionalService service;

	private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

	@Test
	public void getPagedVctDAEOrderByDistance() {
		try {
			DaeFilter filter = new DaeFilter();
			filter.setLocation(new LocationFilter());
			filter.getLocation().setSrid(4326);
			filter.getLocation().setGeoJSON(
					"{\"type\":\"Polygon\",\"coordinates\":[[[10.310668945312502,44.1151978766043],[10.310668945312502,45.1394300814679],[12.079467773437502,45.1394300814679],[12.079467773437502,44.1151978766043],[10.310668945312502,44.1151978766043]]]}");

			List<VctDaeDODistanceBean> list = daeDelegateService.getPagedVctDAEOrderByDistance(filter);
			Assert.assertEquals(9, list.size());
		} catch (Exception e) {
			Assert.fail();
			e.printStackTrace();
		}
	}

	@Test
	public void testSearchDae() {
		try {
			DaeFilter filter = new DaeFilter();
			filter.setLocation(new LocationFilter());

			filter.setOperativo(true);
			filter.setStatoValidazione(DaeStatoEnum.VALIDATO);
			filter.setIsInFault(false);

			filter.setPageSize(100);
			filter.setFromIndex(0);

			filter.getLocation().setSrid(4326);
			filter.getLocation().setLongitudine((float) 10.310668945312502);
			filter.getLocation().setLatitudine((float) 44.1151978766043);

			// filter.getLocation().setGeoJSON(
			// "{\"type\":\"Polygon\",\"coordinates\":[[[10.310668945312502,44.1151978766043],[10.310668945312502,45.1394300814679],[12.079467773437502,45.1394300814679],[12.079467773437502,44.1151978766043],[10.310668945312502,44.1151978766043]]]}");

			List<Dae> list = daeDelegateService.searchDaeByFilter(filter);
			Assert.assertEquals(9, list.size());
		} catch (Exception e) {
			Assert.fail();
			e.printStackTrace();
		}
	}

	@Test
	public void getPagedVctDAEOrderByDistance2() {
		try {
			DaeFilter filter = new DaeFilter();
			filter.setLocation(new LocationFilter());
			filter.getLocation().setSrid(4326);
			filter.setPageSize(10);
			filter.setFromIndex(5);
			filter.getLocation().setLatitudine((float) 11);
			filter.getLocation().setLongitudine((float) 41);

			List<VctDaeDODistanceBean> list = daeDelegateService.getPagedVctDAEOrderByDistance(filter);

			Assert.assertEquals(5, list.size());

		} catch (Exception e) {
			Assert.fail();
			e.printStackTrace();
		}
	}

	@Test
	public void synchGisDATA() {
		try {
			boolean result = daeDelegateService.synchGisDATA();
			Assert.assertTrue(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	public void testCrudDAE() throws ParseException, DaeDuplicateException {

		Dae dae = createDae("TEST_DATE_IDS");

		Dae returnedDae = daeDelegateService.insertDae(dae, null, false);

		lastID = returnedDae.getId();
		Assert.assertNotNull(lastID);

		String id = lastID;
		dae = daeDelegateService.getDaeById(id);

		Assert.assertNotNull(dae);

		boolean del = daeDelegateService.deleteDaeById(id);
		Assert.assertTrue(del);

	}

	@Test
	public void BgetAllDAE() throws Exception {

		List<Dae> daeList = daeDelegateService.getAllDAE(DaeDeepDepthRule.NAME);
		System.out.println(daeList.size());
		Assert.assertNotNull(daeList);
		Assert.assertTrue(!daeList.isEmpty());

	}

	@Bean
	public GruppoTransactionalService getGruppoTransactionalService() {
		return new GruppoTransactionalServiceImpl();
	}

	@Bean
	public GruppoDAO getGruppoDAO() {
		return new GruppoDAOImpl();
	}

	@Test
	public void DgetDAEByFilter() {

		DaeFilter daeFilter = new DaeFilter();

		// daeFilter.setId("4028dbc357d3475a0157d34764a90007");

		// daeFilter.setModello("AEDPLUS");

		daeFilter.setScadenzaDae(getCalendar(2020, 11, 11, 11, 11, 11));

		// daeFilter.setComune("ROMA");

		List<Dae> daeList;
		try {
			daeList = daeDelegateService.searchDaeByFilter(daeFilter);
			Assert.assertNotNull(daeList);
			Assert.assertTrue(!daeList.isEmpty());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Ignore
	public void DgetDAEByFilterScadenza() throws Exception {

		DaeFilter daeFilter = new DaeFilter();

		daeFilter.setScadenzaManutenzioneDa(getDate(2010, 01, 01, 00, 00, 00));
		daeFilter.setScadenzaManutenzioneA(getDate(2020, 11, 11, 11, 11, 11));

		// daeFilter.setComune("ROMA");

		List<Dae> daeList = daeDelegateService.searchDaeByFilter(daeFilter);
		Assert.assertNotNull(daeList);
		Assert.assertTrue(!daeList.isEmpty());

	}

	// @Ignore
	@Test
	public void saceVctDAE() {
		VctDAE vctDae = new VctDAE();
		vctDae.setDescrizione("DESRCIZIONE 1");
		vctDae.setId("12345");
		vctDae.setLoc((Point) wktToGeometry("POINT(6 10)"));

		VctDAE saved = daeDelegateService.saveVctDae(vctDae);
		Assert.assertNotNull(saved);
		// List<VctDAE> daeList = daeDelegateService.getAllVctDae();
		// Assert.assertNotNull(daeList);
		// Assert.assertTrue(!daeList.isEmpty());

	}

	private Geometry wktToGeometry(String wktPoint) {
		WKTReader fromText = new WKTReader();
		Geometry geom = null;
		try {
			geom = fromText.read(wktPoint);
		} catch (Exception e) {
			throw new RuntimeException("Not a WKT string:" + wktPoint);
		}
		return geom;
	}

	private Dae createDae(String id) throws ParseException {

		Dae dae = new Dae();
		dae.setId(id);
		dae.setAlias("ALIAS1");
		dae.setMatricola("matricola1");
		dae.setModello("modello1");
		dae.setNomeSede("nomeSede1");
		dae.setNotediAccessoallaSede("notediAccessoallaSede1");
		dae.setNoteGenerali("noteGenerali1");
		dae.setOperativo(true);
		Calendar scadenzaDae1 = getCalendar(2016, 10, 12, 11, 11, 11);
		dae.setScadenzaDae(scadenzaDae1);
		dae.setTipo("tipo1");
		dae.setUbicazione("ubicazione1");

		dae.setResponsabile(createResponsabile());

		dae.setTipologiaStruttura(createTipologiaStruttura());

		dae.setImmagine(CreateImmagine());

		// dae.setCertificatoDae(createCertificatoDae());

		dae.setGpsLocation(createGpsLocation());

		dae.setProgrammiManutenzione(CreateProgrammiManutenzione(dae));

		dae.setDisponibilita(CreateDisponibilita(dae));

		return dae;
	}

	private GPSLocation createGpsLocation() {

		GPSLocation gpsLocation = new GPSLocation();

		gpsLocation.setAltitudine(22.2f);
		gpsLocation.setComune(createComune());
		gpsLocation.setIndirizzo(getStrade());
		gpsLocation.setCivico("22A");
		gpsLocation.setError(1.2f);
		gpsLocation.setLatitudine(134.4f);
		gpsLocation.setLongitudine(1323.5f);
		gpsLocation.setLuogoPubblico("Bar Tre Stelle");
		gpsLocation.setTipoLuogoPubblico("BAR");

		return gpsLocation;

	}

	private Strade getStrade() {

		StradeFilter stradeFilter = new StradeFilter();

		/*
		 * AnagraficheDelegateTest anagraficheDelegateTest = new
		 * AnagraficheDelegateTest();
		 * appContext.getAutowireCapableBeanFactory().autowireBean(
		 * anagraficheDelegateTest); ;
		 * 
		 * anagraficheDelegateTest.DgetStrdeByFilter();
		 */

		stradeFilter.setCodiceIstatComune("040036");
		stradeFilter.setName("Nazionale");

		List<Strade> stradeList = anagraficaDelegateService.searchStradeByFilter(stradeFilter);

		return stradeList.get(0);
	}

	private List<Disponibile> CreateDisponibilita(Dae dae) throws ParseException {
		// TODO Auto-generated method stub
		Disponibile disponibile = new Disponibile();
		List<Disponibile> disponibilehs = new ArrayList<Disponibile>();
		disponibile.setDae(dae);
		disponibile.setA(format.parse("11:11:11 12/10/2016"));
		disponibile.setDa(format.parse("11:11:11 12/10/2016"));
		disponibile.setOrarioA("24:00");
		disponibile.setOrarioDa("00:00");
		// disponibile.setDisponibilitaEccezioni(CreateDisponibilitaEccezioni(disponibile));
		// disponibile.setDisponibilitaSpecifica(CreateDisponibilitaSpecifica(disponibile));

		disponibilehs.add(disponibile);
		return disponibilehs;
	}

	private DisponibilitaSpecifica CreateDisponibilitaSpecifica() {
		DisponibilitaSpecifica disponibilitaSpecifica = new DisponibilitaSpecifica();
		disponibilitaSpecifica.setDisponibilitaGiornaliera(CreateDisponibilitaGiornaliere());
		return disponibilitaSpecifica;
	}

	private List<DisponibilitaGiornaliera> CreateDisponibilitaGiornaliere() {

		DisponibilitaGiornaliera disponibilitaGiornaliera = new DisponibilitaGiornaliera();

		List<DisponibilitaGiornaliera> disponibilitaGiornalierahs = new ArrayList<DisponibilitaGiornaliera>();

		disponibilitaGiornaliera.setGiornoSettimana(GiornoSettimanaEnum.DOMENICA);
		disponibilitaGiornaliera.setOrarioDa("08,30");
		disponibilitaGiornaliera.setOrarioA("20,00");

		disponibilitaGiornalierahs.add(disponibilitaGiornaliera);
		return disponibilitaGiornalierahs;
	}

	private DisponibilitaEccezioni CreateDisponibilitaEccezioni(Disponibile disponibile) {

		DisponibilitaEccezioni disponibilitaEccezioni = new DisponibilitaEccezioni();
		disponibilitaEccezioni.setData(getCalendar(2016, 10, 12, 11, 11, 11));
		disponibilitaEccezioni.setMotivazione("Santo Patrono");
		disponibilitaEccezioni.setIdDisponibilita(disponibile.getId());

		return null;
	}

	private List<ProgrammaManutenzione> CreateProgrammiManutenzione(Dae dae) {

		List<ProgrammaManutenzione> programmaManutenzioneHS = new ArrayList<ProgrammaManutenzione>();
		ProgrammaManutenzione programmaManutenzione1 = new ProgrammaManutenzione();
		ProgrammaManutenzione programmaManutenzione2 = new ProgrammaManutenzione();
		programmaManutenzione1.setDae(dae);
		programmaManutenzione1.setDurata(2);
		programmaManutenzione1.setTipoManutenzione("tipoManutenzione1");
		programmaManutenzione1.setIntervallotraInterventi(10);
		programmaManutenzione1.setNota("2");
		programmaManutenzione1.setResponsabile(CreateManutentore("pippo", "pluto"));
		programmaManutenzione1.setScadenzaDopo(getDate(2016, 11, 10, 12, 12, 11));
		programmaManutenzione1.setStato("2");
		programmaManutenzione1.setTipoManutenzione("Revisione Generale");

		programmaManutenzione2.setDurata(2);
		programmaManutenzione2.setTipoManutenzione("tipoManutenzione2");
		programmaManutenzione2.setIntervallotraInterventi(10);
		programmaManutenzione2.setNota("2");
		programmaManutenzione2.setResponsabile(CreateManutentore("paperino", "topolino"));
		programmaManutenzione2.setScadenzaDopo(getDate(2016, 11, 10, 12, 12, 11));
		programmaManutenzione2.setStato("2");
		programmaManutenzione2.setTipoManutenzione("Revisione Generale");
		programmaManutenzione2.setDae(dae);

		programmaManutenzioneHS.add(programmaManutenzione1);
		programmaManutenzioneHS.add(programmaManutenzione2);

		return programmaManutenzioneHS;
	}

	private Manutentore CreateManutentore(String cognome, String nome) {

		Manutentore manutentore = new Manutentore();
		manutentore.setCognome(cognome);
		manutentore.setIndirizzo("Via dei Crisantemi");
		manutentore.setNome(nome);
		manutentore.setEmail("email@manutentori@org");
		manutentore.setTel("3335590341");
		return manutentore;
	}

	private GPSLocation CreateGpsLocation() {
		GPSLocation gpslocation = new GPSLocation();
		gpslocation.setAltitudine(123f);
		gpslocation.setComune(createComune());
		gpslocation.setError(12.3f);
		gpslocation.setLatitudine(213f);
		gpslocation.setIndirizzo(getStrade());
		gpslocation.setLongitudine(123.3f);
		gpslocation.setTimeStamp(getCalendar(2016, 12, 12, 12, 12, 12).getTime());

		return gpslocation;
	}

	private CertificatoDae createCertificatoDae() {

		CertificatoDae certificatoDae = new CertificatoDae();

		certificatoDae.setDataConseguimento(getDate(2016, 12, 12));
		certificatoDae.setDescrizione("descrizione Certificato Dae 1");
		certificatoDae.setEmailScadenza("emailScadenza1@gmail.com");
		certificatoDae.setRilasciatoDa("Universita 1");
		certificatoDae.setDataScadenza(getDate(2016, 12, 12));
		certificatoDae.setImmagine(CreateImmagine());

		return certificatoDae;
	}

	private Image CreateImmagine() {

		Image image = new Image();
		image.setData("http.//www.immaginestore.com/immagine1.jpg");
		return image;
	}

	private TipoStruttura createTipologiaStruttura() {

		TipoStruttura tipoStruttura = new TipoStruttura();

		tipoStruttura.setId("3");
		tipoStruttura.setDescrizione("Bar");

		return tipoStruttura;
	}

	private Responsabile createResponsabile() {

		Responsabile responsabile = new Responsabile();

		// responsabile.setId("1234");
		responsabile.setCodiceFiscale("codiceFiscale1");
		responsabile.setCognome("cognome1");
		responsabile.setComuneResidenza(createComune());
		responsabile.setIndirizzoResidenza("indirizzoResidenza1");
		responsabile.setEmail("emailresponsabile1@google.com");

		return responsabile;
	}

	private Comune createComune() {
		Comune comune = new Comune();
		comune.setId("BO_MUN_094023");
		comune.setCodiceIstat("094023");
		comune.setNomeComune("ISERNIA");
		Provincia prov = new Provincia();
		prov.setId("BO_PROV_094");
		prov.setNomeProvincia("IS");
		comune.setProvincia(prov);
		return comune;
	}

	private Date getDate(int year, int month, int day) {
		Calendar c = getCalendar(year, month, day, 0, 0, 0);
		return c.getTime();
	}

	private Date getDate(int year, int month, int day, int hour, int min, int sec) {
		Calendar c = getCalendar(year, month, day, hour, min, sec);
		return c.getTime();
	}

	private Calendar getCalendar(int year, int month, int day, int hour, int min, int sec) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, sec);

		return c;
	}

}
