package it.eng.areas.ems.sdodaeservices.rest.model;

import it.eng.areas.ems.sdodaeservices.delegate.model.filter.FirstResponderFilter;

public class MessageFRFilter {

	private String message;

	private FirstResponderFilter filter;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FirstResponderFilter getFilter() {
		return filter;
	}

	public void setFilter(FirstResponderFilter filter) {
		this.filter = filter;
	}

}
