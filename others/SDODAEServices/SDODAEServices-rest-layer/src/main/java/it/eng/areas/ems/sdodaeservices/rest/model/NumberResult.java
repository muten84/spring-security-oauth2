package it.eng.areas.ems.sdodaeservices.rest.model;

import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel
public class NumberResult {

	private List<List<String>> data;

	private List<String> labels;

	private List<String> series;

	public List<List<String>> getData() {
		return data;
	}

	public void setData(List<List<String>> data) {
		this.data = data;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<String> getSeries() {
		return series;
	}

	public void setSeries(List<String> series) {
		this.series = series;
	}

}
