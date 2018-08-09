/**
 * 
 */
package it.eng.areas.ems.mobileagent.device;

import java.util.Calendar;

import it.eng.areas.ems.sdoemsrepo.delegate.model.DeviceInfo;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * @author Bifulco Luigi
 *
 */
public class DeviceInfoUtil {

	private static SystemInfo si;
	
	

	public static DeviceInfo createDeviceInfo(String artifacts) {
		if (si == null) {
			si = new SystemInfo();
		}
		DeviceInfo info = new DeviceInfo();
		HardwareAbstractionLayer hal = si.getHardware();
		info.setDeviceId(hal.getComputerSystem().getSerialNumber());
		info.setTimestamp(Calendar.getInstance().getTimeInMillis());
		info.setNote(artifacts);
		return info;
	}

}
