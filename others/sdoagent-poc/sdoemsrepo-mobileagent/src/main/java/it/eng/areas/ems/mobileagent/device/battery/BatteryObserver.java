/**
 * 
 */
package it.eng.areas.ems.mobileagent.device.battery;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Bifulco Luigi
 *
 */
public abstract class BatteryObserver implements Observer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable source, Object event) {
		if (source instanceof BatteryObservable && event instanceof BatteryEvent) {
			BatteryData data = ((BatteryEvent) event).getData();
			onBatteryData(data);
		}

	}

	public abstract void onBatteryData(BatteryData data);
}
