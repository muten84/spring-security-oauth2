package it.eng.areas.ems.sdodaeservices.rest.security;

import java.io.Serializable;

public class OperationUserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6766873148640898641L;

	private boolean operazioneEffettuata;
	private String tipoOperazione;
	private String messaggio;

	public OperationUserInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OperationUserInfo(boolean operazioneEffettuata, String tipoOperazione, String messaggio) {
		super();
		this.operazioneEffettuata = operazioneEffettuata;
		this.tipoOperazione = tipoOperazione;
		this.messaggio = messaggio;
	}

	public boolean isOperazioneEffettuata() {
		return operazioneEffettuata;
	}

	public void setOperazioneEffettuata(boolean operazioneEffettuata) {
		this.operazioneEffettuata = operazioneEffettuata;
	}

	public String getTipoOperazione() {
		return tipoOperazione;
	}

	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

}
