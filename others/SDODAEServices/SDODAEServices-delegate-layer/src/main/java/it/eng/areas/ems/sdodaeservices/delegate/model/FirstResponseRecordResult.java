package it.eng.areas.ems.sdodaeservices.delegate.model;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "FirstResponseRecordResult", description = "Bean che raccoglie l'esito di una nuova registrazione First Response")
public class FirstResponseRecordResult {

	private boolean esito;

	private String messaggio;

	public FirstResponseRecordResult() {
		// TODO Auto-generated constructor stub
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
