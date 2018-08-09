package it.eng.areas.ems.mobileagent.device.gps;

public class GpsEvent {

	protected GPSData gpsData;

	protected short gpsStatus;

	public GpsEvent(GPSData gpsData) {
		this.gpsData = gpsData;
	}
	
	public GpsEvent(short status) {
		this.gpsStatus = status;
	}


	public GPSData getGpsData() {
		return gpsData;
	}

	/**
	 * @return the status
	 */
	public short getGpsStatus() {
		return gpsStatus;
	}



}
