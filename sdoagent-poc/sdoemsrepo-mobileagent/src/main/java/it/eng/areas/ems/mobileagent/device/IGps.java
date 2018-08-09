/**
 * 
 */
package it.eng.areas.ems.mobileagent.device;

import it.eng.areas.ems.mobileagent.device.gps.GPSData;

/**
 * @author Bifulco Luigi
 *
 */
public interface IGps {
	
	public GPSData getPosition();
	
	public short getStatus();
	
	public boolean shutdown();

}
