package it.eng.areas.ems.sdodaeservices.exception;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.entity.DaeDO;

public class DaeDuplicateException extends Exception {

	private static final long serialVersionUID = 1L;

	List<DaeDO> dae;

	public DaeDuplicateException(List<DaeDO> duplicates) {
		this.dae = duplicates;
	}

	public List<DaeDO> getDae() {
		return dae;
	}

	public void setDae(List<DaeDO> dae) {
		this.dae = dae;
	}

}
