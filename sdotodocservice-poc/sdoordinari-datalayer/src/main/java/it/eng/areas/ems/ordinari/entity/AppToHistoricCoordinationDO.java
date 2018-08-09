package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the APP_TO_HISTORIC_COORDINATION database table.
 * 
 */
@Entity
@Table(name="APP_TO_HISTORIC_COORDINATION")
public class AppToHistoricCoordinationDO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private BigDecimal historic;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_HISTORIC")
	private Date lastHistoric;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPDATE")
	private Date lastUpdate;

	public AppToHistoricCoordinationDO() {
	}

	public BigDecimal getHistoric() {
		return this.historic;
	}

	public void setHistoric(BigDecimal historic) {
		this.historic = historic;
	}

	public Date getLastHistoric() {
		return this.lastHistoric;
	}

	public void setLastHistoric(Date lastHistoric) {
		this.lastHistoric = lastHistoric;
	}

	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}