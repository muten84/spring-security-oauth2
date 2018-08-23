package it.eng.areas.ems.sdodaeservices.delegate.model;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "UpdateAvailabilityResponse", description = "Modello dati utilizzato per ottenere l'esito dell'operazione di cambio disponibilità")
public class UpdateAvailabilityResponse {

	private boolean success;

	public UpdateAvailabilityResponse() {
		// TODO Auto-generated constructor stub
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
