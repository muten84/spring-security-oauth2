/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.jsondb.annotation.Document;

public class MobileEmergencyData implements Serializable {
	
	private String luogo;
	
	private String patologia;
	
	private String criticita;
	
	private DetailItemMap criticitaObject;
	
	private String modAttivazione;
	
	private String sirena;
	
	private String comune;
	
	private String localita;
	
	private DetailItemMap localitaObject;
	
	private String cap;
	
	private String indirizzo;
	
	private DetailItemMap indirizzoObject;
	
	private String luogoPubblico;
	
	private DetailItemMap luogoPubblicoObject;
	
	private String civico;
	
	private String piano;
	
	private String codEmergenza;
	
	private DetailItemMap codEmergenzaObject;
	
	private String dataOraSegnalazione;
	
	private DetailItemMap dataOraSegnalazioneObject;
	
	private String personaRif;
	
	private String telefono;
	
	private String nPazienti;
	
	private String sex;
	
	private String eta;
	
	private String note;
	
	private DetailItemMap noteObject;
	
	public MobileEmergencyData(){}
	
	public MobileEmergencyData(String luogo, String patologia, String criticita, String modAttivazione, String sirena, 
			                   String comune, String localita, String cap, String indirizzo, String luogoPubblico, String civico,
			                   String piano, String codEmergenza, String dataOraSegnalazione, String personaRif,
			                   String telefono, String nPazienti, String sex, String eta, String note){ 
		       this.luogo = luogo;
		       this.patologia = patologia;
		       this.criticita = criticita;
		       this.modAttivazione = modAttivazione;
		       this.sirena = sirena;
		       this.comune = comune;
		       this.localita = localita;
		       this.cap = cap;
		       this.indirizzo = indirizzo;
		       this.luogoPubblico = luogoPubblico;
		       this.civico = civico;
		       this.piano = piano;
		       this.codEmergenza = codEmergenza;
		       this.dataOraSegnalazione = dataOraSegnalazione;
		       this.personaRif = personaRif;
		       this.telefono = telefono;
		       this.nPazienti = nPazienti;
		       this.sex = sex;
		       this.eta = eta;
		       this.note = note;
	}

	

	public String getLuogo() {
		return luogo;
	}

	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}

	public String getPatologia() {
		return patologia;
	}

	public void setPatologia(String patologia) {
		this.patologia = patologia;
	}

	public String getCriticita() {
		return criticita;
	}

	public void setCriticita(String criticita) {
		this.criticita = criticita;
	}

	public DetailItemMap getCriticitaObject() {
		return criticitaObject;
	}

	public void setCriticitaObject(DetailItemMap criticitaObject) {
		this.criticitaObject = criticitaObject;
	}

	public String getModAttivazione() {
		return modAttivazione;
	}

	public void setModAttivazione(String modAttivazione) {
		this.modAttivazione = modAttivazione;
	}

	public String getSirena() {
		return sirena;
	}

	public void setSirena(String sirena) {
		this.sirena = sirena;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public DetailItemMap getLocalitaObject() {
		return localitaObject;
	}

	public void setLocalitaObject(DetailItemMap localitaObject) {
		this.localitaObject = localitaObject;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public DetailItemMap getIndirizzoObject() {
		return indirizzoObject;
	}

	public void setIndirizzoObject(DetailItemMap indirizzoObject) {
		this.indirizzoObject = indirizzoObject;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getPiano() {
		return piano;
	}

	public void setPiano(String piano) {
		this.piano = piano;
	}

	public String getCodEmergenza() {
		return codEmergenza;
	}

	public void setCodEmergenza(String codEmergenza) {
		this.codEmergenza = codEmergenza;
	}

	public DetailItemMap getCodEmergenzaObject() {
		return codEmergenzaObject;
	}

	public void setCodEmergenzaObject(DetailItemMap codEmergenzaObject) {
		this.codEmergenzaObject = codEmergenzaObject;
	}

	public String getDataOraSegnalazione() {
		return dataOraSegnalazione;
	}

	public void setDataOraSegnalazione(String dataOraSegnalazione) {
		this.dataOraSegnalazione = dataOraSegnalazione;
	}

	public DetailItemMap getDataOraSegnalazioneObject() {
		return dataOraSegnalazioneObject;
	}

	public void setDataOraSegnalazioneObject(
			DetailItemMap dataOraSegnalazioneObject) {
		this.dataOraSegnalazioneObject = dataOraSegnalazioneObject;
	}

	public String getPersonaRif() {
		return personaRif;
	}

	public void setPersonaRif(String personaRif) {
		this.personaRif = personaRif;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getnPazienti() {
		return nPazienti;
	}

	public void setnPazienti(String nPazienti) {
		this.nPazienti = nPazienti;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public DetailItemMap getNoteObject() {
		return noteObject;
	}

	public void setNoteObject(DetailItemMap noteObject) {
		this.noteObject = noteObject;
	}

	public String getLuogoPubblico() {
		return luogoPubblico;
	}

	public void setLuogoPubblico(String luogoPubblico) {
		this.luogoPubblico = luogoPubblico;
	}

	public DetailItemMap getLuogoPubblicoObject() {
		return luogoPubblicoObject;
	}

	public void setLuogoPubblicoObject(DetailItemMap luogoPubblicoObject) {
		this.luogoPubblicoObject = luogoPubblicoObject;
	}	
	
	
}
