package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_GRUPPO")
public class GruppoDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@Column(name = "NOME")
	private String nome;

	@ManyToMany(mappedBy = "gruppi")
	private Set<UtenteDO> utenti;

	@OneToMany(mappedBy = "gruppo", cascade = { CascadeType.ALL }, orphanRemoval = true)
	private Set<GruppoProvinciaDO> province;

	@OneToMany(mappedBy = "gruppo", cascade = { CascadeType.ALL }, orphanRemoval = true)
	private Set<GruppoComuneDO> comuni;

	public GruppoDO() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<UtenteDO> getUtenti() {
		return utenti;
	}

	public void setUtenti(Set<UtenteDO> utenti) {
		this.utenti = utenti;
	}

	public Set<GruppoProvinciaDO> getProvince() {
		return province;
	}

	public void setProvince(Set<GruppoProvinciaDO> province) {
		this.province = province;
	}

	public Set<GruppoComuneDO> getComuni() {
		return comuni;
	}

	public void setComuni(Set<GruppoComuneDO> comuni) {
		this.comuni = comuni;
	}

}
