package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_RESET_PASSWORD")
public class ResetPasswordDO implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum Stato {
		ATTESA, ACCETTATO, RIFIUTATO
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	private String token;

	private String ip;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_RICHIESTA")
	private Date dataRichiesta;

	@Enumerated(EnumType.STRING)
	private Stato stato;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "UTENTE_ID")
	private UtenteDO utente;

	public ResetPasswordDO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	public Stato getStato() {
		return stato;
	}

	public void setStato(Stato stato) {
		this.stato = stato;
	}

	public UtenteDO getUtente() {
		return utente;
	}

	public void setUtente(UtenteDO utente) {
		this.utente = utente;
	}

	@Override
	public String toString() {
		return "ResetPasswordDO [id=" + id + ", token=" + token + ", ip=" + ip + ", dataRichiesta=" + dataRichiesta
				+ ", stato=" + stato + ", utente=" + utente + "]";
	}

}
