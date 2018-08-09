package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_FIRST_RESPONDER")
public class FirstResponderDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "UTENTE")
	private UtenteDO utente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORIA_FR")
	private CategoriaFrDO categoriaFr;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATO_PROFILO")
	private FRStatoProfiloEnum statoProfilo = FRStatoProfiloEnum.IN_ATTESA_DI_ATTIVAZIONE;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "CERTIFICATO_FR")
	private CertificatoFrDO certificatoFr;

	@Column(name = "RICHIESTE_ESEGUITE")
	private Integer richiesteEseguite;

	@Column(name = "INTERVENTI_ESEGUITI")
	private Integer interventiEseguiti;

	@OneToMany(mappedBy = "eseguitoDa", fetch = FetchType.LAZY)
	private Set<InterventoDO> interventi;

	@Column(name = "NUMERO_CELLULARE")
	private String numCellulare;

	@Column(name = "DISPONIBILE")
	private Boolean disponibile;

	@Column(name = "DO_NOT_DISTURB")
	private Boolean doNotDisturb;

	@Column(name = "DO_NOT_DISTURB_FROM")
	private String doNotDisturbFrom;

	@Column(name = "DO_NOT_DISTURB_TO")
	private String doNotDisturbTo;

	@Column(name = "SILENT")
	private Boolean silent;

	@Column(name = "SILENT_FROM")
	private String silentFrom;

	@Column(name = "SILENT_TO")
	private String silentTo;

	@Column(name = "NOTE")
	private String note;

	@Column(name = "PRIVACY_ACCEPTED_TS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date privacyAcceptedDate;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "DISPOSITIVO")
	private DispositiviDO dispositivo;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "PROFILO_SANITARIO")
	private ProfiloSanitarioDO profiloSanitario;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "LAST_POSITION")
	private LocationDO lastPosition;

	@OneToMany(mappedBy = "firstResponder", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Set<QuestionarioFirstResponderDO> questionarioFirstResponder;

	@Column(name = "DATA_ISCRIZIONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataIscrizione;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "MEDICO_FR")
	private MedicoFrDO medicoFr;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROFESSIONE_ID")
	private ProfessioneDO professione;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "DAE_COMUNE_COMPETENZA", joinColumns = {
			@JoinColumn(name = "FIRST_RESPONDER_ID") }, inverseJoinColumns = { @JoinColumn(name = "COMUNE_ID") })
	private Set<ComuneDO> comuniCompetenza;

	@OneToMany(mappedBy = "firstResponder", fetch = FetchType.LAZY)
	private Set<NotificheEventoDO> notifiche;

	@Column(name = "DATA_VALIDAZIONE")
	private Date dataValidazione;

	@Column(name = "UTENTE_VALIDAZIONE")
	private String utenteValidazione;

	public FirstResponderDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DispositiviDO getDispositivo() {
		return dispositivo;
	}

	public ProfessioneDO getProfessione() {
		return professione;
	}

	public void setProfessione(ProfessioneDO professione) {
		this.professione = professione;
	}

	public void setDispositivo(DispositiviDO dispositivo) {
		this.dispositivo = dispositivo;
	}

	public UtenteDO getUtente() {
		return utente;
	}

	public Date getDataIscrizione() {
		return dataIscrizione;
	}

	public void setDataIscrizione(Date dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}

	public void setUtente(UtenteDO utente) {
		this.utente = utente;
	}

	public String getNumCellulare() {
		return numCellulare;
	}

	public void setNumCellulare(String numCellulare) {
		this.numCellulare = numCellulare;
	}

	public CategoriaFrDO getCategoriaFr() {
		return categoriaFr;
	}

	public void setCategoriaFr(CategoriaFrDO categoriaFr) {
		this.categoriaFr = categoriaFr;
	}

	public FRStatoProfiloEnum getStatoProfilo() {
		return statoProfilo;
	}

	public void setStatoProfilo(FRStatoProfiloEnum statoProfilo) {
		this.statoProfilo = statoProfilo;
	}

	public CertificatoFrDO getCertificatoFr() {
		return certificatoFr;
	}

	public void setCertificatoFr(CertificatoFrDO certificatoFr) {
		this.certificatoFr = certificatoFr;
	}

	public Integer getRichiesteEseguite() {
		return richiesteEseguite;
	}

	public void setRichiesteEseguite(Integer richiesteEseguite) {
		this.richiesteEseguite = richiesteEseguite;
	}

	public Integer getInterventiEseguiti() {
		return interventiEseguiti;
	}

	public Boolean getDisponibile() {
		return disponibile;
	}

	public void setDisponibile(Boolean disponibile) {
		this.disponibile = disponibile;
	}

	public void setInterventiEseguiti(Integer interventiEseguiti) {
		this.interventiEseguiti = interventiEseguiti;
	}

	public ProfiloSanitarioDO getProfiloSanitario() {
		return profiloSanitario;
	}

	public void setProfiloSanitario(ProfiloSanitarioDO profiloSanitario) {
		this.profiloSanitario = profiloSanitario;
	}

	public LocationDO getLastPosition() {
		return lastPosition;
	}

	public void setLastPosition(LocationDO lastPosition) {
		this.lastPosition = lastPosition;
	}

	public Set<QuestionarioFirstResponderDO> getQuestionarioFirstResponder() {
		return questionarioFirstResponder;
	}

	public void setQuestionarioFirstResponder(Set<QuestionarioFirstResponderDO> questionarioFirstResponder) {
		this.questionarioFirstResponder = questionarioFirstResponder;
	}

	public MedicoFrDO getMedicoFr() {
		return medicoFr;
	}

	public void setMedicoFr(MedicoFrDO medicoFr) {
		this.medicoFr = medicoFr;
	}

	public Set<InterventoDO> getInterventi() {
		return interventi;
	}

	public Date getPrivacyAcceptedDate() {
		return privacyAcceptedDate;
	}

	public void setPrivacyAcceptedDate(Date privacyAcceptedDate) {
		this.privacyAcceptedDate = privacyAcceptedDate;
	}

	public void setInterventi(Set<InterventoDO> interventi) {
		this.interventi = interventi;
	}

	public Boolean getDoNotDisturb() {
		return doNotDisturb;
	}

	public void setDoNotDisturb(Boolean doNotDisturb) {
		this.doNotDisturb = doNotDisturb;
	}

	public String getDoNotDisturbFrom() {
		return doNotDisturbFrom;
	}

	public void setDoNotDisturbFrom(String doNotDisturbFrom) {
		this.doNotDisturbFrom = doNotDisturbFrom;
	}

	public String getDoNotDisturbTo() {
		return doNotDisturbTo;
	}

	public void setDoNotDisturbTo(String doNotDisturbTo) {
		this.doNotDisturbTo = doNotDisturbTo;
	}

	public Set<ComuneDO> getComuniCompetenza() {
		return comuniCompetenza;
	}

	public void setComuniCompetenza(Set<ComuneDO> comuniCompetenza) {
		this.comuniCompetenza = comuniCompetenza;
	}

	public Set<NotificheEventoDO> getNotifiche() {
		return notifiche;
	}

	public void setNotifiche(Set<NotificheEventoDO> notifiche) {
		this.notifiche = notifiche;
	}

	public Boolean getSilent() {
		return silent;
	}

	public void setSilent(Boolean silent) {
		this.silent = silent;
	}

	public String getSilentFrom() {
		return silentFrom;
	}

	public void setSilentFrom(String silentFrom) {
		this.silentFrom = silentFrom;
	}

	public String getSilentTo() {
		return silentTo;
	}

	public void setSilentTo(String silentTo) {
		this.silentTo = silentTo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDataValidazione() {
		return dataValidazione;
	}

	public void setDataValidazione(Date dataValidazione) {
		this.dataValidazione = dataValidazione;
	}

	public String getUtenteValidazione() {
		return utenteValidazione;
	}

	public void setUtenteValidazione(String utenteValidazione) {
		this.utenteValidazione = utenteValidazione;
	}

}
