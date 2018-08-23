package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.Date;

public class CertificatoFr {
	private String id;
	private String descrizione;
	private Image immagine;
	private Date dataConseguimento;
	private String rilasciatoDa;
	private Date dataScadenza;
	private String emailScadenza;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Image getImmagine() {
		return immagine;
	}

	public void setImmagine(Image immagine) {
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

}
