/**
 * 
 */
package it.eng.areas.ems.mobileagent.device.gps;

import java.util.Observable;
import java.util.Observer;

import org.pmw.tinylog.Logger;

/**
 * @author Bifulco Luigi
 *
 */
public abstract class GpsObserver implements Observer {

	private short lastStatus = -2;

	@Override
	public void update(Observable source, Object event) {
		if (source instanceof GpsObservable) {
			if (event instanceof GpsEvent) {
				GPSData data = ((GpsEvent) event).getGpsData();
				if (data != null) {
					//Logger.info("on gps data: "+data.getDMMLatitude()+" - "+data.getDMMLongitude());
					onGpsData(data);
				}
				short newStatus = ((GpsEvent) event).getGpsStatus();
				if (lastStatus != newStatus) {
					lastStatus = newStatus;
					onGpsStatus(newStatus);
				}

			}
		}

	}

	public abstract void onGpsStatus(short status);

	public abstract void onGpsData(GPSData data);

}
