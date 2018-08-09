package it.eng.areas.ems.sdodaeservices.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_CERTIFICATO_DAE")
public class CertificatoDaeDO {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DAE")
	private DaeDO dae;

	private String descrizione;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "IMMAGINE")
	private ImageDO immagine;

	@Column(name = "DATA_CONSEGUIMENTO")
	private Date dataConseguimento;

	@Column(name = "RILASCIATO_DA")
	private String rilasciatoDa;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_SCADENZA")
	private Date dataScadenza;

	@Column(name = "EMAIL_SCADENZA")
	private String emailScadenza;

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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public ImageDO getImmagine() {
		return immagine;
	}

	public void setImmagine(ImageDO immagine) {
		this.immagine = immagine;
	}

	public Date getDataConseguimento() {
		return dataConseguimento;
	}

	public void setDataConseguimento(Date dataConseguimento) {
		this.dataConseguimento = dataConseguimento;
	}

	public String getRilasciatoDa() {
		return rilasciatoDa;
	}

	public void setRilasciatoDa(String rilasciatoDa) {
		this.rilasciatoDa = rilasciatoDa;
	}

	public Date getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String getEmailScadenza() {
		return emailScadenza;
	}

	public void setEmailScadenza(String emailScadenza) {
		this.emailScadenza = emailScadenza;
	}

	public CertificatoDaeDO() {
		super();
		// TODO Auto-generated constructor stub
	}

}
