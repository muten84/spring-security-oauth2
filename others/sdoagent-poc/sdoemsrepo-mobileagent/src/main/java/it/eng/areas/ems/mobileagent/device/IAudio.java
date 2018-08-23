/**
 * 
 */
package it.eng.areas.ems.mobileagent.device;

/**
 * @author Bifulco Luigi
 *
 */
public interface IAudio {

	/**
	 * 
	 */
	void suspend();

	/**
	 * 
	 */
	void shutdown();

	/**
	 * 
	 */
	void resume();

	/**
	 * 
	 */
	void playSound();

	/**
	 * 
	 */
	void restart();
}
