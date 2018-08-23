package it.eng.areas.ems.sdodaeservices.delegate.model;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "Dispositivo", description = "Modello dati che rappresenta un Dispositivo")
public class Dispositivo {

	private String id;
	private String marca;
	private String modello;
	private String os;
	private String pushToken;

	public Dispositivo() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
