/**
 * 
 */
package it.eng.areas.ems.mobileagent.device.gps;

/**
 * @author Bifulco Luigi
 *
 */
public class GpsConfiguration {

	private boolean autoPortScan;

	private int readFrequencyInMillis;

	private String port;

	private int baudRate;

	/**
	 * @return the autoPortScan
	 */
	public boolean isAutoPortScan() {
		return autoPortScan;
	}

	/**
	 * @param autoPortScan
	 *            the autoPortScan to set
	 */
	public void setAutoPortScan(boolean autoPortScan) {
		this.autoPortScan = autoPortScan;
	}

	/**
	 * @return the readFrequencyInMillis
	 */
	public int getReadFrequencyInMillis() {
		return readFrequencyInMillis;
	}

	/**
	 * @param readFrequencyInMillis
	 *            the readFrequencyInMillis to set
	 */
	public void setReadFrequencyInMillis(int readFrequencyInMillis) {
		this.readFrequencyInMillis = readFrequencyInMillis;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the baudRate
	 */
	public int getBaudRate() {
		return baudRate;
	}

	/**
	 * @param baudRate
	 *            the baudRate to set
	 */
	public void setBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}

}
