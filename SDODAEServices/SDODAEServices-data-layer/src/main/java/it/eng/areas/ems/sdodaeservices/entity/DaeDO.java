package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_DAE")
public class DaeDO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	private boolean operativo;

	@Column(name = "DELETED")
	private Boolean deleted = false;

	private String matricola;

	private String modello;

	private String tipo;

	private String alias;

	// @Column(name = "DISPONIBILITA_PERMANENTE")
	// private Boolean disponibilitaPermanente;

	@Column(name = "TIPOLOGIA_DISPONIBILITA")
	private String tipologiaDisponibilita;

	// @OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy
	// = "dae")
	// private CertificatoDaeDO certificatoDae;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TIPOLOGIA_STRUTTURA")
	private TipoStrutturaDO tipologiaStruttura;

	private String nomeSede;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "dae")
	private ResponsabileDO responsabile;

	private String ubicazione;

	@OneToMany(mappedBy = "dae", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	private Set<DisponibileDO> disponibilita;

	@Column(name = "NOTE_DI_ACCESSO_ALLA_SEDE")
	private String notediAccessoallaSede;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "POSIZIONE")
	private LocationDO posizione;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "IMMAGINE")
	private ImageDO immagine;

	@Temporal(TemporalType.DATE)
	@Column(name = "SCADENZA_DAE")
	private Calendar scadenzaDae;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INSERIMENTO")
	private Date dataInserimento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP_INSERIMENTO")
	private Date timestampInserimento;

	@OneToMany(mappedBy = "dae", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true)
	private Set<ProgrammaManutenzioneDO> programmiManutenzione = new HashSet<ProgrammaManutenzioneDO>();

	@Column(name = "NOTE_GENERALI")
	private String noteGenerali;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATO_DA_UTENTE")
	private UtenteDO creatoDA;

	@OneToMany(mappedBy = "dae", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Set<StatoDAEDO> storicoStatoDAE = new HashSet<StatoDAEDO>();

	@OneToMany(mappedBy = "dae", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Set<DaeFaultDO> guasti = new HashSet<DaeFaultDO>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENT_STATO_ID")
	private StatoDO currentStato;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATO_VALIDAZIONE")
	private DaeStatoEnum statoValidazione;

	@Column(name = "DATA_VALIDAZIONE")
	private Date dataValidazione;

	@Column(name = "UTENTE_VALIDAZIONE")
	private String utenteValidazione;

	public DaeDO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isOperativo() {
		return operativo;
	}

	public void setOperativo(boolean operativo) {
		this.operativo = operativo;
	}

	public String getMatricola() {
		return matricola;
	}

	public Date getDataInserimento() {
		return dataInserimento;
	}

	public UtenteDO getCreatoDA() {
		return creatoDA;
	}

	public void setCreatoDA(UtenteDO creatoDA) {
		this.creatoDA = creatoDA;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Date getTimestampInserimento() {
		return timestampInserimento;
	}

	public void setTimestampInserimento(Date timestampInserimento) {
		this.timestampInserimento = timestampInserimento;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public String getTipo() {
		return tipo;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public TipoStrutturaDO getTipologiaStruttura() {
		return tipologiaStruttura;
	}

	public void setTipologiaStruttura(TipoStrutturaDO tipologiaStruttura) {
		this.tipologiaStruttura = tipologiaStruttura;
	}

	public String getNomeSede() {
		return nomeSede;
	}

	public void setNomeSede(String nomeSede) {
		this.nomeSede = nomeSede;
	}

	public ResponsabileDO getResponsabile() {
		return responsabile;
	}

	public void setResponsabile(ResponsabileDO responsabile) {
		this.responsabile = responsabile;
	}

	public String getUbicazione() {
		return ubicazione;
	}

	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}

	public String getNotediAccessoallaSede() {
		return notediAccessoallaSede;
	}

	public void setNotediAccessoallaSede(String notediAccessoallaSede) {
		this.notediAccessoallaSede = notediAccessoallaSede;
	}

	public LocationDO getPosizione() {
		return posizione;
	}

	public void setPosizione(LocationDO posizione) {
		this.posizione = posizione;
	}

	public ImageDO getImmagine() {
		return immagine;
	}

	public void setImmagine(ImageDO immagine) {
		this.immagine = immagine;
	}

	// public CertificatoDaeDO getCertificatoDae() {
	// return certificatoDae;
	// }
	//
	// public void setCertificatoDae(CertificatoDaeDO certificatoDae) {
	// this.certificatoDae = certificatoDae;
	// }

	public Calendar getScadenzaDae() {
		return scadenzaDae;
	}

	public void setScadenzaDae(Calendar scadenzaDae) {
		this.scadenzaDae = scadenzaDae;
	}

	public String getNoteGenerali() {
		return noteGenerali;
	}

	public void setNoteGenerali(String noteGenerali) {
		this.noteGenerali = noteGenerali;
	}

	public Set<ProgrammaManutenzioneDO> getProgrammiManutenzione() {
		return programmiManutenzione;
	}

	public void setProgrammiManutenzione(Set<ProgrammaManutenzioneDO> programmiManutenzione) {
		this.programmiManutenzione = programmiManutenzione;
	}

	public Set<DisponibileDO> getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(Set<DisponibileDO> disponibilita) {
		this.disponibilita = disponibilita;
	}

	public Set<StatoDAEDO> getStoricoStatoDAE() {
		return storicoStatoDAE;
	}

	public void setStoricoStatoDAE(Set<StatoDAEDO> storicoStatoDAE) {
		this.storicoStatoDAE = storicoStatoDAE;
	}

	public StatoDO getCurrentStato() {
		return currentStato;
	}

	public void setCurrentStato(StatoDO currentStato) {
		this.currentStato = currentStato;
	}

	public DaeStatoEnum getStatoValidazione() {
		return statoValidazione;
	}

	public void setStatoValidazione(DaeStatoEnum statoValidazione) {
		this.statoValidazione = statoValidazione;
	}

	public Set<DaeFaultDO> getGuasti() {
		return guasti;
	}

	public void setGuasti(Set<DaeFaultDO> guasti) {
		this.guasti = guasti;
	}

	public String getTipologiaDisponibilita() {
		return tipologiaDisponibilita;
	}

	public void setTipologiaDisponibilita(String tipologiaDisponibilita) {
		this.tipologiaDisponibilita = tipologiaDisponibilita;
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

	// public Boolean getDisponibilitaPermanente() {
	// return disponibilitaPermanente;
	// }
	//
	// public void setDisponibilitaPermanente(Boolean disponibilitaPermanente) {
	// this.disponibilitaPermanente = disponibilitaPermanente;
	// }

}
