package it.eng.areas.ems.ordinari.entity;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TO_DISTANCES database table.
 * 
 */
@Entity
@Table(name="TO_DISTANCES")
public class ToDistanceDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;

	private String distance;

	@Column(name="PLACE1_DESCRIPTION")
	private String place1Description;

	@Column(name="PLACE1_ID")
	private String place1Id;

	@Column(name="PLACE1_TYPE")
	private String place1Type;

	@Column(name="PLACE2_DESCRIPTION")
	private String place2Description;

	@Column(name="PLACE2_ID")
	private String place2Id;

	@Column(name="PLACE2_TYPE")
	private String place2Type;

	public ToDistanceDO() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDistance() {
		return this.distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getPlace1Description() {
		return this.place1Description;
	}

	public void setPlace1Description(String place1Description) {
		this.place1Description = place1Description;
	}

	public String getPlace1Id() {
		return this.place1Id;
	}

	public void setPlace1Id(String place1Id) {
		this.place1Id = place1Id;
	}

	public String getPlace1Type() {
		return this.place1Type;
	}

	public void setPlace1Type(String place1Type) {
		this.place1Type = place1Type;
	}

	public String getPlace2Description() {
		return this.place2Description;
	}

	public void setPlace2Description(String place2Description) {
		this.place2Description = place2Description;
	}

	public String getPlace2Id() {
		return this.place2Id;
	}

	public void setPlace2Id(String place2Id) {
		this.place2Id = place2Id;
	}

	public String getPlace2Type() {
		return this.place2Type;
	}

	public void setPlace2Type(String place2Type) {
		this.place2Type = place2Type;
	}

}