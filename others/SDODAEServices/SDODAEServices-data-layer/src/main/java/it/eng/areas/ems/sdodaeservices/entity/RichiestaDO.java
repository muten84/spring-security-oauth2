package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "DAE_RICHIESTA")
public class RichiestaDO implements Serializable {

	@Id
	@Column(name= "ID")
	private String id;

	private Integer codiceRichiesta;

	private Date data;

	private GPSLocationDO luogo;

	private String nota;

	private FirstResponderDO apertoDa;

	private StatoRichiestaDO statoRichiesta;

	private ProvinciaDO[] inviaAlertA;

	private Integer tempoMinimoAlert;

	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public int getCodiceRichiesta() {
		return codiceRichiesta;
	}

	
	public void setCodiceRichiesta(int codiceRichiesta) {
		this.codiceRichiesta = codiceRichiesta;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public GPSLocationDO getLuogo() {
		return luogo;
	}

	public void setLuogo(GPSLocationDO luogo) {
		this.luogo = luogo;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public FirstResponderDO getApertoDa() {
		return apertoDa;
	}

	public void setApertoDa(FirstResponderDO apertoDa) {
		this.apertoDa = apertoDa;
	}

	public StatoRichiestaDO getStatoRichiesta() {
		return statoRichiesta;
	}

	public void setStatoRichiesta(StatoRichiestaDO statoRichiesta) {
		this.statoRichiesta = statoRichiesta;
	}

	public ProvinciaDO[] getInviaAlertA() {
		return inviaAlertA;
	}

	public void setInviaAlertA(ProvinciaDO[] inviaAlertA) {
		this.inviaAlertA = inviaAlertA;
	}

	public int getTempoMinimoAlert() {
		return tempoMinimoAlert;
	}

	public void setTempoMinimoAlert(int tempoMinimoAlert) {
		this.tempoMinimoAlert = tempoMinimoAlert;
	}

	public RichiestaDO() {
		super();
		// TODO Auto-generated constructor stub
	}


	

}
