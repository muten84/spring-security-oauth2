package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "DAE_SCADENZA_DAE")
public class ScadenzaDAEDO implements Serializable {

   @Id
   @Column(name= "ID")
	private String id;

	private String dae;

	private String tipoScadenza;

	private String dataScadenza;

	private String statoscadenza;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getdae() {
		return dae;
	}

	public void setdae(String dae) {
		this.dae = dae;
	}

	public String getTipoScadenza() {
		return tipoScadenza;
	}

	public void setTipoScadenza(String tipoScadenza) {
		this.tipoScadenza = tipoScadenza;
	}

	public String getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String getStatoscadenza() {
		return statoscadenza;
	}

	public void setStatoscadenza(String statoscadenza) {
		this.statoscadenza = statoscadenza;
	}

	public ScadenzaDAEDO() {
		super();
		// TODO Auto-generated constructor stub
	}


	

}
