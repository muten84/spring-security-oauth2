package it.eng.areas.ems.mobileagent.device.gps;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.pmw.tinylog.Logger;

import it.eng.areas.ems.mobileagent.device.QualifiedCoordinates;

public class GPSData {
	
	public static final String PATTERN_STANDARD_DDMMYYYYHHMMSS = "dd/MM/yyyy HH:mm:ss";

	protected long timestamp;

	protected QualifiedCoordinates coords;

	protected float speed;

	protected float course;

	protected double hdop;

	protected double vdop;

	protected int satellite;

	protected boolean valid;

	public GPSData() {
		timestamp = LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC);
		
		speed = Float.NaN;
		course = Float.NaN;
		hdop = Double.NaN;
		vdop = Double.NaN;
		satellite = 0;
		coords = new QualifiedCoordinates();
		valid = false;
	}

	public GPSData(double latitude, double longitude, float speed, float course, boolean valid) {
		this.timestamp = LocalDateTime.now(ZoneOffset.UTC).toEpochSecond(ZoneOffset.UTC);
		this.coords = new QualifiedCoordinates(latitude, longitude);
		this.speed = speed;
		this.course = course;
		this.valid = valid;
	}

	public double getLatitude() {
		return this.coords.getLatitude();
	}

	public void setLatitude(double latitude) {
		this.coords.setLatitude(latitude);
	}

	public double getLongitude() {
		return this.coords.getLongitude();
	}

	public void setLongitude(double longitude) {
		this.coords.setLongitude(longitude);
	}

	public void setAltitude(float altitude) {
		coords.setAltitude(altitude);
	}

	public float getAltitude() {
		return coords.getAltitude();
	}

	public double bearingAngle(GPSData target) {
		return coords.bearingAngle(target.getCoords());
	}

	public double haversineDistance(GPSData target) {
		return coords.haversineDistance(target.getCoords());
	}

	protected double normToDD(double c) {
		double deg = Math.floor(c * 1e-2);
		double min = c - deg * 1e2;
		return deg + min / 60.0;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getCourse() {
		return course;
	}

	public void setCourse(float course) {
		this.course = course;
	}

	public double getHdop() {
		return hdop;
	}

	public void setHdop(double hdop) {
		this.hdop = hdop;
	}

	public double getVdop() {
		return vdop;
	}

	public void setVdop(double vdop) {
		this.vdop = vdop;
	}

	public int getSatellite() {
		return satellite;
	}

	public void setSatellite(int satellite) {
		this.satellite = satellite;
	}

	public String getEstimate() {
		String eval = "NV";
		if (valid) {
			if (satellite >= 3) {
				eval = "V";
			}
		}
		return eval;
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isAccurate() {
		// return (valid && getCoords().getHorizontalAccuracy() <= 0.0f);
		return valid;
	}

	public String getDMS(double position, int degNumeral) {
//		if (position == Double.NaN) {
//			return "---";
//		}
//
//		StringBuffer result = new StringBuffer();
//		int log10 = (int) (Math.log(position) / Math.log(10.0));
//		for (int idx = log10; idx < degNumeral - 1; idx++) {
//			result.append("0");
//		}
//		result.append(Integer.toString((int) Math.floor(position)) + "\260 ");
//		position = (position - Math.floor(position)) * 60;
//		if (position < 10) {
//			result.append('0');
//		}
//		result.append(Integer.toString((int) Math.floor(position)) + "' ");
//		position = (position - Math.floor(position)) * 60;
//		position = Math.floor(position * 100) / 100;
//		if (position < 10) {
//			result.append('0');
//		}
//		result.append(Double.toString(position));
//		result.append("\"");
//		return result.toString();
		return "";
	}

	public String getDMSLatitude() {
		return getDMS(coords.getLatitude(), 2);
	}

	public String getDMSLongitude() {
		return getDMS(coords.getLongitude(), 3);
	}

	protected double getDMM(double value) {
//		double deg = Math.floor(value);
//		double min = (value - deg) * 60;
//
//		double exp = Math.pow(0.1, Math.floor((Math.log(min) / Math.log(10.0)) + 1));
//		min = min * exp;
//
//		return deg + min;
		return 0;
	}

	public double getDMMLatitude() {
		return getDMM(coords.getLatitude());
	}

	public double getDMMLongitude() {
		return getDMM(coords.getLongitude());
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public QualifiedCoordinates getCoords() {
		return coords;
	}

	public void setCoords(QualifiedCoordinates coords) {
		this.coords = coords;
	}

//	public String toString() {
//		StringBuffer sb = new StringBuffer(100);
//		sb.append("[GPSData time(");
//		sb.append(format(new java.util.Date(timestamp), PATTERN_STANDARD_DDMMYYYYHHMMSS, TimeZone.getTimeZone("Europe/Rome")));
//		sb.append(") latitude(");
//		sb.append(coords.getLatitude());
//		sb.append(") longitude(");
//		sb.append(coords.getLongitude());
//		sb.append(") speed(");
//		sb.append(speed);
//		sb.append(") hdop(");
//		sb.append(hdop);
//		sb.append(") satellite(");
//		sb.append(satellite);
//		sb.append(") course(");
//		sb.append(course);
//		sb.append(") valid(");
//		sb.append(valid);
//		sb.append(")]");
//
//		return sb.toString();
//	}
	
	public static String format(Date date, String pattern, TimeZone timezone) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(timezone);

		String output = null;
		try {
			return sdf.format(date);
		} catch (Exception e) {
			Logger.error(e,"format GpsData exception ");
		}
		return output;
	}

}