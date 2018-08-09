package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_DISPOSITIVI")
public class DispositiviDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "dispositivo")
	private FirstResponderDO firstResponder;

	private String marca;

	private String modello;

	@Column(name = "PUSH_TOKEN")
	private String pushToken;

	private String os;

	@Column(name = "ULTIMO_ACCESSO")
	private Calendar ultimoAccesso;

	public DispositiviDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FirstResponderDO getFirstResponder() {
		return firstResponder;
	}

	public void setFirstResponder(FirstResponderDO firstResponder) {
		this.firstResponder = firstResponder;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public Calendar getUltimoAccesso() {
		return ultimoAccesso;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setUltimoAccesso(Calendar ultimoAccesso) {
		this.ultimoAccesso = ultimoAccesso;
	}

}
