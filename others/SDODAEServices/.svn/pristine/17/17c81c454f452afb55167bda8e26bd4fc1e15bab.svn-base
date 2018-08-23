package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "Utente", description = "Modello dati che rappresenta un Utente")
public class Utente extends TerritorialEntity implements Principal {

	private static final long serialVersionUID = 1L;

	private String id;

	private String nome;

	private String cognome;

	private String email;

	private String civico;

	private Strade indirizzo;

	private Comune comuneResidenza;

	private String codiceFiscale;

	private String language;

	private String logon;

	private List<Ruolo> ruoli;

	private String passwordHash;

	private String password;

	private String statoUtente;

	private Date dataNascita;

	private List<Gruppo> gruppi;

	private Image immagine;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public Strade getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(Strade indirizzo) {
		this.indirizzo = indirizzo;
	}

	public Comune getComuneResidenza() {
		return comuneResidenza;
	}

	public void setComuneResidenza(Comune comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLogon() {
		return logon;
	}

	public void setLogon(String logon) {
		this.logon = logon;
	}

	public List<Ruolo> getRuoli() {
		return ruoli;
	}

	public void setRuoli(List<Ruolo> ruoli) {
		this.ruoli = ruoli;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getStatoUtente() {
		return statoUtente;
	}

	public void setStatoUtente(String statoUtente) {
		this.statoUtente = statoUtente;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getName() {
		return getLogon();
	}

	public List<Gruppo> getGruppi() {
		return gruppi;
	}

	public void setGruppi(List<Gruppo> gruppi) {
		this.gruppi = gruppi;
	}

	public Image getImmagine() {
		return immagine;
	}

	public void setImmagine(Image immagine) {
		this.immagine = immagine;
	}

	public boolean haveRole(RuoliEnum ruolo) {
		for (Ruolo r : ruoli) {
			if (r.getNome().equals(ruolo.name())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getProvince() {
		if (comuneResidenza != null && comuneResidenza.getProvincia() != null) {
			return comuneResidenza.getProvincia().getNomeProvincia();
		} else {
			return "";
		}
	}

	@Override
	public String getMunicipality() {
		if (comuneResidenza != null) {
			return comuneResidenza.getNomeComune();
		} else {
			return "";
		}
	}

}
