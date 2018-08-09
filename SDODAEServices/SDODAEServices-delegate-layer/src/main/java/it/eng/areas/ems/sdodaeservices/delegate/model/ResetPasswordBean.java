package it.eng.areas.ems.sdodaeservices.delegate.model;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "ResetPasswordBean", description = "Bean utilizzato per il reset della password")
public class ResetPasswordBean {

	private String emailAddress;

	public ResetPasswordBean() {
		// TODO Auto-generated constructor stub
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
