package it.eng.areas.ems.sdodaeservices.delegate.model;

public class GenericResponse {

	private boolean response;

	private String message;

	private boolean error = false;

	public GenericResponse() {
		// TODO Auto-generated constructor stub
	}

	public GenericResponse(String message, boolean response) {
		super();
		this.response = response;
		this.message = message;
	}

	public boolean isResponse() {
		return response;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
