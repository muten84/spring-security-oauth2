/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.eng.areas.ems.sdoemsrepo.dao.DeviceInfoRepository;
import it.eng.areas.ems.sdoemsrepo.delegate.model.DeviceInfo;
import it.eng.areas.ems.sdoemsrepo.entity.DeviceInfoDO;
import it.eng.areas.ems.sdoemsrepo.service.DeviceInfoService;

/**
 * @author Bifulco Luigi
 *
 */
@Component
public class DeviceInfoServiceImpl implements DeviceInfoService {

	@Autowired
	private DeviceInfoRepository deviceInfoDAO;

	
	@Override
	public List<DeviceInfoDO> searchDeviceById(String deviceId) {
		return deviceInfoDAO.findByDeviceId(deviceId);
	}

	/* (non-Javadoc)
	 * @see it.eng.areas.ems.sdoemsrepo.service.DeviceInfoService#storeDevice(it.eng.areas.ems.sdoemsrepo.entity.DeviceInfoDO)
	 */
	@Override
	public DeviceInfoDO storeDevice(DeviceInfo device) {
		DeviceInfoDO d = new DeviceInfoDO();
		d.setDeviceId(device.getDeviceId());
		d.setDeviceAddress(device.getIpAddress());
		d.setDeviceArtifacts(device.getNote());
		d.setTimestamp(Calendar.getInstance().getTime());
		return deviceInfoDAO.save(d);
	}

	/* (non-Javadoc)
	 * @see it.eng.areas.ems.sdoemsrepo.service.DeviceInfoService#listDevice()
	 */
	@Override
	public List<DeviceInfoDO> listDevice() {
		return deviceInfoDAO.findAll();
	}

}
