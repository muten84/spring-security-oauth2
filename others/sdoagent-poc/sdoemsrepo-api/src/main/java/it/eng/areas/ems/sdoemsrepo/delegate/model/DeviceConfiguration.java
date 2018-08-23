/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import java.util.Map;

/**
 * @author Bifulco Luigi
 *
 */
public class DeviceConfiguration {

	private int batteryReadOffsetInSeconds;

	private boolean batteryPoller;

	private boolean gpsAutoPortScan;

	private int gpsReadFrequencyInMillis;

	private String gpsPort;

	private int gpsBaudRate;

	private String osType;

	private String beepSoundLocation;

	private int initialBrightnessLevel;

	private int idleTime;

	private int awayTime;

	private long idleMonitorFrequencyCheck;

	private Map<String, Object> extraProps;

	/**
	 * @return the batteryReadOffsetInSeconds
	 */
	public int getBatteryReadOffsetInSeconds() {
		return batteryReadOffsetInSeconds;
	}

	/**
	 * @param batteryReadOffsetInSeconds
	 *            the batteryReadOffsetInSeconds to set
	 */
	public void setBatteryReadOffsetInSeconds(int batteryReadOffsetInSeconds) {
		this.batteryReadOffsetInSeconds = batteryReadOffsetInSeconds;
	}

	/**
	 * @return the batteryPoller
	 */
	public boolean isBatteryPoller() {
		return batteryPoller;
	}

	/**
	 * @param batteryPoller
	 *            the batteryPoller to set
	 */
	public void setBatteryPoller(boolean batteryPoller) {
		this.batteryPoller = batteryPoller;
	}

	/**
	 * @return the gpsAutoPortScan
	 */
	public boolean isGpsAutoPortScan() {
		return gpsAutoPortScan;
	}

	/**
	 * @param gpsAutoPortScan
	 *            the gpsAutoPortScan to set
	 */
	public void setGpsAutoPortScan(boolean gpsAutoPortScan) {
		this.gpsAutoPortScan = gpsAutoPortScan;
	}

	/**
	 * @return the gpsReadFrequencyInMillis
	 */
	public int getGpsReadFrequencyInMillis() {
		return gpsReadFrequencyInMillis;
	}

	/**
	 * @param gpsReadFrequencyInMillis
	 *            the gpsReadFrequencyInMillis to set
	 */
	public void setGpsReadFrequencyInMillis(int gpsReadFrequencyInMillis) {
		this.gpsReadFrequencyInMillis = gpsReadFrequencyInMillis;
	}

	/**
	 * @return the gpsPort
	 */
	public String getGpsPort() {
		return gpsPort;
	}

	/**
	 * @param gpsPort
	 *            the gpsPort to set
	 */
	public void setGpsPort(String gpsPort) {
		this.gpsPort = gpsPort;
	}

	/**
	 * @return the gpsBaudRate
	 */
	public int getGpsBaudRate() {
		return gpsBaudRate;
	}

	/**
	 * @param gpsBaudRate
	 *            the gpsBaudRate to set
	 */
	public void setGpsBaudRate(int gpsBaudRate) {
		this.gpsBaudRate = gpsBaudRate;
	}

	/**
	 * @return the osType
	 */
	public String getOsType() {
		return osType;
	}

	/**
	 * @param osType
	 *            the osType to set
	 */
	public void setOsType(String osType) {
		this.osType = osType;
	}

	/**
	 * @return the beepSoundLocation
	 */
	public String getBeepSoundLocation() {
		return beepSoundLocation;
	}

	/**
	 * @param beepSoundLocation
	 *            the beepSoundLocation to set
	 */
	public void setBeepSoundLocation(String beepSoundLocation) {
		this.beepSoundLocation = beepSoundLocation;
	}

	/**
	 * @return the initialBrightnessLevel
	 */
	public int getInitialBrightnessLevel() {
		return initialBrightnessLevel;
	}

	/**
	 * @param initialBrightnessLevel
	 *            the initialBrightnessLevel to set
	 */
	public void setInitialBrightnessLevel(int initialBrightnessLevel) {
		this.initialBrightnessLevel = initialBrightnessLevel;
	}

	/**
	 * @return the extraProps
	 */
	public Map<String, Object> getExtraProps() {
		return extraProps;
	}

	/**
	 * @param extraProps
	 *            the extraProps to set
	 */
	public void setExtraProps(Map<String, Object> extraProps) {
		this.extraProps = extraProps;
	}

	/**
	 * @return the awayTime
	 */
	public int getAwayTime() {
		return awayTime;
	}

	/**
	 * @param awayTime
	 *            the awayTime to set
	 */
	public void setAwayTime(int awayTime) {
		this.awayTime = awayTime;
	}

	/**
	 * @return the idleMonitorFrequencyCheck
	 */
	public long getIdleMonitorFrequencyCheck() {
		return idleMonitorFrequencyCheck;
	}

	/**
	 * @param idleMonitorFrequencyCheck
	 *            the idleMonitorFrequencyCheck to set
	 */
	public void setIdleMonitorFrequencyCheck(long idleMonitorFrequencyCheck) {
		this.idleMonitorFrequencyCheck = idleMonitorFrequencyCheck;
	}

	/**
	 * @return the idleTime
	 */
	public int getIdleTime() {
		return idleTime;
	}

	/**
	 * @param idleTime
	 *            the idleTime to set
	 */
	public void setIdleTime(int idleTime) {
		this.idleTime = idleTime;
	}

}
