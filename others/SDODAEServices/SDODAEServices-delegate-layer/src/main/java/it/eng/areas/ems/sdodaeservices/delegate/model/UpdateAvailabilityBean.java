package it.eng.areas.ems.sdodaeservices.delegate.model;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "UpdateAvailabilityBean", description = "Modello dati utile a modificare la disponibilità di un First Responder")
public class UpdateAvailabilityBean {

	private boolean available;

	public UpdateAvailabilityBean() {
		// TODO Auto-generated constructor stub
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

}
