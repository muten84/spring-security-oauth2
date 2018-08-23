/**
 * 
 */
package it.eng.areas.ems.mobileagent.device;

/**
 * @author Bifulco Luigi
 *
 */
public interface IIdleMonitor {

	public void start(int idleTime, int awayTime, long  checkFreq);

	public void stop();

}
