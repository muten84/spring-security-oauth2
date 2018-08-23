package it.eng.areas.ems.ordinari.delegate;

import it.eng.areas.ems.ordinari.model.WebBooking;

public interface SDOOrdinariDelegateService {

	
	public WebBooking saveWebBooking(
			WebBooking webBooking);
	
	public WebBooking getWebBookingById(
			String id);
	
}
