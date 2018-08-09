package it.eng.areas.ems.sdodaeservices.delegate.model;
import java.util.Date;

public class Notifica {

	private String pnsIdentifier;

	private String Messaggio;

	private String alias;

	private Tono tono;

	private String platform;

	private String registrationID;

	private String tags;

	private Date validita;

	private FirstResponder proprieta;

	private Integer log;

	private Integer versione;

	private Date ultimaRichiesta;

	public String getPnsIdentifier() {
		return pnsIdentifier;
	}

	public void setPnsIdentifier(String pnsIdentifier) {
		this.pnsIdentifier = pnsIdentifier;
	}

	public String getMessaggio() {
		return Messaggio;
	}

	public void setMessaggio(String messaggio) {
		Messaggio = messaggio;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Tono getTono() {
		return tono;
	}

	public void setTono(Tono tono) {
		this.tono = tono;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getRegistrationID() {
		return registrationID;
	}

	public void setRegistrationID(String registrationID) {
		this.registrationID = registrationID;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Date getValidita() {
		return validita;
	}

	public void setValidita(Date validita) {
		this.validita = validita;
	}

	public FirstResponder getProprieta() {
		return proprieta;
	}

	public void setProprieta(FirstResponder proprieta) {
		this.proprieta = proprieta;
	}

	public int getLog() {
		return log;
	}

	public void setLog(int log) {
		this.log = log;
	}

	public int getVersione() {
		return versione;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public Date getultimaRichiesta() {
		return ultimaRichiesta;
	}

	public void setultimaRichiesta(Date ultimaRichiesta) {
		this.ultimaRichiesta = ultimaRichiesta;
	}

	
	
}
