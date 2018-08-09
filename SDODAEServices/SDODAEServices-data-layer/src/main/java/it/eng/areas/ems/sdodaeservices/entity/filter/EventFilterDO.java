package it.eng.areas.ems.sdodaeservices.entity.filter;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class EventFilterDO extends TerritorialFilterDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String coRiferimento;

	private Calendar dataDA;

	private Calendar dataA;

	private String comune;

	private String indirizzo;

	private String cartellinoEvento;

	private String nomeFR;

	private String cognomeFR;

	private String frID;

	private String categoria;

	private boolean accepted;

	private boolean managed;

	public EventFilterDO() {
		// TODO Auto-generated constructor stub
	}

	public EventFilterDO(String fetchrule) {
		super(fetchrule);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCoRiferimento() {
		return coRiferimento;
	}

	public void setCoRiferimento(String coRiferimento) {
		this.coRiferimento = coRiferimento;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getCartellinoEvento() {
		return cartellinoEvento;
	}

	public void setCartellinoEvento(String cartellinoEvento) {
		this.cartellinoEvento = cartellinoEvento;
	}

	public String getNomeFR() {
		return nomeFR;
	}

	public void setNomeFR(String nomeFR) {
		this.nomeFR = nomeFR;
	}

	public String getCognomeFR() {
		return cognomeFR;
	}

	public void setCognomeFR(String cognomeFR) {
		this.cognomeFR = cognomeFR;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public Calendar getDataDA() {
		return dataDA;
	}

	public void setDataDA(Calendar dataDA) {
		this.dataDA = dataDA;
	}

	public Calendar getDataA() {
		return dataA;
	}

	public void setDataA(Calendar dataA) {
		this.dataA = dataA;
	}

	public String getFrID() {
		return frID;
	}

	public void setFrID(String frID) {
		this.frID = frID;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public boolean isManaged() {
		return managed;
	}

	public void setManaged(boolean managed) {
		this.managed = managed;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

}
