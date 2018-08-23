package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_MESSAGGIO")
public class MessaggioDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	private String testo;

	@Column(name = "INVIO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date invio;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "DAE_MESSAGGIO_FIRST_RESPONDER", //
			joinColumns = { @JoinColumn(name = "MESSAGGIO_ID", nullable = false, updatable = false) }, //
			inverseJoinColumns = { @JoinColumn(name = "FIRST_RESPONDER_ID", nullable = false, updatable = false) })
	private Set<FirstResponderDO> responders;

	@Formula("(select count(*) from DAE_MESSAGGIO_FIRST_RESPONDER mfr where mfr.MESSAGGIO_ID = ID)")
	private Integer destinatari;

	private Integer deleted = 0;
	
	@Column(name = "STATO")
	private String stato;		
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public Date getInvio() {
		return invio;
	}

	public void setInvio(Date invio) {
		this.invio = invio;
	}

	public Set<FirstResponderDO> getResponders() {
		return responders;
	}

	public void setResponders(Set<FirstResponderDO> responders) {
		this.responders = responders;
	}

	public Integer getDestinatari() {
		return destinatari;
	}

	public void setDestinatari(Integer destinatari) {
		this.destinatari = destinatari;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
	
	
	

}
