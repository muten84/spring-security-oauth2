/**
 * 
 */
package it.eng.areas.ems.mobileagent.device.battery;

/**
 * @author Bifulco Luigi
 *
 */
public class BatteryConfiguration {

	private boolean interalPoller;

	private int readOffsetInSeconds;

	/**
	 * @return the interalPoller
	 */
	public boolean isInteralPoller() {
		return interalPoller;
	}

	/**
	 * @param interalPoller
	 *            the interalPoller to set
	 */
	public void setInteralPoller(boolean interalPoller) {
		this.interalPoller = interalPoller;
	}

	/**
	 * @return the readOffsetInSeconds
	 */
	public int getReadOffsetInSeconds() {
		return readOffsetInSeconds;
	}

	/**
	 * @param readOffsetInSeconds
	 *            the readOffsetInSeconds to set
	 */
	public void setReadOffsetInSeconds(int readOffsetInSeconds) {
		this.readOffsetInSeconds = readOffsetInSeconds;
	}

}
