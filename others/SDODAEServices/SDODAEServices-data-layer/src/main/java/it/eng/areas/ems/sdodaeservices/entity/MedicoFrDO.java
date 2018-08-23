package it.eng.areas.ems.sdodaeservices.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_MEDICO_FR")
public class MedicoFrDO {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@Column(name = "DATA_LAUREA")
	private Date dataLaurea;

	@Column(name = "NUM_ISCRIZIONE_ORDINE")
	private String numIscrizioneOrdine;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDataLaurea() {
		return dataLaurea;
	}

	public void setDataLaurea(Date dataLaurea) {
		this.dataLaurea = dataLaurea;
	}

	public String getNumIscrizioneOrdine() {
		return numIscrizioneOrdine;
	}

	public void setNumIscrizioneOrdine(String numIscrizioneOrdine) {
		this.numIscrizioneOrdine = numIscrizioneOrdine;
	}

}
