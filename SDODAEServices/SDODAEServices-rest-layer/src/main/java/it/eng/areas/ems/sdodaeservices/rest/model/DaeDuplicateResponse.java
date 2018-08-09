package it.eng.areas.ems.sdodaeservices.rest.model;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.delegate.model.Dae;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericResponse;

public class DaeDuplicateResponse extends GenericResponse {

	boolean duplicate = true;

	List<Dae> dae;

	public List<Dae> getDae() {
		return dae;
	}

	public void setDae(List<Dae> dae) {
		this.dae = dae;
	}

	public boolean isDuplicate() {
		return duplicate;
	}

	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}

}
