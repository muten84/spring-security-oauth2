package it.eng.areas.ems.sdodaeservices.delegate.model;
public class NotificheConfig {

	private String id;

	private boolean aggiungiDataCreazione;

	private Tono audioInterventoNuovo;

	private Tono audioInterventoAggiornato;

	private Tono audioInterventoAnnullato;

	private Tono audioInterventoTerminato;

	private Tono audioNews;

	private Tono audioCertificatoBLSDAE;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isAggiungiDataCreazione() {
		return aggiungiDataCreazione;
	}

	public void setAggiungiDataCreazione(boolean aggiungiDataCreazione) {
		this.aggiungiDataCreazione = aggiungiDataCreazione;
	}

	public Tono getAudioInterventoNuovo() {
		return audioInterventoNuovo;
	}

	public void setAudioInterventoNuovo(Tono audioInterventoNuovo) {
		this.audioInterventoNuovo = audioInterventoNuovo;
	}

	public Tono getAudioInterventoAggiornato() {
		return audioInterventoAggiornato;
	}

	public void setAudioInterventoAggiornato(Tono audioInterventoAggiornato) {
		this.audioInterventoAggiornato = audioInterventoAggiornato;
	}

	public Tono getAudioInterventoAnnullato() {
		return audioInterventoAnnullato;
	}

	public void setAudioInterventoAnnullato(Tono audioInterventoAnnullato) {
		this.audioInterventoAnnullato = audioInterventoAnnullato;
	}

	public Tono getAudioInterventoTerminato() {
		return audioInterventoTerminato;
	}

	public void setAudioInterventoTerminato(Tono audioInterventoTerminato) {
		this.audioInterventoTerminato = audioInterventoTerminato;
	}

	public Tono getAudioNews() {
		return audioNews;
	}

	public void setAudioNews(Tono audioNews) {
		this.audioNews = audioNews;
	}

	public Tono getAudioCertificatoBLSDAE() {
		return audioCertificatoBLSDAE;
	}

	public void setAudioCertificatoBLSDAE(Tono audioCertificatoBLSDAE) {
		this.audioCertificatoBLSDAE = audioCertificatoBLSDAE;
	}

	
	
}
