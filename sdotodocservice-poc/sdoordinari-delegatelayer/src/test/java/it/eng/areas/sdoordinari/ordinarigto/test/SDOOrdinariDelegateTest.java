package it.eng.areas.sdoordinari.ordinarigto.test;


import it.eng.areas.ems.ordinari.delegate.SDOOrdinariDelegateService;
import it.eng.areas.ems.ordinari.model.WebBooking;
import it.eng.areas.ems.ordinari.model.WebBookingEquipment;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations =
// "classpath:it/eng/areas/ems/sdogtoTrasportoservices/test-context/test-ctx.xml")
@ContextConfiguration(classes = { SDOOrdinariDelegateTestCtx.class })
//@ActiveProfiles(profiles = "oracle")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SDOOrdinariDelegateTest extends AbstractJUnit4SpringContextTests {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Autowired
	protected SDOOrdinariDelegateService sdordinariDelegateService;

	
	@Test
	public void AsaveWebBooking() {

		WebBooking webBooking  = creaWebBooking();
		
		WebBooking returnedWebBooking = sdordinariDelegateService.saveWebBooking(webBooking);

		
		Assert.assertNotNull(returnedWebBooking);

		
	//	webBooking = sdordinariDelegateService.getWebBookingById(webBooking.getId());

	//	Assert.assertNotNull(webBooking);

//	boolean del = sdordinariDelegateService.deleteWebBookingById(webBooking.getId());
//		Assert.assertTrue(del);

		
		
		
	}

	private WebBooking creaWebBooking() {
		
		WebBooking webBooking = new WebBooking();
		
		webBooking.setCreationDate(new Date());
		webBooking.setStartDescr("Reparto: Blocco Endoscopico 2° piano - Pad.5 - Amb. Endoscopia 2° piano - Pad.5 - Cola , Codice Reparto: CHI3A7 ,  , Struttura: OSPEDALE SANT'ORSOLA , Codice Struttura: STS_SANTORSOLA , Via: Via Massarenti , 40100 , Comune: Bologna , Provincia: BO ,  , Padiglione NUOVE PATOLOGIE , Codice Padiglione: 5 ,  ,  ,");
		webBooking.setDestDescr("Reparto: Amb. Pre-Ricovero Centralizzato - Amb. Pre-Ricovero Centralizzato , Codice Reparto: PRERICA1 ,  , Struttura: OSPEDALE SANT'ORSOLA , Codice Struttura: STS_SANTORSOLA , Via: Via Massarenti , 40100 , Comune: Bologna , Provincia: BO ,  , Padiglione NUOVE PATOLOGIE , Codice Padiglione: 5 ,  ,  , "); 
		webBooking.setName("ALBERTO");
		webBooking.setSurname("ROSSI");
		webBooking.setNote("IL PAZIENTE VA PRELEVATO TRAMITE LETTIGA DAL LETTO");
		webBooking.setWebBookingEquipments(CreatewebBookingEquipments());
		
		return webBooking;
	}

	private Set<WebBookingEquipment> CreatewebBookingEquipments() {
		
		Set<WebBookingEquipment> webBookingEquipments = new HashSet<WebBookingEquipment>();
		
		WebBookingEquipment webBookingEquipment = new WebBookingEquipment();
		
		webBookingEquipment.setEquipmentId("1941");
		
		webBookingEquipment.setEquipmentCompact("DEFIBRILLATORE");
		webBookingEquipments.add(webBookingEquipment);
		
		return webBookingEquipments;
		
	}





}
