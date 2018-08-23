package it.eng.areas.ems.sdodaeservices.rest.model;

import java.util.Map;

import io.swagger.annotations.ApiModel;

@ApiModel
public class NumberDaeFrResult {

	private Map<String, Integer> dae;

	private Map<String, Integer> fr;

	private Integer totalDAE;

	private Integer totalFR;

	public Map<String, Integer> getDae() {
		return dae;
	}

	public void setDae(Map<String, Integer> dae) {
		this.dae = dae;
	}

	public Map<String, Integer> getFr() {
		return fr;
	}

	public void setFr(Map<String, Integer> fr) {
		this.fr = fr;
	}

	public Integer getTotalDAE() {
		return totalDAE;
	}

	public void setTotalDAE(Integer totalDAE) {
		this.totalDAE = totalDAE;
	}

	public Integer getTotalFR() {
		return totalFR;
	}

	public void setTotalFR(Integer totalFR) {
		this.totalFR = totalFR;
	}

}
