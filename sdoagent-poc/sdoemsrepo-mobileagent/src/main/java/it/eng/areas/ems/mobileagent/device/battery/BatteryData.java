/**
 * 
 */
package it.eng.areas.ems.mobileagent.device.battery;

/**
 * @author Bifulco Luigi
 *
 */
public class BatteryData {
	private double lifePercent;

	private boolean acConnected;

	/**
	 * @return the lifePercent
	 */
	public double getLifePercent() {
		return lifePercent;
	}

	/**
	 * @param lifePercent
	 *            the lifePercent to set
	 */
	public void setLifePercent(double lifePercent) {
		this.lifePercent = lifePercent;
	}

	/**
	 * @return the acConnected
	 */
	public boolean isAcConnected() {
		return acConnected;
	}

	/**
	 * @param acConnected
	 *            the acConnected to set
	 */
	public void setAcConnected(boolean acConnected) {
		this.acConnected = acConnected;
	}

}
