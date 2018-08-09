package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_STRADE")
public class StradeDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMUNE")
	private ComuneDO comune;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "DAE_LOCALITA_STRADE", //
			joinColumns = { @JoinColumn(columnDefinition = "LOCALITA") }, //
			inverseJoinColumns = { @JoinColumn(columnDefinition = "STRADA") })
	private Set<LocalitaDO> localita;

	@Column(name = "DELETED")
	private Boolean deleted;

	private String name;

	private String note;

	public StradeDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ComuneDO getComune() {
		return comune;
	}

	public void setComune(ComuneDO comune) {
		this.comune = comune;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
