/**
 * 
 */
package it.eng.areas.ems.mobileagent.device;

/**
 * @author Bifulco Luigi
 *
 */
public interface IDisplay {

	public void start();

	public void stop();

	/**
	 * @return
	 */
	int getContrast();

	/**
	 * @param contrast
	 */
	void setContrast(int contrast);

	/**
	 * @param brightness
	 */
	void setBrightness(int brightness);

	/**
	 * @return
	 */
	int getBrightness();

}
