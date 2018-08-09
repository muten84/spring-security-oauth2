package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TS_SERVICE_ID database table.
 * 
 */
@Entity
@Table(name="TS_SERVICE_ID")
public class TsServiceIdDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TsServiceIdPKDO id;

	public TsServiceIdDO() {
	}

	public TsServiceIdPKDO getId() {
		return this.id;
	}

	public void setId(TsServiceIdPKDO id) {
		this.id = id;
	}

}