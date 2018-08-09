package it.eng.areas.ems.sdodaeservices.geo.utility;

import org.geotools.measure.Angle;
import org.geotools.measure.Measure;
import org.opengis.geometry.DirectPosition;

public class GeodeticCurve {

	private DirectPosition anchorPoint;

	private Measure distance;

	private Angle azimuth;

	private Angle reverseAzimuth;

	public GeodeticCurve(DirectPosition anchorPoint, Measure distance, Angle azimuth, Angle reverseAzimuth) {
		this.anchorPoint = anchorPoint;
		this.distance = distance;
		this.azimuth = azimuth;
		this.reverseAzimuth = reverseAzimuth;
	}

	public DirectPosition getAnchorPoint() {
		return anchorPoint;
	}

	public void setAnchorPoint(DirectPosition anchorPoint) {
		this.anchorPoint = anchorPoint;
	}

	public Angle getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(Angle azimuth) {
		this.azimuth = azimuth;
	}

	public Measure getDistance() {
		return distance;
	}

	public void setDistance(Measure distance) {
		this.distance = distance;
	}

	public Angle getReverseAzimuth() {
		return reverseAzimuth;
	}

	public void setReverseAzimuth(Angle reverseAzimuth) {
		this.reverseAzimuth = reverseAzimuth;
	}

	@Override
	public String toString() {
		return "GeodeticCurve [anchorPoint=" + anchorPoint + ", distance=" + distance + ", azimuth=" + azimuth + ", reverseAzimuth=" + reverseAzimuth + "]";
	}

}
