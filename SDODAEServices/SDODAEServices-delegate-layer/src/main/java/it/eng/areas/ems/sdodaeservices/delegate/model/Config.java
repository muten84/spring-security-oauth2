package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.io.Serializable;

import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;

public class Config implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private String parametro;

	private String valore;

	// valore usato solo dall'interfaccia grafica
	private String descrizione;

	public Config() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
		// carico la descrizione dall'enum
		if (descrizione == null) {
			for (ParametersEnum en : ParametersEnum.values()) {
				if (en.name().equals(parametro)) {
					descrizione = en.getDescription();
				}
			}
		}
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
