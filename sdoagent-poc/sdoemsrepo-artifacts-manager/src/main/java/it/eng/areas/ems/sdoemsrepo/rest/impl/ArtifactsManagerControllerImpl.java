/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.rest.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdoemsrepo.delegate.ArtifactsManagerDelegateService;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfoRequest;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DeviceInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DownloadArtifactResponse;
import it.eng.areas.ems.sdoemsrepo.rest.ArtifactsManagerController;
import it.eng.areas.ems.sdoemsrepo.service.DeviceInfoService;

/**
 * @author Bifulco Luigi
 *
 */
@RestController //
@RequestMapping("/api/rest/artifacts")
@Api(value = "ArtifactsManagerController", protocols = "http")
public class ArtifactsManagerControllerImpl implements ArtifactsManagerController {

	private final static Logger LOG = LoggerFactory.getLogger(ArtifactsManagerControllerImpl.class);

	@Autowired
	private ArtifactsManagerDelegateService delegate;

	@Autowired
	private DeviceInfoService deviceService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdoemsrepo.rest.ArtifactsManagerController#
	 * getArtifactInfo(it.eng.areas.ems.sdoemsrepo.delegate.model.
	 * ArtifactInfoRequest)
	 */

	@RequestMapping(path = "/getArtifactInfo", //
			method = RequestMethod.POST, //
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, //
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "getArtifactInfo", notes = "Permette di ottenere informazioni sull'artefatto in input")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "OK"), //
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	public @ResponseBody ArtifactInfo getArtifactInfo(@RequestBody ArtifactInfoRequest request,
			HttpServletRequest httpRequest) {
		// System.out.println("device info: " +
		// request.getDeviceInfo().getDeviceId());
		String ipAddress = httpRequest.getRemoteAddr();
		DeviceInfo device = request.getDeviceInfo();
		if (device != null) {
			LOG.info("getArtifactInfo REQUEST FROM: " + ipAddress + " - " + device.getDeviceId() + " - "
					+ device.getNote());
			try {
				device.setIpAddress(ipAddress);
				deviceService.storeDevice(device);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			LOG.info("getArtifactInfo REQUEST FROM: " + ipAddress);
		}

		// List<DeviceInfoDO> devices =
		// deviceService.searchDeviceById(device.getDeviceId());

		// TODO: memorizzare stato del device
		return delegate.getArtifactInfo(request);
	}

	@RequestMapping(path = "/requestArtifactDownload", //
			method = RequestMethod.POST, //
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, //
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "requestArtifactDownload", notes = "Permette di ottenere informazioni per avviare il download dell'artefatto")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "OK"), //
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	public @ResponseBody DownloadArtifactResponse requestArtifactDownload(@RequestBody ArtifactInfo info,
			HttpServletRequest httpRequest) {
		String ipAddress = httpRequest.getRemoteAddr();

		LOG.info("requestArtifactDownload REQUEST FROM: " + ipAddress);

		return delegate.requestArtifactDownload(info);
	}

	@RequestMapping(value = "/repository/{groupId}/**", method = RequestMethod.GET)
	public void downloadFile(@PathVariable("groupId") String groupId, HttpServletRequest request,
			HttpServletResponse response) {
		String ipAddress = request.getRemoteAddr();

		LOG.info("downloadFile REQUEST FROM: " + ipAddress);
		final String path = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
		final String bestMatchingPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE)
				.toString();

		String relativePath = path.substring(path.indexOf(groupId), path.length());

		// String arguments = new
		// AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);
		String fullPath = delegate.getArtifactPath(relativePath);
		try (FileInputStream is = new FileInputStream(new File(fullPath))) {
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdoemsrepo.rest.ArtifactsManagerController#
	 * listArtifactsByType(java.lang.String)
	 */
	@RequestMapping(value = "/listArtifactsInGroup/{groupId}/**", method = RequestMethod.GET)
	@ApiOperation(value = "listArtifactsInGroup", notes = "Permette di ottenere informazioni per avviare il download dell'artefatto")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "OK"), //
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	public List<ArtifactInfoRequest> listArtifactsInGroup(@PathVariable("groupId") String groupId) {

		return delegate.listArtifactsInGroup(groupId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.sdoemsrepo.rest.ArtifactsManagerController#
	 * getArtifactInfo(it.eng.areas.ems.sdoemsrepo.delegate.model.
	 * ArtifactInfoRequest)
	 */

	public ArtifactInfo getArtifactInfo(ArtifactInfoRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
