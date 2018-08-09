package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Calendar;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_EVENT")
public class EventDO implements Serializable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;

	private String telefono;

	private String info;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIME_STAMP")
	private Calendar timestamp;

	@Column(name = "DATA")
	private String data;

	@Column(name = "CLOSED")
	private Boolean closed = false;

	private String riferimento;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "COORDINATE")
	private GPSLocationDO coordinate;

	@Column(name = "CIVICO")
	private String civico;

	@Column(name = "INDIRIZZO")
	private String indirizzo;

	@Column(name = "COMUNE")
	private String comune;

	@Column(name = "PROVINCIA")
	private String provincia;

	@Column(name = "LUOGO")
	private String luogo;

	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Set<NotificheEventoDO> notifiche;

	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Set<InterventoDO> interventi;

	private String note;

	@Column(name = "TEMPO_ARRIVO_AMBULANZA")
	private Integer tempoArrivoAmbulanza;

	@Column(name = "CO_RIFERIMENTO")
	private String coRiferimento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORIA_ID")
	private CategoriaFrDO categoriaFr;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CLOSE_DATE")
	private Calendar closeDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ABORT_DATE")
	private Calendar abortDate;

	@Column(name = "CARTELLINO")
	private String cartellino;

	@Column(name = "ACCEPTED_RESPONDERS")
	private Integer acceptedResponders;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ARRIVO_AMBULANZA")
	private Date dataArrivoAmbulanza;

	@Column(name = "DEFIBRILLATO")
	private Boolean defibrillato;

	@Column(name = "NOTE_DAE")
	private String noteDAE;

	public EventDO() {
		// TODO Auto-generated constructor stub
	}

	public CategoriaFrDO getCategoriaFr() {
		return categoriaFr;
	}

	public Set<NotificheEventoDO> getNotifiche() {
		return notifiche;
	}

	public void setNotifiche(Set<NotificheEventoDO> notifiche) {
		this.notifiche = notifiche;
	}

	public Calendar getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Calendar closeDate) {
		this.closeDate = closeDate;
	}

	public Calendar getAbortDate() {
		return abortDate;
	}

	public void setAbortDate(Calendar abortDate) {
		this.abortDate = abortDate;
	}

	public void setCategoriaFr(CategoriaFrDO categoriaFr) {
		this.categoriaFr = categoriaFr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public String getRiferimento() {
		return riferimento;
	}

	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
	}

	public GPSLocationDO getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(GPSLocationDO coordinate) {
		this.coordinate = coordinate;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getTempoArrivoAmbulanza() {
		return tempoArrivoAmbulanza;
	}

	public void setTempoArrivoAmbulanza(Integer tempoArrivoAmbulanza) {
		this.tempoArrivoAmbulanza = tempoArrivoAmbulanza;
	}

	public String getCoRiferimento() {
		return coRiferimento;
	}

	public void setCoRiferimento(String coRiferimento) {
		this.coRiferimento = coRiferimento;
	}

	public String getCartellino() {
		return cartellino;
	}

	public void setCartellino(String cartellino) {
		this.cartellino = cartellino;
	}

	public String getData() {
		return data;
	}

	public Boolean getClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

	public Integer getAcceptedResponders() {
		return acceptedResponders;
	}

	public void setAcceptedResponders(Integer accepted) {
		this.acceptedResponders = accepted;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Set<InterventoDO> getInterventi() {
		return interventi;
	}

	public void setInterventi(Set<InterventoDO> interventi) {
		this.interventi = interventi;
	}

	public Date getDataArrivoAmbulanza() {
		return dataArrivoAmbulanza;
	}

	public void setDataArrivoAmbulanza(Date dataArrivoAmbulanza) {
		this.dataArrivoAmbulanza = dataArrivoAmbulanza;
	}

	public String getLuogo() {
		return luogo;
	}

	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}

	public Boolean getDefibrillato() {
		return defibrillato;
	}

	public void setDefibrillato(Boolean defibrillato) {
		this.defibrillato = defibrillato;
	}

	public String getNoteDAE() {
		return noteDAE;
	}

	public void setNoteDAE(String noteDAE) {
		this.noteDAE = noteDAE;
	}

	@Override
	public String toString() {
		return "EventDO [id=" + id + ", telefono=" + telefono + ", info=" + info + ", timestamp=" + timestamp
				+ ", data=" + data + ", closed=" + closed + ", riferimento=" + riferimento + ", coordinate="
				+ coordinate + ", civico=" + civico + ", indirizzo=" + indirizzo + ", comune=" + comune + ", provincia="
				+ provincia + ", luogo=" + luogo + ", notifiche=" + notifiche + ", interventi=" + interventi + ", note="
				+ note + ", tempoArrivoAmbulanza=" + tempoArrivoAmbulanza + ", coRiferimento=" + coRiferimento
				+ ", categoriaFr=" + categoriaFr + ", closeDate=" + closeDate + ", abortDate=" + abortDate
				+ ", cartellino=" + cartellino + ", acceptedResponders=" + acceptedResponders + ", dataArrivoAmbulanza="
				+ dataArrivoAmbulanza + "]";
	}

}
