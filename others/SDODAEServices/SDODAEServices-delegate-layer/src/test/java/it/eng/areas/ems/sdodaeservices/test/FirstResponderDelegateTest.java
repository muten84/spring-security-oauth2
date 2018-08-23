package it.eng.areas.ems.sdodaeservices.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.CategoriaFr;
import it.eng.areas.ems.sdodaeservices.delegate.model.CertificatoFr;
import it.eng.areas.ems.sdodaeservices.delegate.model.Colore;
import it.eng.areas.ems.sdodaeservices.delegate.model.Comune;
import it.eng.areas.ems.sdodaeservices.delegate.model.DomandaQuestionario;
import it.eng.areas.ems.sdodaeservices.delegate.model.FRLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.Gruppo;
import it.eng.areas.ems.sdodaeservices.delegate.model.Image;
import it.eng.areas.ems.sdodaeservices.delegate.model.ProfiloSanitario;
import it.eng.areas.ems.sdodaeservices.delegate.model.Provincia;
import it.eng.areas.ems.sdodaeservices.delegate.model.Questionario;
import it.eng.areas.ems.sdodaeservices.delegate.model.QuestionarioFirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.RispostaQuestionarioFirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.Ruolo;
import it.eng.areas.ems.sdodaeservices.delegate.model.Strade;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.ComuneFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.FirstResponderFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.StradeFilter;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTFirstResponderDO;
import it.eng.areas.ems.sdodaeservices.service.FirstResponderTransactionalService;

@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations =
// "classpath:it/eng/areas/ems/sdofirstResponderservices/test-context/test-ctx.xml")
@ContextConfiguration(classes = { FirstResponderDelegateTestCtx.class })
@ActiveProfiles(profiles = "oracle")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FirstResponderDelegateTest extends AbstractJUnit4SpringContextTests {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	String lastID = "";

	@Autowired
	protected FirstResponderDelegateService firstResponderDelegateService;

	@Autowired
	protected AnagraficheDelegateService anagraficaDelegateService;

	@Autowired
	private FirstResponderTransactionalService service;

	private List<FirstResponder> firstResponders = null;

	@Test
	public void testFRtoNotify() {

		List<FirstResponder> frsToNotified = firstResponderDelegateService.getFirstResponderToBeNotified(0);

		frsToNotified.forEach(System.out::println);

	}

	@Test
	public void testRicercaPerProvincia() {
		List<Provincia> province = anagraficaDelegateService.getAllProvince();

		province.forEach(p -> {
			try {
				System.out.println("---------------> " + p.getNomeProvincia());

				FirstResponderFilter firstResponderFilter = new FirstResponderFilter();
				firstResponderFilter.setProvince(p.getNomeProvincia());

				List<FirstResponder> res = firstResponderDelegateService
						.searchFirstResponderByFilter(firstResponderFilter);

				System.out.println(res.size());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
	}

	@Test
	public void testRicercaPerComune() {
		ComuneFilter comuniFilter = new ComuneFilter();
		List<Comune> province = anagraficaDelegateService.getComuniByFilter(comuniFilter);

		province.forEach(p -> {
			try {
				System.out.println("---------------> " + p.getNomeComune());

				FirstResponderFilter firstResponderFilter = new FirstResponderFilter();
				firstResponderFilter.setComune(p.getNomeComune());

				List<FirstResponder> res = firstResponderDelegateService
						.searchFirstResponderByFilter(firstResponderFilter);

				System.out.println(res.size());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
	}

	@Test
	public void testCrudUtente() {
		Utente ut = createUtente();

		Utente savedUtent = firstResponderDelegateService.saveUtente(ut);
		lastID = savedUtent.getId();
		Assert.assertNotNull(lastID);

		String id = lastID;
		savedUtent = firstResponderDelegateService.getUtenteById(id);

		Assert.assertNotNull(savedUtent);

		Assert.assertNotNull(savedUtent.getRuoli());
		Assert.assertNotNull(savedUtent.getIndirizzo());
		Assert.assertNotNull(savedUtent.getComuneResidenza());
		boolean del = firstResponderDelegateService.deleteUtente(savedUtent);
		Assert.assertTrue(del);

	}

	@Test
	public void testCrudFirstResponder() {

		FirstResponder firstResponder = createFirstResponder();

		FirstResponder returnedFirstResponder = null;
		try {
			returnedFirstResponder = firstResponderDelegateService.saveFirstResponder(firstResponder, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		lastID = returnedFirstResponder.getId();
		Assert.assertNotNull(lastID);

		// firstResponderDelegateService.updateImmagineCertificato(lastID,
		// createImmagine().get);

		String id = lastID;
		firstResponder = firstResponderDelegateService.getFirstResponderById(FirstResponderDeepDepthRule.NAME, id);

		Assert.assertNotNull(firstResponder);

		boolean del = firstResponderDelegateService.deleteFirstResponderById(id);
		Assert.assertTrue(del);

	}

	/*
	 * public FirstResponder getFirsRespondebyId(String id){ FirstResponder
	 * firstResponder = firstResponderDelegateService.getFirstResponderById(id);
	 * return firstResponder; }
	 * 
	 */

	@Test
	public void BgetAllFirstResponder() {

		List<FirstResponder> firstResponderList = firstResponderDelegateService.getAllFirstResponder();

		Assert.assertNotNull(firstResponderList);
		Assert.assertTrue(!firstResponderList.isEmpty());

	}

	@Test
	public void CgetFilteredFirstResponder() {

		FirstResponderFilter firstResponderFilter = new FirstResponderFilter();

		firstResponderFilter.setId("prova");

		// firstResponderFilter.setNome("Geppo");
		// firstResponderFilter.setCognome("Rossi");
		// firstResponderFilter.setCategoriaFr("4028dbb557e5ed150157e5ed2d970000");

		List<FirstResponder> firstResponderList = firstResponderDelegateService
				.searchFirstResponderByFilter(firstResponderFilter);

		Assert.assertNotNull(firstResponderList);
		Assert.assertTrue(!firstResponderList.isEmpty());

	}

	@Test
	public void DupdatePositionFirstResponder() {

		FirstResponder firstResponder = firstResponderDelegateService
				.getFirstResponderById(FirstResponderDeepDepthRule.NAME, "prova");

		FRLocation gpsLocation = createGpsLocation();

		FirstResponder returnedFirstResponder = firstResponderDelegateService
				.updatePositionFirstResponderById(firstResponder.getId(), gpsLocation);

		Assert.assertNotNull(returnedFirstResponder);

	}

	/*
	 * @Test public void DtestCrudQuestionario() {
	 * 
	 * Questionario questionario = createQuestionario();
	 * 
	 * Questionario returnedQuestionario =
	 * firstResponderDelegateService.insertQuestionario(questionario);
	 * 
	 * lastID = returnedQuestionario.getId(); Assert.assertNotNull(lastID);
	 * 
	 * String id = lastID; questionario =
	 * firstResponderDelegateService.getQuestionarioById(id);
	 * 
	 * Assert.assertNotNull(questionario);
	 * 
	 * boolean del = firstResponderDelegateService.deleteQuestionarioById(id);
	 * Assert.assertTrue(del);
	 * 
	 * }
	 * 
	 */

	/*
	 * @Test public void EassegnaQuestionarioAFirstResponder() {
	 * 
	 * FirstResponderFilter firstResponderFilter = new FirstResponderFilter();
	 * 
	 * firstResponderFilter.setId("4028dbb257f6e1be0157f6e1d70d0007");
	 * 
	 * List<FirstResponder> firstResponderList = firstResponderDelegateService
	 * .getFirstResponderByFilter(firstResponderFilter);
	 * 
	 * FirstResponder firstResponder = firstResponderList.get(0);
	 * 
	 * Questionario questionario =
	 * firstResponderDelegateService.getQuestionarioById(
	 * "4028b881584da81f01584da83c3e0000");
	 * 
	 * Set<QuestionarioFirstResponder> questionariFirstResponder =
	 * assegnaQuestionarioAFirstResponder(firstResponder,questionario);
	 * 
	 * Assert.assertNotNull(questionariFirstResponder);
	 * Assert.assertTrue(!questionariFirstResponder.isEmpty());
	 * 
	 * 
	 * }
	 * 
	 */

	private Questionario createQuestionario() {

		Questionario questionario = new Questionario();
		questionario.setDescrizione("Test di conoscenza del DAE");

		Set<DomandaQuestionario> domandeQuestionario = new HashSet<DomandaQuestionario>();

		DomandaQuestionario domandaQuestionario = new DomandaQuestionario();
		domandaQuestionario.setDomanda("Cosa è un Defibrillatore Semiautomatico Esterno (DAE)?");
		domandaQuestionario.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario);

		DomandaQuestionario domandaQuestionario2 = new DomandaQuestionario();
		domandaQuestionario2.setDomanda("Come funziona un defibrillatore?");
		domandaQuestionario2.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario2);

		DomandaQuestionario domandaQuestionario3 = new DomandaQuestionario();
		domandaQuestionario3
				.setDomanda("Utilizzare il DAE è una manovra difficile oppure sono tutti in grado di farla?");
		domandaQuestionario3.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario3);

		DomandaQuestionario domandaQuestionario4 = new DomandaQuestionario();
		domandaQuestionario4.setDomanda("È necessario un corso per utilizzare il DAE?");
		domandaQuestionario4.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario4);

		DomandaQuestionario domandaQuestionario5 = new DomandaQuestionario();
		domandaQuestionario5.setDomanda(
				"Quanto dura un corso che insegna come utilizzare un DAE? Quali argomenti vengono trattati? Dove si svolgono?");
		domandaQuestionario5.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario5);

		DomandaQuestionario domandaQuestionario6 = new DomandaQuestionario();
		domandaQuestionario6.setDomanda(
				"Per utilizzare correttamente il DAE occorre essere guidati da parte degli operatori del 118?");
		domandaQuestionario6.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario6);

		DomandaQuestionario domandaQuestionario7 = new DomandaQuestionario();
		domandaQuestionario7.setDomanda(
				"Dopo quanto tempo dall’arresto cardiaco la vittima deve essere sottoposta alle pratiche di rianimazione?");
		domandaQuestionario7.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario7);

		DomandaQuestionario domandaQuestionario8 = new DomandaQuestionario();
		domandaQuestionario8.setDomanda(
				"Dopo quanto tempo dall’arresto cardiaco la vittima deve essere sottoposta alle pratiche di rianimazione?");
		domandaQuestionario8.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario8);

		DomandaQuestionario domandaQuestionario9 = new DomandaQuestionario();
		domandaQuestionario9.setDomanda("Quale legame intercorre tra defibrillazione e massaggio cardiaco?");
		domandaQuestionario9.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario9);

		DomandaQuestionario domandaQuestionario10 = new DomandaQuestionario();
		domandaQuestionario10.setDomanda("Il DAE è sempre efficace? Quanto è affidabile?");
		domandaQuestionario10.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario10);

		DomandaQuestionario domandaQuestionario11 = new DomandaQuestionario();
		domandaQuestionario11.setDomanda("Vi sono precauzioni da tenere presenti quando si utilizza il DAE?");
		domandaQuestionario11.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario11);

		DomandaQuestionario domandaQuestionario12 = new DomandaQuestionario();
		domandaQuestionario12.setDomanda("Dove posso trovare oggi un DAE?");
		domandaQuestionario12.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario12);

		DomandaQuestionario domandaQuestionario13 = new DomandaQuestionario();
		domandaQuestionario13.setDomanda("Come riconosco la presenza di un DAE?");
		domandaQuestionario13.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario13);

		DomandaQuestionario domandaQuestionario14 = new DomandaQuestionario();
		domandaQuestionario14.setDomanda("Cosa è la catena della sopravvivenza?");
		domandaQuestionario14.setTipoRisposta("text");
		domandeQuestionario.add(domandaQuestionario14);

		questionario.setDomandaQuestionario(domandeQuestionario);
		return questionario;
	}

	/*
	 * @Ignore public void CgetAllVCTDAE() {
	 * 
	 * List<VCTFirstResponderDO> firstResponderList =
	 * firstResponderDelegateService.getAllVctFirstResponder();
	 * 
	 * Assert.assertNotNull(firstResponderList);
	 * Assert.assertTrue(!firstResponderList.isEmpty());
	 * 
	 * }
	 * 
	 */

	private Utente createUtente() {

		Utente ut = new Utente();
		// firstResponder.setId("12345678");
		ut.setCodiceFiscale("DPPPPP88M16I533D");
		ut.setCognome("Rossi");
		ut.setEmail("GeppoRossi@gmail.com");
		ut.setIndirizzo(getStrade());
		ut.setLanguage("Inglese");
		ut.setLogon(UUID.randomUUID().toString());
		ut.setNome("Geppo");
		ut.setPasswordHash("234gei8u3rh398ue0909d09ufd03qeud093i0");
		List<Ruolo> ruolo = new ArrayList<Ruolo>();
		Ruolo r1 = new Ruolo();
		r1.setDescrizione("AMMINISTRATORE");
		r1.setId("1");
		r1.setNome("AMMINISTRATORE");
		ruolo.add(r1);
		ut.setRuoli(ruolo);
		ut.setStatoUtente("Attivo");
		ut.setComuneResidenza(createComune());

		return ut;
	}

	private FirstResponder createFirstResponder() {

		FirstResponder firstResponder = new FirstResponder();
		// firstResponder.setId("12345678");
		firstResponder.setInterventiEseguiti(22);
		firstResponder.setRichiesteEseguite(22);
		firstResponder.setNumCellulare("3332350531");

		firstResponder.setCivico("22/A");
		;
		firstResponder.setCodiceFiscale("DPPPPP88M16I533D");
		firstResponder.setCognome("Rossi");
		firstResponder.setEmail("GeppoRossi@gmail.com");
		firstResponder.setIndirizzo(getStrade());
		firstResponder.setLanguage("Inglese");

		firstResponder.setLogon(java.util.UUID.randomUUID().toString());
		firstResponder.setNome("Geppo");
		firstResponder.setPasswordHash("234gei8u3rh398ue0909d09ufd03qeud093i0");
		List<Ruolo> ruolo = new ArrayList<Ruolo>();
		Ruolo r1 = new Ruolo();
		r1.setDescrizione("First Responder");
		r1.setId("1");
		r1.setNome("FIRST_RESPONDER");
		ruolo.add(r1);
		firstResponder.setRuoli(ruolo);
		firstResponder.setStatoUtente("Attivo");
		firstResponder.setCategoriaFr(createCategoriaFr());
		firstResponder.setCertificatoFr(createCertificatoFirstResponder());
		firstResponder.setComuneResidenza(createComune());

		// firstResponder.setGruppo(createGruppi());
		firstResponder.setLastPosition(createGpsLocation());
		firstResponder.setProfiloSanitario(createProfiloSanitario());
		// firstResponder.setQuestionarioFirstResponder(assegnaQuestionarioAFirstResponder(firstResponder));

		return firstResponder;
	}

	private Set<QuestionarioFirstResponder> assegnaQuestionarioAFirstResponder(FirstResponder firstResponder,
			Questionario questionario) {

		Set<QuestionarioFirstResponder> questionariFirstResponder = new HashSet<QuestionarioFirstResponder>();
		Set<RispostaQuestionarioFirstResponder> risposteQuestionarioFirstResponder = new HashSet<RispostaQuestionarioFirstResponder>();

		Iterator<DomandaQuestionario> dqi = questionario.getDomandaQuestionario().iterator();

		while (dqi.hasNext()) {

			QuestionarioFirstResponder questionarioFirstResponder = new QuestionarioFirstResponder();
			RispostaQuestionarioFirstResponder rispostaQuestionarioFirstResponder = new RispostaQuestionarioFirstResponder();
			questionarioFirstResponder.setDeleted(false);
			// questionarioFirstResponder.setFirstResponder(firstResponder);
			questionariFirstResponder.add(questionarioFirstResponder);
			rispostaQuestionarioFirstResponder.setDomandaQuestionario(dqi.next());
			rispostaQuestionarioFirstResponder.setRisposta(null);
			questionariFirstResponder.add(questionarioFirstResponder);
			risposteQuestionarioFirstResponder.add(rispostaQuestionarioFirstResponder);

		}

		return questionariFirstResponder;
	}

	private ProfiloSanitario createProfiloSanitario() {
		ProfiloSanitario profiloSanitario = new ProfiloSanitario();

		profiloSanitario.setNumIscrizione("123456");
		profiloSanitario.setOrdine("Ordine dei Medici di Alessandria");

		return profiloSanitario;
	}

	private Set<Gruppo> createGruppi() {

		Gruppo gruppo = new Gruppo();
		Set<Gruppo> gruppi = new HashSet<Gruppo>();
		// gruppo.setUtenti(this.firstResponders);
		gruppi.add(gruppo);
		return gruppi;
	}

	private CategoriaFr createCategoriaFr() {

		CategoriaFr categoriaFr = new CategoriaFr();
		categoriaFr.setId("FIRST_RESPONDER");
		categoriaFr.setDescrizione("Categoria dei dae servizio in auto");

		return categoriaFr;
	}

	private Colore createColore() {
		Colore colore = new Colore();
		colore.setNomeColore("Giallo");
		return colore;
	}

	private FRLocation createGpsLocation() {

		FRLocation gpsLocation = new FRLocation();

		gpsLocation.setAltitudine(111.2f);
		gpsLocation.setComune(createComune());
		gpsLocation.setCivico("11A");
		gpsLocation.setError(11.2f);
		gpsLocation.setIndirizzo(getStrade());
		gpsLocation.setLatitudine(44.4938100f);
		gpsLocation.setLongitudine(11.3387500f);
		gpsLocation.setLuogoPubblico("Bar Nuovo Bar");
		gpsLocation.setTipoLuogoPubblico("BAR");
		gpsLocation.setTimeStamp(getCalendar(2016, 12, 12, 12, 12, 12).getTime());

		return gpsLocation;
	}

	private Strade getStrade() {

		StradeFilter stradeFilter = new StradeFilter();
		stradeFilter.setId("BO_STR_795");
		List<Strade> stradeList = anagraficaDelegateService.searchStradeByFilter(stradeFilter);

		return stradeList.get(0);
	}

	private CertificatoFr createCertificatoFirstResponder() {

		CertificatoFr certificatoFirstResponder = new CertificatoFr();

		certificatoFirstResponder.setDataConseguimento(getDate(2016, 12, 12));
		certificatoFirstResponder.setDescrizione("descrizione Certificato FirstResponder 1");
		certificatoFirstResponder.setEmailScadenza("emailScadenza1@gmail.com");
		certificatoFirstResponder.setRilasciatoDa("Universita 1");
		certificatoFirstResponder.setDataScadenza(getDate(2016, 12, 12));
		// certificatoFirstResponder.setImmagine(createImmagine());

		return certificatoFirstResponder;
	}

	private Image createImmagine() {

		Image image = new Image();
		image.setData("http.//www.immaginestore.com/immagine1.jpg");
		return image;
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
