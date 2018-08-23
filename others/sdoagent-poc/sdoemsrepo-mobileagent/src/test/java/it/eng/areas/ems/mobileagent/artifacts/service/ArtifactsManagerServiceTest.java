/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfoRequest;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DetailItem;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DetailItemMap;
import it.eng.areas.ems.sdoemsrepo.delegate.model.Message;
import it.eng.areas.ems.sdoemsrepo.delegate.model.MobileEmergencyData;

/**
 * @author Bifulco Luigi
 *
 */
@RunWith(JUnit4.class)
public class ArtifactsManagerServiceTest {
	
	ObjectMapper mapper = new ObjectMapper();

	private ArtifactsManagerService service;
	private String url = "http://127.0.0.1:8080/artifacts-manager/";

	@Before
	public void init() {
		if (service == null) {
			service = new ArtifactsManagerService(url, "api/rest/artifacts");
		}
	}
	
	
	
	@Test
	public final void createAttivationMessage() throws IOException {	
		
		Message message = new Message();
		
		message.setType("ACT");
		
		MobileEmergencyData objectMessageActive = new MobileEmergencyData();
		objectMessageActive.setLuogo("TVZ");
		objectMessageActive.setPatologia("TST");
		objectMessageActive.setCriticita("B");
		objectMessageActive.setModAttivazione("Avanzato BLU");
		objectMessageActive.setSirena("NO");
		objectMessageActive.setComune("BO");
		objectMessageActive.setLocalita("Galvani");
		objectMessageActive.setCap("40131");
		objectMessageActive.setIndirizzo("Via Cristoforo Colombo");
		objectMessageActive.setLuogoPubblico("Prova Luogo Pubblico");
		objectMessageActive.setCivico("110");
		objectMessageActive.setPiano("3");
		objectMessageActive.setCodEmergenza("1234AB34TR");
		objectMessageActive.setDataOraSegnalazione("10/02/2017 - 13:50");
		objectMessageActive.setPersonaRif("Gianluigi Albertazzi");
		objectMessageActive.setTelefono("3453456789");
		objectMessageActive.setnPazienti("1");
		objectMessageActive.setSex("M");
		objectMessageActive.setEta("34 anni");
		objectMessageActive.setNote("PROVA NOTE  kkkkkkkkkkkkkkkkkk  kkkkkkkkkkkk kkkkkkkkkkkkkkk kkkkkkkkkkkkkkk kkkkkkkkkkkkkk kkkkkkkkkkkkkk");
		
		
		DetailItemMap detailItemMessageActive = new DetailItemMap();		
		detailItemMessageActive.getDetailMap().put("", "YYYYYYYYYYY");
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setCriticitaObject(detailItemMessageActive);
		
		detailItemMessageActive = new DetailItemMap();		
		detailItemMessageActive.getDetailMap().put("Data", "XXXXXXXXX");
		detailItemMessageActive.getDetailMap().put("Ora", "ZZZZZZZZ");
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setDataOraSegnalazioneObject(detailItemMessageActive);
		
		detailItemMessageActive = new DetailItemMap();		
		detailItemMessageActive.getDetailMap().put("Via", "XXXXXXXXX");
		detailItemMessageActive.getDetailMap().put("Luogo", "ZZZZZZZZ");
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setIndirizzoObject(detailItemMessageActive);
		
		detailItemMessageActive = new DetailItemMap();		
		detailItemMessageActive.getDetailMap().put("", "xxxxxxxxxxxxxxxxx x  xxxxxxxxxx"
				+ "xxxxxxxxxxxxxxxxxxxx");
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setLuogoPubblicoObject(detailItemMessageActive);
		
		detailItemMessageActive = new DetailItemMap();		
		detailItemMessageActive.getDetailMap().put("", "YYYYYYYYYYY");
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setLocalitaObject(detailItemMessageActive);
		
		detailItemMessageActive = new DetailItemMap();		
		detailItemMessageActive.getDetailMap().put("", "YYYYYYYYYYY");
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setCodEmergenzaObject(detailItemMessageActive);
		
		detailItemMessageActive = new DetailItemMap();		
		detailItemMessageActive.getDetailMap().put("", objectMessageActive.getNote() + "xxxxxxxxxx nuiehgtiu  v5ktiov4ni eguv4oingti45 t45tv4 tv45u9t8vuc4 t894v  8vu5yvtn4iu v45bv89t4 tv4 xxxxxxxxxx nuiehgtiu  v5ktiov4ni eguv4oingti45 t45tv4 tv45u9t8vuc4 t894v  8vu5yvtn4iu v45bv89t4 tv4xxxxxxxxxx nuiehgtiu  v5ktiov4ni eguv4oingti45 t45tv4 tv45u9t8vuc4 t894v  8vu5yvtn4iu v45bv89t4 tv4 xxxxxxxxxx nuiehgtiu  v5ktiov4ni eguv4oingti45 t45tv4 tv45u9t8vuc4 t894v  8vu5yvtn4iu v45bv89t4 tv4 ");
		detailItemMessageActive.getDetailArray();
		objectMessageActive.setNoteObject(detailItemMessageActive);
	
		message.setPayload(mapper.writeValueAsString(objectMessageActive));
		
		//Logger.info(mapper.writeValueAsString(message));
	}
	

	@Test
	public final void testlistArtifactsInGroup() throws IOException {
		String groupId = "NACI";
		List<ArtifactInfoRequest> list = service.listArtifactsInGroup(groupId);
		Assert.assertTrue(list.size() > 0);
		Assert.assertTrue(list.iterator().next().getArtifacId().equals("terminal"));

	}
}
