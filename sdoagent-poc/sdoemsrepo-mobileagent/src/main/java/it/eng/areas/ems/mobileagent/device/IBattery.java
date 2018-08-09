/**
 * 
 */
package it.eng.areas.ems.mobileagent.device;

/**
 * @author Bifulco Luigi
 *
 */
public interface IBattery {

	double getBatteryLevelPercent();

	boolean getBatteryAcConnected();
	
	public boolean shutdown();

}
