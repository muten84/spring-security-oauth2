package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DAE_STATIC_DATA")
public class StaticDataDO implements Serializable {

	@Id
	@Column(name = "ID")
	private String id;

	private String type;

	private String value;

	private Integer ordering;

	public StaticDataDO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public StaticDataDO(String id) {
		super();
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getOrdering() {
		return ordering;
	}

	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}

}
