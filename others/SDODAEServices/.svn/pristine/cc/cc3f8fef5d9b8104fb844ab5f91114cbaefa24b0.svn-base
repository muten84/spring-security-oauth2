package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_UTENTE")
public class UtenteDO implements Serializable {

	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	@Id
	private String id;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "utente")
	private FirstResponderDO firstResponder;

	private String nome;

	private String cognome;

	private String email;

	private String civico;

	@Column(name = "DATA_NASCITA")
	private Date dataNascita;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INDIRIZZO")
	private StradeDO indirizzo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMUNE_RESIDENZA")
	private ComuneDO comuneResidenza;

	@Column(name = "CODICE_FISCALE")
	private String codiceFiscale;

	private String language;

	private String logon;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "DAE_UTENTE_RUOLO", joinColumns = { @JoinColumn(name = "UTENTE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "RUOLO_ID") })
	private Set<RuoloDO> ruoli;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "DAE_UTENTE_GRUPPO", joinColumns = { @JoinColumn(name = "UTENTE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "GRUPPO_ID") })
	private Set<GruppoDO> gruppi;

	@Column(name = "PASSWORD_HASH")
	private String passwordHash;

	@Column(name = "STATO_UTENTE")
	private String statoUtente;

	@Column(name = "DELETED")
	private Boolean deleted = false;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "IMMAGINE")
	private ImageDO immagine;

	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Set<PasswordHistoryDO> passwordHistories;

	@OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
	private Set<DaeFaultDO> guasti = new HashSet<DaeFaultDO>();

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

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public StradeDO getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(StradeDO indirizzo) {
		this.indirizzo = indirizzo;
	}

	public ComuneDO getComuneResidenza() {
		return comuneResidenza;
	}

	public void setComuneResidenza(ComuneDO comuneResidenza) {
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

	public FirstResponderDO getFirstResponder() {
		return firstResponder;
	}

	public void setFirstResponder(FirstResponderDO firstResponder) {
		this.firstResponder = firstResponder;
	}

	public Set<RuoloDO> getRuoli() {
		return ruoli;
	}

	public void setRuoli(Set<RuoloDO> ruoli) {
		this.ruoli = ruoli;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public UtenteDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Set<GruppoDO> getGruppi() {
		return gruppi;
	}

	public void setGruppi(Set<GruppoDO> gruppi) {
		this.gruppi = gruppi;
	}

	public ImageDO getImmagine() {
		return immagine;
	}

	public void setImmagine(ImageDO immagine) {
		this.immagine = immagine;
	}

	public Set<PasswordHistoryDO> getPasswordHistories() {
		if (passwordHistories == null) {
			passwordHistories = new HashSet<>();
		}
		return passwordHistories;
	}

	public void setPasswordHistories(Set<PasswordHistoryDO> passwordHistories) {
		this.passwordHistories = passwordHistories;
	}

	public Set<DaeFaultDO> getGuasti() {
		return guasti;
	}

	public void setGuasti(Set<DaeFaultDO> guasti) {
		this.guasti = guasti;
	}

}
