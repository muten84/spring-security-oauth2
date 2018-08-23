/**
 * 
 */
package it.eng.areas.ems.mobileagent.jnative.win32;

import com.sun.jna.platform.win32.Dxva2;
import com.sun.jna.platform.win32.PhysicalMonitorEnumerationAPI.PHYSICAL_MONITOR;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.DWORDByReference;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HMONITOR;
import com.sun.jna.platform.win32.WinUser.MONITORENUMPROC;
import com.sun.jna.platform.win32.WinUser.MONITORINFOEX;

import it.eng.areas.ems.mobileagent.device.IDisplay;

/**
 * @author Bifulco Luigi
 *
 */
public class Win32Display implements IDisplay {

	HMONITOR display;

	HANDLE hPhysicalMonitor;

	public void start() {
		User32.INSTANCE.EnumDisplayMonitors(null, null, new MONITORENUMPROC() {

			@Override
			public int apply(HMONITOR hMonitor, HDC hdc, RECT rect, LPARAM lparam) {

				MONITORINFOEX info = new MONITORINFOEX();
				User32.INSTANCE.GetMonitorInfo(hMonitor, info);
				System.out.println("Screen " + info.rcMonitor);
				System.out.println("Work area " + info.rcWork);
				boolean isPrimary = (info.dwFlags & WinUser.MONITORINFOF_PRIMARY) != 0;
				if (!isPrimary)
					return 0;

				collect(hMonitor);

				return 1;
			}

		}, new LPARAM(0));
	}

	public void stop() {
		display = null;
		hPhysicalMonitor = null;
	}

	private void collect(HMONITOR hMonitor) {
		display = hMonitor;
		DWORDByReference pdwNumberOfPhysicalMonitors = new DWORDByReference();
		Dxva2.INSTANCE.GetNumberOfPhysicalMonitorsFromHMONITOR(hMonitor, pdwNumberOfPhysicalMonitors);
		int monitorCount = pdwNumberOfPhysicalMonitors.getValue().intValue();
		PHYSICAL_MONITOR[] physMons = new PHYSICAL_MONITOR[monitorCount];
		Dxva2.INSTANCE.GetPhysicalMonitorsFromHMONITOR(hMonitor, monitorCount, physMons);
		for (int i = 0; i < monitorCount; i++) {
			hPhysicalMonitor = physMons[0].hPhysicalMonitor;
			System.out.println("Monitor " + i + " - " + new String(physMons[i].szPhysicalMonitorDescription));
			// enumeratePhysicalMonitor(hPhysicalMonitor);
			DWORDByReference temps = new DWORDByReference();
			DWORDByReference caps = new DWORDByReference();
			Dxva2.INSTANCE.GetMonitorCapabilities(hPhysicalMonitor, caps, temps);
		}
	}

	@Override
	public int getBrightness() {
		DWORDByReference pdwMinimumBrightness = new DWORDByReference();
		DWORDByReference pdwCurrentBrightness = new DWORDByReference();
		DWORDByReference pdwMaximumBrightness = new DWORDByReference();
		Dxva2.INSTANCE.GetMonitorBrightness(hPhysicalMonitor, pdwMinimumBrightness, pdwCurrentBrightness,
				pdwMaximumBrightness);
		return pdwCurrentBrightness.getValue().intValue();
	}

	@Override
	public void setBrightness(int brightness) {
		// Brightness
		Dxva2.INSTANCE.SetMonitorBrightness(hPhysicalMonitor, brightness);
	}

	@Override
	public void setContrast(int contrast) {
		Dxva2.INSTANCE.SetMonitorContrast(hPhysicalMonitor, contrast);
	}

	@Override
	public int getContrast() {
		DWORDByReference min = new DWORDByReference();
		DWORDByReference curr = new DWORDByReference();
		DWORDByReference max = new DWORDByReference();
		Dxva2.INSTANCE.GetMonitorContrast(hPhysicalMonitor, min, curr, max);
		return curr.getValue().intValue();
	}

}
