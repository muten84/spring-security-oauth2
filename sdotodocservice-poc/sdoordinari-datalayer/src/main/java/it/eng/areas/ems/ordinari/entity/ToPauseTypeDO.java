package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TO_PAUSE_TYPE database table.
 * 
 */
@Entity
@Table(name="TO_PAUSE_TYPE")
public class ToPauseTypeDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	private String description;

	public ToPauseTypeDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}