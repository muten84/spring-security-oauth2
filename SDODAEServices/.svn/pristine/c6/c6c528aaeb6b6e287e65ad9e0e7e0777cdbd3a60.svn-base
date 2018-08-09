package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_COLORE")
public class ColoreDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORIA_FR")
	private CategoriaFrDO categoriaFr;

	@Column(name = "NOME_COLORE")
	private String nomeColore;

	public ColoreDO() {

		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNomeColore() {
		return nomeColore;
	}

	public void setNomeColore(String nomeColore) {
		this.nomeColore = nomeColore;
	}

	public CategoriaFrDO getCategoriaFr() {
		return categoriaFr;
	}

	public void setCategoriaFr(CategoriaFrDO categoriaFr) {
		this.categoriaFr = categoriaFr;
	}

}
