package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "Event", description = "Modello dati che rappresenta una emergenza di tipo cardiaco da poter asservire")
@JsonInclude(Include.NON_NULL)
public class Event implements Serializable {

	private String id;

	private String telefono;

	private String info;

	private String coRiferimento;

	private Calendar timestamp;

	private String riferimento;

	private GPSLocation coordinate;

	private String civico;

	private String indirizzo;

	private String comune;

	private String provincia;

	private String luogo;

	private String note;

	private String cartellino;

	private Integer tempoArrivoAmbulanza;

	private CategoriaFr categoria;

	private List<Intervento> interventi;

	private List<NotificheEvento> notifiche;

	private EventManagedEnum managementStatus;

	private int accettati = 0;

	private int arrivati = 0;

	private long conteggioNotifiche = 0;

	private Boolean closed = false;

	private Calendar abortDate;

	private Calendar closeDate;

	private Date dataArrivoAmbulanza;

	private Boolean defibrillato;

	private String noteDAE;

	private Date frAcceptDate;

	private Date frCloseDate;

	public Event() {
		// TODO Auto-generated constructor stub
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

	public CategoriaFr getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaFr categoria) {
		this.categoria = categoria;
	}

	public String getCoRiferimento() {
		return coRiferimento;
	}

	public void setCoRiferimento(String coRiferimento) {
		this.coRiferimento = coRiferimento;
	}

	public GPSLocation getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(GPSLocation coordinate) {
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

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public String getRiferimento() {
		return riferimento;
	}

	public String getCartellino() {
		return cartellino;
	}

	public void setCartellino(String cartellino) {
		this.cartellino = cartellino;
	}

	public void setRiferimento(String riferimento) {
		this.riferimento = riferimento;
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

	public List<Intervento> getInterventi() {
		return interventi;
	}

	public void setInterventi(List<Intervento> interventi) {
		this.interventi = interventi;
	}

	public EventManagedEnum getManagementStatus() {
		return managementStatus;
	}

	public void setManagementStatus(EventManagedEnum managementStatus) {
		this.managementStatus = managementStatus;
	}

	public List<NotificheEvento> getNotifiche() {
		return notifiche;
	}

	public void setNotifiche(List<NotificheEvento> notifiche) {
		this.notifiche = notifiche;
	}

	public int getAccettati() {
		return accettati;
	}

	public void setAccettati(int accettati) {
		this.accettati = accettati;
	}

	/**
	 * @return the closed
	 */
	public Boolean getClosed() {
		return closed;
	}

	/**
	 * @param closed
	 *            the closed to set
	 */
	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", telefono=" + telefono + ", info=" + info + ", coRiferimento=" + coRiferimento
				+ ", timestamp=" + timestamp + ", riferimento=" + riferimento + ", coordinate=" + coordinate
				+ ", civico=" + civico + ", indirizzo=" + indirizzo + ", comune=" + comune + ", provincia=" + provincia
				+ ", note=" + note + ", cartellino=" + cartellino + ", tempoArrivoAmbulanza=" + tempoArrivoAmbulanza
				+ ", categoria=" + categoria + "]";
	}

	public Calendar getAbortDate() {
		return abortDate;
	}

	public void setAbortDate(Calendar abortDate) {
		this.abortDate = abortDate;
	}

	public Calendar getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Calendar closeDate) {
		this.closeDate = closeDate;
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

	public long getConteggioNotifiche() {
		return conteggioNotifiche;
	}

	public void setConteggioNotifiche(long conteggioNotifiche) {
		this.conteggioNotifiche = conteggioNotifiche;
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

	public int getArrivati() {
		return arrivati;
	}

	public void setArrivati(int arrivati) {
		this.arrivati = arrivati;
	}

	public Date getFrAcceptDate() {
		return frAcceptDate;
	}

	public void setFrAcceptDate(Date frAcceptDate) {
		this.frAcceptDate = frAcceptDate;
	}

	public Date getFrCloseDate() {
		return frCloseDate;
	}

	public void setFrCloseDate(Date frCloseDate) {
		this.frCloseDate = frCloseDate;
	}

}
