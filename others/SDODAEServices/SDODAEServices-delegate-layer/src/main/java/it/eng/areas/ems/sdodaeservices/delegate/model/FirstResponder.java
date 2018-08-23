package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import it.eng.areas.ems.sdodaeservices.entity.FRStatoProfiloEnum;

@ApiModel(value = "FirstResponder", description = "Modello dati che rappresenta una First Responder")
public class FirstResponder extends Utente {

	private String id;

	private String numCellulare;
	private CategoriaFr categoriaFr;
	private FRStatoProfiloEnum statoProfilo;
	private CertificatoFr certificatoFr;
	private Integer richiesteEseguite;
	private Integer interventiEseguiti;
	private ProfiloSanitario profiloSanitario;
	private GPSLocation lastPosition;
	private Date privacyAcceptedDate;
	private Boolean disponibile = false;
	private Date dataIscrizione;
	private Dispositivo dispositivo;
	private MedicoFr medicoFr;
	private Professione professione;

	private Boolean doNotDisturb;
	private String doNotDisturbFrom;
	private String doNotDisturbTo;
	private Boolean silent;
	private String silentFrom;
	private String silentTo;

	private String note;

	private List<QuestionarioFirstResponder> questionarioFirstResponder;

	private List<Comune> comuniCompetenza;

	private Date dataValidazione;
	private String utenteValidazione;

	// Mauro - questi campi servono solo per l'invio della mail, non
	// valorizzarli altrimenti
	private String confirmURL;

	public FirstResponder() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public Date getDataIscrizione() {
		return dataIscrizione;
	}

	public void setDataIscrizione(Date dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumCellulare() {
		return numCellulare;
	}

	public void setNumCellulare(String numCellulare) {
		this.numCellulare = numCellulare;
	}

	public CategoriaFr getCategoriaFr() {
		return categoriaFr;
	}

	public Professione getProfessione() {
		return professione;
	}

	public void setProfessione(Professione professione) {
		this.professione = professione;
	}

	public void setCategoriaFr(CategoriaFr categoriaFr) {
		this.categoriaFr = categoriaFr;
	}

	public Boolean getDisponibile() {
		return disponibile;
	}

	public void setDisponibile(Boolean disponibile) {
		this.disponibile = disponibile;
	}

	public FRStatoProfiloEnum getStatoProfilo() {
		return statoProfilo;
	}

	public Dispositivo getDispositivo() {
		return dispositivo;
	}

	public void setDispositivo(Dispositivo dispositivo) {
		this.dispositivo = dispositivo;
	}

	public void setStatoProfilo(FRStatoProfiloEnum statoProfilo) {
		this.statoProfilo = statoProfilo;
	}

	public CertificatoFr getCertificatoFr() {
		return certificatoFr;
	}

	public void setCertificatoFr(CertificatoFr certificatoFr) {
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

	public void setInterventiEseguiti(Integer interventiEseguiti) {
		this.interventiEseguiti = interventiEseguiti;
	}

	public ProfiloSanitario getProfiloSanitario() {
		return profiloSanitario;
	}

	public void setProfiloSanitario(ProfiloSanitario profiloSanitario) {
		this.profiloSanitario = profiloSanitario;
	}

	public GPSLocation getLastPosition() {
		return lastPosition;
	}

	public Date getPrivacyAcceptedDate() {
		return privacyAcceptedDate;
	}

	public void setPrivacyAcceptedDate(Date privacyAcceptedDate) {
		this.privacyAcceptedDate = privacyAcceptedDate;
	}

	public void setLastPosition(GPSLocation lastPosition) {
		this.lastPosition = lastPosition;
	}

	public List<QuestionarioFirstResponder> getQuestionarioFirstResponder() {
		return questionarioFirstResponder;
	}

	public void setQuestionarioFirstResponder(List<QuestionarioFirstResponder> questionarioFirstResponder) {
		this.questionarioFirstResponder = questionarioFirstResponder;
	}

	public MedicoFr getMedicoFr() {
		return medicoFr;
	}

	public void setMedicoFr(MedicoFr medicoFr) {
		this.medicoFr = medicoFr;
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

	public List<Comune> getComuniCompetenza() {
		return comuniCompetenza;
	}

	public void setComuniCompetenza(List<Comune> comuniCompetenza) {
		this.comuniCompetenza = comuniCompetenza;
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

	public String getConfirmURL() {
		return confirmURL;
	}

	public void setConfirmURL(String confirmURL) {
		this.confirmURL = confirmURL;
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

	@Override
	public String toString() {
		return "FirstResponder [id=" + id + ", numCellulare=" + numCellulare + ", categoriaFr=" + categoriaFr
				+ ", statoProfilo=" + statoProfilo + ", certificatoFr=" + certificatoFr + ", richiesteEseguite="
				+ richiesteEseguite + ", interventiEseguiti=" + interventiEseguiti + ", profiloSanitario="
				+ profiloSanitario + ", lastPosition=" + lastPosition + ", privacyAcceptedDate=" + privacyAcceptedDate
				+ ", disponibile=" + disponibile + ", dataIscrizione=" + dataIscrizione + ", dispositivo=" + dispositivo
				+ ", medicoFr=" + medicoFr + ", professione=" + professione + ", doNotDisturb=" + doNotDisturb
				+ ", doNotDisturbFrom=" + doNotDisturbFrom + ", doNotDisturbTo=" + doNotDisturbTo
				+ ", questionarioFirstResponder=" + questionarioFirstResponder + ", comuniCompetenza="
				+ comuniCompetenza + "]";
	}

}
