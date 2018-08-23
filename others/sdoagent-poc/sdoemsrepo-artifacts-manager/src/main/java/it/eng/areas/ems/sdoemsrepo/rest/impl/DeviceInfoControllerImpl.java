/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.rest.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DeviceInfo;
import it.eng.areas.ems.sdoemsrepo.entity.DeviceInfoDO;
import it.eng.areas.ems.sdoemsrepo.rest.DeviceInfoController;
import it.eng.areas.ems.sdoemsrepo.service.DeviceInfoService;

/**
 * @author Bifulco Luigi
 *
 */
@RestController //
@RequestMapping("/api/rest/device")
@Api(value = "DeviceInfoControllerImpl //", protocols = "http")
public class DeviceInfoControllerImpl implements DeviceInfoController {
	
	@Autowired
	private DeviceInfoService deviceService;
	
	@RequestMapping(path = "/listDevices", //
			method = RequestMethod.GET, //			
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "listDevices", notes = "")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "OK"), //
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") }) //
	public @ResponseBody List<DeviceInfo> listDevices(){
		
		List<DeviceInfoDO> devices= deviceService.listDevice();
		List<DeviceInfo> list = new ArrayList<DeviceInfo>();
		devices.forEach((d) -> {
			list.add(fromDoToDTO(d));
		});
		return list;
	}
	
	private DeviceInfo fromDoToDTO(DeviceInfoDO d){
		DeviceInfo info = new DeviceInfo();
		info.setDeviceId(d.getDeviceId());
		info.setIpAddress(d.getDeviceAddress());
		info.setNote(d.getDeviceArtifacts());
		info.setTimestamp(d.getTimestamp().getTime());
		return info;
	}

}
