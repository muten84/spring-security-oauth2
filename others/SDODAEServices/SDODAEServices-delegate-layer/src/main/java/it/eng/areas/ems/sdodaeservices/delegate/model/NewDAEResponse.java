package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.Calendar;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "NewDAEResponse", description = "Modello dati che rappresenta l'esito di un censimento di  Defibrillatore Automatico Elettronico")
public class NewDAEResponse {

	private Calendar timeStamp;
	
	private boolean esito;
	
	private String messaggio;
	
	public NewDAEResponse() {
		// TODO Auto-generated constructor stub
	}

	public Calendar getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Calendar timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isEsito() {
		return esito;
	}

	public void setEsito(boolean esito) {
		this.esito = esito;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
	
	
	
	
	
	
	
}
