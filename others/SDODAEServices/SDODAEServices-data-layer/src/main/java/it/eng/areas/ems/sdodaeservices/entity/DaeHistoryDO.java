package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import it.eng.areas.ems.sdodaeservices.entity.enums.Operation;

@Entity
@Table(name = "DAE_DAE_HISTORY")
public class DaeHistoryDO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@Column(name = "DAE_ID")
	private String daeId;

	private boolean operativo;

	@Column(name = "DELETED")
	private Boolean deleted = false;

	private String matricola;

	private String modello;

	private String tipo;

	private String alias;

	@Column(name = "DISPONIBILITA_PERMANENTE")
	private Boolean disponibilitaPermanente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TIPOLOGIA_STRUTTURA")
	private TipoStrutturaDO tipologiaStruttura;

	private String nomeSede;

	@Embedded
	private ResponsabileEmbedded responsabile;

	private String ubicazione;

	@Column(name = "NOTE_DI_ACCESSO_ALLA_SEDE")
	private String notediAccessoallaSede;

	@Embedded
	private LocationEmbedded posizione;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
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

	@Column(name = "NOTE_GENERALI")
	private String noteGenerali;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENT_STATO_ID")
	private StatoDO currentStato;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATO_VALIDAZIONE")
	private DaeStatoEnum statoValidazione;

	// Dati dell'history
	@Enumerated(EnumType.STRING)
	@Column(name = "OPERATION")
	private Operation operation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UTENTE_ID")
	private UtenteDO utente;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPERATION_DATE")
	private Date operationDate;

	public DaeHistoryDO() {
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

	public ImageDO getImmagine() {
		return immagine;
	}

	public void setImmagine(ImageDO immagine) {
		this.immagine = immagine;
	}

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

	public Boolean getDisponibilitaPermanente() {
		return disponibilitaPermanente;
	}

	public void setDisponibilitaPermanente(Boolean disponibilitaPermanente) {
		this.disponibilitaPermanente = disponibilitaPermanente;
	}

	public String getDaeId() {
		return daeId;
	}

	public void setDaeId(String daeId) {
		this.daeId = daeId;
	}

	public ResponsabileEmbedded getResponsabile() {
		return responsabile;
	}

	public void setResponsabile(ResponsabileEmbedded responsabile) {
		this.responsabile = responsabile;
	}

	public LocationEmbedded getPosizione() {
		return posizione;
	}

	public void setPosizione(LocationEmbedded posizione) {
		this.posizione = posizione;
	}

	public UtenteDO getUtente() {
		return utente;
	}

	public void setUtente(UtenteDO utente) {
		this.utente = utente;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

}
