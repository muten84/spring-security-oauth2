package it.eng.areas.ems.sdodaeservices.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
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
import it.eng.areas.ems.sdodaeservices.delegate.MessagesDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponder;
import it.eng.areas.ems.sdodaeservices.delegate.model.Messaggio;
//import it.eng.areas.ems.sdodaeservices.entity.gis.VCTMessaggioDO;
import it.eng.areas.ems.sdodaeservices.service.MessagesTransactionalService;

@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations =
// "classpath:it/eng/areas/ems/sdomessaggioservices/test-context/test-ctx.xml")
@ContextConfiguration(classes = { MessagesDelegateTestCtx.class })
@ActiveProfiles(profiles = "oracle")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessagesDelegateTest extends AbstractJUnit4SpringContextTests {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	String lastID = "";

	@Autowired
	protected MessagesDelegateService messaggioDelegateService;

	@Autowired
	private MessagesTransactionalService service;

	@Autowired
	protected GruppoDelegateService gruppoDelegateService;

	@Test
	@Ignore
	public void AtestCrudMessaggio() {

		Messaggio messaggio = createMessaggio();

		Messaggio returnedMessaggio = messaggioDelegateService.insertMessaggio(messaggio);

		lastID = returnedMessaggio.getId();
		Assert.assertNotNull(lastID);

		String id = lastID;
		messaggio = messaggioDelegateService.getMessaggioById(id);

		Assert.assertNotNull(messaggio);

		boolean del = messaggioDelegateService.deleteMessaggioById(id);
		Assert.assertTrue(del);

	}

	@Test
	@Ignore
	public void BgetAllMessaggi() {

		List<Messaggio> messaggioList = messaggioDelegateService.getAllMessaggio();

		Assert.assertNotNull(messaggioList);
		Assert.assertTrue(!messaggioList.isEmpty());

	}
	/*
	 * @Ignore public void CgetAllVCTDAE() {
	 * 
	 * List<VCTMessaggioDO> messaggioList = messaggioDelegateService.getAllVctMessaggio();
	 * 
	 * Assert.assertNotNull(messaggioList); Assert.assertTrue(!messaggioList.isEmpty());
	 * 
	 * }
	 * 
	 */

	private Messaggio createMessaggio() {

		Messaggio messaggio = new Messaggio();

		messaggio.setId("123456789123456789");
		messaggio.setTesto("Prova di invio messaggio a gruppo 219b55bc57fc02910157fc02b8af0014");
		messaggio.setInvio(new Date());

		List<FirstResponder> responders = new ArrayList<>();
		FirstResponder fr = new FirstResponder();
		fr.setId("4028b88158b61e120158b61fcb4a000b");
		responders.add(fr);

		fr = new FirstResponder();
		fr.setId("8af66b8358bb4e1e0158bb520452000b");
		responders.add(fr);

		messaggio.setResponders(responders);

		return messaggio;
	}

}
