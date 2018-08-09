package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.io.Serializable;
import java.util.Calendar;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "AppSession", description = "Bean utilizzato per restituire le informazioni di avvenuta autenticazione")
public class AppSession implements Serializable {

	private String token;

	private Calendar startSessionTime;

	private String message;

	private boolean available;

	private boolean updatePassword;

	private boolean unauthorized;

	private boolean privacyAccepted;

	private boolean error = false;

	public AppSession() {
		// TODO Auto-generated constructor stub
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Calendar getStartSessionTime() {
		return startSessionTime;
	}

	public void setStartSessionTime(Calendar startSessionTime) {
		this.startSessionTime = startSessionTime;
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

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isUpdatePassword() {
		return updatePassword;
	}

	public void setUpdatePassword(boolean updatePassword) {
		this.updatePassword = updatePassword;
	}

	public boolean isPrivacyAccepted() {
		return privacyAccepted;
	}

	public void setPrivacyAccepted(boolean privacyAccepted) {
		this.privacyAccepted = privacyAccepted;
	}

	public boolean isUnauthorized() {
		return unauthorized;
	}

	public void setUnauthorized(boolean unauthorized) {
		this.unauthorized = unauthorized;
	}

}
