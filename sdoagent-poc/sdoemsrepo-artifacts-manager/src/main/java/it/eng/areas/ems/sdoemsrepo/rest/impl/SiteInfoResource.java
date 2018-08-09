/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.rest.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DeviceConfiguration;
import it.eng.areas.ems.sdoemsrepo.delegate.model.SiteInfo;
import it.eng.areas.ems.sdoemsrepo.service.DocumentStore;
import it.eng.areas.ems.sdoemsrepo.service.impl.JsonDocumentStore;

/**
 * @author Bifulco Luigi
 *
 */
@RestController //
@RequestMapping("/api/rest/resource/siteinfo")
@Api(value = "SiteInfoResource", protocols = "http")
public class SiteInfoResource {

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

	@Value("${default.mqttReceiveFreqInMillis}")
	private long mqttReceiveFreqInMillis;

	@Value("${default.mqttReconnectDelayMax}")
	private long mqttReconnectDelayMax;

	@Value("${default.beepSoundLocation}")
	private String beepSoundLocation;

	@Value("${sdoemsrepo.repository.storepath}")
	private String storePath;

	private DocumentStore<SiteInfo> store;

	@PostConstruct
	public void init() {
		this.store = new JsonDocumentStore<>(this.storePath, SiteInfo.class);
	}

	@RequestMapping(path = "", //
			method = RequestMethod.POST, //
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "create", notes = "Crea un sito")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "OK"), //
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	public @ResponseBody SiteInfo create(@RequestBody SiteInfo siteInfo) {
		SiteInfo base = this.createTemplate();
		this.updateSiteInfo(siteInfo, base);
		base.setId(siteInfo.getId());
		base.setName(siteInfo.getName());
		this.store.insert(base);
		return this.store.find("/.[id='" + siteInfo.getId() + "']").get(0);
	}

	@RequestMapping(path = "/{siteId}", //
			method = RequestMethod.GET, //
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "create", notes = "Get site by id")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "OK"), //
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	public @ResponseBody SiteInfo getById(@PathVariable("siteId") String siteId) {
		return this.store.find("/.[id='" + siteId + "']").get(0);
	}

	@RequestMapping(path = "", //
			method = RequestMethod.PUT, //
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "update", notes = "Crea un sito")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "OK"), //
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	public @ResponseBody SiteInfo update(@RequestBody SiteInfo siteInfo) {
		SiteInfo base = this.store.find("/.[id='" + siteInfo.getId() + "']").get(0);
		this.updateSiteInfo(siteInfo, base);
		this.store.update(base);
		return this.store.find("/.[id='" + siteInfo.getId() + "']").get(0);

	}

	@RequestMapping(path = "", //
			method = RequestMethod.DELETE, //
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "remove", notes = "Crea un sito")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "OK"), //
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	public @ResponseBody SiteInfo remove(@RequestBody SiteInfo siteInfo) {
		return this.store.remove(siteInfo);

	}

	private void updateSiteInfo(SiteInfo from, SiteInfo to) {
		to.setMessageReceiverUrl(from.getMessageReceiverUrl());
		to.setMessageServiceUrl(from.getMessageServiceUrl());
		to.setDefaultLogType(from.getDefaultLogType());
		to.setMqttConnectAttemptMax(from.getMqttConnectAttemptMax());
		to.setMqttKeepAlive(from.getMqttKeepAlive());
		to.setMqttQosLevel(from.getMqttQosLevel());
		to.setMqttReconnectAttemptsMax(from.getMqttReconnectAttemptsMax());
		to.setMqttReconnectDelay(from.getMqttReconnectDelay());
		to.setMqttReconnectDelayMax(from.getMqttReconnectDelayMax());
		to.setMqttReceiveFreqInMillis(from.getMqttReceiveFreqInMillis());
		to.setConnectTimeoutInMillis(from.getConnectTimeoutInMillis());
		to.setReadTimeoutInMillis(from.getReadTimeoutInMillis());
		to.setWriteTimeoutInMillis(from.getWriteTimeoutInMillis());
		this.updateDeviceConfiguration(from.getDeviceConf(), to.getDeviceConf());

	}

	public void updateDeviceConfiguration(DeviceConfiguration from, DeviceConfiguration to) {
		to.setAwayTime(from.getAwayTime());
		to.setBatteryPoller(from.isBatteryPoller());
		to.setBatteryReadOffsetInSeconds(from.getBatteryReadOffsetInSeconds());
		to.setBeepSoundLocation(from.getBeepSoundLocation());
		to.setExtraProps(from.getExtraProps());
		to.setGpsAutoPortScan(from.isGpsAutoPortScan());
		to.setGpsBaudRate(from.getGpsBaudRate());
		to.setGpsPort(from.getGpsPort());
		to.setGpsReadFrequencyInMillis(from.getGpsReadFrequencyInMillis());
		to.setIdleMonitorFrequencyCheck(from.getIdleMonitorFrequencyCheck());
		to.setIdleTime(from.getIdleTime());
		to.setInitialBrightnessLevel(from.getInitialBrightnessLevel());
		to.setOsType(from.getOsType());

	}

	private SiteInfo createTemplate() {
		SiteInfo site = new SiteInfo();
		site.setMqttQosLevel(mqttQosLevel);
		site.setMqttKeepAlive(mqttKeepAlive);
		site.setMqttConnectAttemptMax(connectAttemptMax);
		site.setMqttReconnectAttemptsMax(mqttReconnectAttemptsMax);
		site.setMqttReconnectDelay(mqttReconnectDelay);
		site.setMqttReconnectDelayMax(mqttReconnectDelayMax);
		site.setMqttReceiveFreqInMillis(mqttReceiveFreqInMillis);
		site.setConnectTimeoutInMillis(10000);
		site.setReadTimeoutInMillis(10000);
		site.setWriteTimeoutInMillis(10000);

		site.setDeviceConf(createDeviceTemplate());
		return site;
	}

	private DeviceConfiguration createDeviceTemplate() {
		DeviceConfiguration deviceConf = new DeviceConfiguration();
		deviceConf.setBatteryPoller(true);
		deviceConf.setBatteryReadOffsetInSeconds(30);
		deviceConf.setGpsAutoPortScan(true);
		deviceConf.setGpsBaudRate(4800);
		deviceConf.setGpsPort("COM1");
		deviceConf.setGpsReadFrequencyInMillis(500);
		deviceConf.setOsType("win32");
		deviceConf.setBeepSoundLocation(beepSoundLocation);
		deviceConf.setIdleMonitorFrequencyCheck(500);
		deviceConf.setIdleTime(30);
		deviceConf.setAwayTime(60 * 5);
		deviceConf.setInitialBrightnessLevel(50);
		return deviceConf;
	}

}
