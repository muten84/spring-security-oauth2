package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TO_DEALLOCATE_REASON database table.
 * 
 */
@Entity
@Table(name="TO_DEALLOCATE_REASON")
public class ToDeallocateReasonDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	private String description;

	@Column(name="TRAVEL_COMPLETED")
	private BigDecimal travelCompleted;

	public ToDeallocateReasonDO() {
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

	public BigDecimal getTravelCompleted() {
		return this.travelCompleted;
	}

	public void setTravelCompleted(BigDecimal travelCompleted) {
		this.travelCompleted = travelCompleted;
	}

}