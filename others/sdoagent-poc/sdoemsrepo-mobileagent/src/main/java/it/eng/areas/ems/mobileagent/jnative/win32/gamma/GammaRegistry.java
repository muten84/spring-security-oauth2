/**
 * 
 */
package it.eng.areas.ems.mobileagent.jnative.win32.gamma;

import org.pmw.tinylog.Logger;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

/**
 * @author Bifulco Luigi
 *
 */
public class GammaRegistry {

	private static final WinReg.HKEY GAMMA_HKEY = WinReg.HKEY_LOCAL_MACHINE;
	private static final String GDI_ICM_GAMMA_RANGE = "Software\\Microsoft\\Windows NT\\CurrentVersion\\ICM";
	private static final String GAMMA_KEY_NAME = "GdiIcmGammaRange";
	private static final int GAMMA_EXTENSION_VALUE = 256;

	/**
	 * System restart required for registry Gamma settings to get changed
	 */
	public void installGammaExtension() {
		setGammaExtensionValue(GAMMA_EXTENSION_VALUE);
	}

	private void setGammaExtensionValue(int value) {
		Advapi32Util.registrySetIntValue(GAMMA_HKEY, GDI_ICM_GAMMA_RANGE, GAMMA_KEY_NAME, value);
	}

	public int getGammaExtensionValue() {
		return Advapi32Util.registryGetIntValue(GAMMA_HKEY, GDI_ICM_GAMMA_RANGE, GAMMA_KEY_NAME);
	}

	public boolean isGammaExtensionInstalled() {
		try {
			return getGammaExtensionValue() == GAMMA_EXTENSION_VALUE;
		} catch (Exception ex) {
			Logger.error(ex,"Exception in isGammaExtensionInstalled handler");
		}
		return false;
	}

	public static void main(String[] args) {
		GammaRegistry gammaRegistry = new GammaRegistry();
		if (!gammaRegistry.isGammaExtensionInstalled()) {
			gammaRegistry.installGammaExtension();
		}
		System.out.println("gamma value: " + gammaRegistry.getGammaExtensionValue());
		gammaRegistry.setGammaExtensionValue(155);
		System.out.println("gamma value: " + gammaRegistry.getGammaExtensionValue());
	}

}
