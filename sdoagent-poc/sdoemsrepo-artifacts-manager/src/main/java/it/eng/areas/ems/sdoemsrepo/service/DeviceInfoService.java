/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.service;

import java.util.List;

import it.eng.areas.ems.sdoemsrepo.delegate.model.DeviceInfo;
import it.eng.areas.ems.sdoemsrepo.entity.DeviceInfoDO;

/**
 * @author Bifulco Luigi
 *
 */
public interface DeviceInfoService {

	/**
	 * @param deviceId
	 * @return
	 */
	List<DeviceInfoDO> searchDeviceById(String deviceId);

	DeviceInfoDO storeDevice(DeviceInfo device);

	List<DeviceInfoDO> listDevice();
}
