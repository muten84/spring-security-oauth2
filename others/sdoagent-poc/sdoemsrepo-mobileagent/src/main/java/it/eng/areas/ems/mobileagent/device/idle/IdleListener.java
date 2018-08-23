/**
 * 
 */
package it.eng.areas.ems.mobileagent.device.idle;

/**
 * @author Bifulco Luigi
 *
 */
public interface IdleListener {

	void onIdle();

	void onAway();

	void onOnline();

}
