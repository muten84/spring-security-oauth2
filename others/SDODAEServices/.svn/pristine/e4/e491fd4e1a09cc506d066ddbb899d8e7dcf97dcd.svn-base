package it.eng.areas.ems.sdodaeservices.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_ENTE_CERTIFICATORE")
public class EnteCertificatoreDO {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	private String descrizione;

	@Column(name = "ONLY_FOR_MED")
	private boolean onlyForMed;

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

	public boolean isOnlyForMed() {
		return onlyForMed;
	}

	public void setOnlyForMed(boolean onlyForMed) {
		this.onlyForMed = onlyForMed;
	}

}
