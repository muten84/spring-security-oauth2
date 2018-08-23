package it.eng.areas.ems.sdodaeservices.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DAE_DASHBOARD_POSITION")
public class DashboardPositionDO {

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "UTENTE_ID")
	private String utenteId;

	@Column(name = "ELEMENT_ID")
	private String elementId;

	@Column(name = "X")
	private double x;

	@Column(name = "ORDERING")
	private int ordering;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUtenteId() {
		return utenteId;
	}

	public void setUtenteId(String utenteId) {
		this.utenteId = utenteId;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

}
