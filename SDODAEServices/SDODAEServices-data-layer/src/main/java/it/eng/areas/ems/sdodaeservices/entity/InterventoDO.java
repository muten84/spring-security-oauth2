package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_INTERVENTO")
public class InterventoDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EVENT_ID")
	private EventDO event;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CREAZIONE")
	private Date dataCreazione;

	@Column(name = "DURATA")
	private Integer durata;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ACCETTAZIONE")
	private Date dataAccettazione;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_RIFIUTO")
	private Date dataRifiuto;

	@OneToMany(mappedBy = "intervento", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Set<InterventoCoordDO> coordinate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CHIUSURA")
	private Date dataChiusura;

	@Column(name = "CHIUSO_DA")
	private String chiusoDa;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ANNULLAMENTO")
	private Date dataAnnullamento;

	@Column(name = "ANNULLATO_DA")
	private String annullatoDa;

	@Column(name = "RIFIUTATO_DA")
	private String rifiutatoDa;

	@Column(name = "ACCETTATO_DA")
	private String accettatoDa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ESEGUITO_DA")
	private FirstResponderDO eseguitoDa;

	@Column(name = "OSSERVAZIONE")
	private String Osservazione;

	@Column(name = "LATITUDINE_ARRIVO")
	private Double latitudineArrivo;

	@Column(name = "LONGITUDINE_ARRIVO")
	private Double longitudineArrivo;

	public InterventoDO() {
		super();
	}

	public InterventoDO(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EventDO getEvent() {
		return event;
	}

	public void setEvent(EventDO event) {
		this.event = event;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Integer getDurata() {
		return durata;
	}

	public void setDurata(Integer durata) {
		this.durata = durata;
	}

	public Date getDataAccettazione() {
		return dataAccettazione;
	}

	public void setDataAccettazione(Date dataAccettazione) {
		this.dataAccettazione = dataAccettazione;
	}

	public String getAccettatoDa() {
		return accettatoDa;
	}

	public void setAccettatoDa(String accettatoDa) {
		this.accettatoDa = accettatoDa;
	}

	public Date getDataRifiuto() {
		return dataRifiuto;
	}

	public void setDataRifiuto(Date dataRifiuto) {
		this.dataRifiuto = dataRifiuto;
	}

	public String getRifiutatoDa() {
		return rifiutatoDa;
	}

	public void setRifiutatoDa(String rifiutatoDa) {
		this.rifiutatoDa = rifiutatoDa;
	}

	public String getOsservazione() {
		return Osservazione;
	}

	public void setOsservazione(String osservazione) {
		Osservazione = osservazione;
	}

	public FirstResponderDO getEseguitoDa() {
		return eseguitoDa;
	}

	public void setEseguitoDa(FirstResponderDO eseguitoDa) {
		this.eseguitoDa = eseguitoDa;
	}

	public Date getDataAnnullamento() {
		return dataAnnullamento;
	}

	public void setDataAnnullamento(Date dataAnnullamento) {
		this.dataAnnullamento = dataAnnullamento;
	}

	public String getAnnullatoDa() {
		return annullatoDa;
	}

	public void setAnnullatoDa(String annullatoDa) {
		this.annullatoDa = annullatoDa;
	}

	public Date getDataChiusura() {
		return dataChiusura;
	}

	public void setDataChiusura(Date dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

	public String getChiusoDa() {
		return chiusoDa;
	}

	public void setChiusoDa(String chiusoDa) {
		this.chiusoDa = chiusoDa;
	}

	public Set<InterventoCoordDO> getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Set<InterventoCoordDO> coordinate) {
		this.coordinate = coordinate;
	}

	public Double getLatitudineArrivo() {
		return latitudineArrivo;
	}

	public void setLatitudineArrivo(Double latitudineArrivo) {
		this.latitudineArrivo = latitudineArrivo;
	}

	public Double getLongitudineArrivo() {
		return longitudineArrivo;
	}

	public void setLongitudineArrivo(Double longitudineArrivo) {
		this.longitudineArrivo = longitudineArrivo;
	}

}
