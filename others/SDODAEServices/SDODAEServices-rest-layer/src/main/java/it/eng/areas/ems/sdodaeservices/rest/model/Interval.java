package it.eng.areas.ems.sdodaeservices.rest.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Interval {

	static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	static {
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	private String from;

	private String to;

	private Calendar fromCal;

	private Calendar toCal;

	public Interval() {
		super();
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		try {
			Date d = format.parse(from);

			fromCal = Calendar.getInstance();
			fromCal.setTime(d);
		} catch (Exception e) {
		}
		this.from = from;
	}

	public String getTo() {

		return to;
	}

	public void setTo(String to) {
		try {
			Date d = format.parse(to);

			toCal = Calendar.getInstance();
			toCal.setTime(d);
		} catch (Exception e) {
		}
		this.to = to;
	}

	public Calendar getFromCal() {
		return fromCal;
	}

	public void setFromCal(Calendar fromCal) {
		this.fromCal = fromCal;
	}

	public Calendar getToCal() {
		return toCal;
	}

	public void setToCal(Calendar toCal) {
		this.toCal = toCal;
	}

}
