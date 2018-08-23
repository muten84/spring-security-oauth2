/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.rest.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DeviceConfiguration;
import it.eng.areas.ems.sdoemsrepo.delegate.model.SiteInfo;

/**
 * @author Bifulco Luigi
 *
 */
@RestController //
@RequestMapping("/api/rest/siteinfo")
@Api(value = "SiteInfoController", protocols = "http")
@Deprecated //use siteinforesource instead
public class SiteInfoController {

	@Value("${default.messageServiceUrl}")
	private String messageServiceUrl;
	
	@Value("${default.messageReceiverUrl}")
	private String messageReceiverUrl;
	
	@Value("${default.mqttQosLevel}")
	private int mqttQosLevel;
	
	@Value("${default.mqttKeepAlive}")
	private int mqttKeepAlive;
	
	@Value("${default.mqttConnectAttemptMax}")
	private long connectAttemptMax;
	
	@Value("${default.mqttReconnectAttemptsMax}")
	private long mqttReconnectAttemptsMax;
	
	@Value("${default.mqttReconnectDelay}")
	private long mqttReconnectDelay;
	
	@Value("${default.mqttReconnectDelayMax}")
	private long mqttReconnectDelayMax;
	
	@Value("${default.beepSoundLocation}")
	private String beepSoundLocation;
	
	
		Map<String, List<String>> regioneMap = new HashMap<>();
		
		public SiteInfoController() {
			regioneMap.put("Emilia Est", Arrays.asList("BOLOGNA", "FERRARA", "MODENA"));
			regioneMap.put("Emilia Ovest", Arrays.asList("PARMA", "PIACENZA", "REGGIO EMILIA"));
			regioneMap.put("Romagna", Arrays.asList("RAVENNA", "FORLI", "RIMINI"));
		}
	

	@RequestMapping(path = "/getSiteInfoByCompetence/{subSiteId}", //
			method = RequestMethod.GET, //
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "receiveMessage", notes = "Scoda un messaggio dalla coda")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "OK"), //
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	public SiteInfo getSiteInfoByCompetence(@PathVariable("subSiteId") String subSiteId) {
		SiteInfo site = new SiteInfo();
		// TODO: restuire i riferimenti si una centrale specifica
		//site.setMessageReceiverUrl("172.30.50.45:1883");
		//site.setMessageServiceUrl("http://127.0.0.1:8081/sdo118Mobile/rest/mobile/");
		site.setMessageServiceUrl(this.messageServiceUrl);
		site.setMessageReceiverUrl(this.messageReceiverUrl);
		//imposto la massima qos level
		site.setMqttQosLevel(mqttQosLevel);		
		site.setMqttKeepAlive(mqttKeepAlive);
		site.setMqttConnectAttemptMax(connectAttemptMax);
		site.setMqttReconnectAttemptsMax(mqttReconnectAttemptsMax);
		site.setMqttReconnectDelay(mqttReconnectDelay);
		site.setMqttReconnectDelayMax(mqttReconnectDelayMax);
		
		DeviceConfiguration deviceConf = new DeviceConfiguration();
		deviceConf.setBatteryPoller(true);
		deviceConf.setBatteryReadOffsetInSeconds(30);
		deviceConf.setGpsAutoPortScan(true);
		deviceConf.setGpsBaudRate(4800);
		deviceConf.setGpsPort("COM1");
		deviceConf.setGpsReadFrequencyInMillis(500);
		deviceConf.setOsType("win32");		
		deviceConf.setBeepSoundLocation(beepSoundLocation);
		site.setDeviceConf(deviceConf);
		site.setName(getSiteName(subSiteId));
		site.setId(subSiteId);
		return site;
	}	
	
	
	private String getSiteName(String sbusSiteId) {
		Set<String> keys = regioneMap.keySet();
		for (String s : keys) {
			List<String> sites = regioneMap.get(s);
			for (String site : sites) {
				if(site.equalsIgnoreCase(sbusSiteId)) {
					return s;
				}
			}
		}
		return "";
	}
	
	
}
