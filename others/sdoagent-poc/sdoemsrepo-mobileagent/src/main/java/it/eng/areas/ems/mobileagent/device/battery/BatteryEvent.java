/**
 * 
 */
package it.eng.areas.ems.mobileagent.device.battery;

/**
 * @author Bifulco Luigi
 *
 */
public class BatteryEvent {

	BatteryData data;

	public BatteryEvent(BatteryData data) {
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public BatteryData getData() {
		return data;
	}

}
