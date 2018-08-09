package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_RESPONSABILE")
public class ResponsabileDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DAE")
	private DaeDO dae;

	private String nome;

	private String cognome;

	private String telefono;

	private String email;

	@Column(name = "INDIRIZZO_RESIDENZA")
	private String indirizzoResidenza;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMUNE_RESIDENZA")
	private ComuneDO comuneResidenza;

	@Column(name = "CODICE_FISCALE")
	private String codiceFiscale;

	@Column(name = "DATA_NASCITA")
	private Calendar dataNascita;

	private boolean whatsApp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DaeDO getDae() {
		return dae;
	}

	public void setDae(DaeDO dae) {
		this.dae = dae;
	}

	public String getNome() {
		return nome;
	}

	public Calendar getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Calendar dataNascita) {
		this.dataNascita = dataNascita;
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

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIndirizzoResidenza() {
		return indirizzoResidenza;
	}

	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
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

	public boolean isWhatsApp() {
		return whatsApp;
	}

	public void setWhatsApp(boolean whatsApp) {
		this.whatsApp = whatsApp;
	}

	public ResponsabileDO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
