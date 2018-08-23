package it.luigibifulco.oauth2.poc.jpa.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class City implements Serializable {

	private static final long serialVersionUID = 1L;

//	@Id
//	@SequenceGenerator(name = "city_generator", sequenceName = "city_sequence", initialValue = 23)
//	@GeneratedValue(generator = "city_generator")
	@Id
	@GeneratedValue(generator = "city_generator", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "city_generator", sequenceName = "city_sequence", allocationSize = 10)
	private Long id;

	@Column(nullable = false)
	@NotNull
	@Size(min = 2)
	private String name;

	@Column(nullable = false)
	@NotNull
	private String state;

	@Column(nullable = false)
	@NotNull
	private String country;

	@Column(nullable = false)
	@NotNull
	private String map;

	@Transient
	private int counter;

	public City() {
	}

	public City(String name, String country) {
		this.name = name;
		this.country = country;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public String getState() {
		return this.state;
	}

	public String getCountry() {
		return this.country;
	}

	public String getMap() {
		return this.map;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	@Override
	public String toString() {
		return getName() + "," + getState() + "," + getCountry();
	}

}
