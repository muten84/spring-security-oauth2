package it.eng.areas.ems.sdodaeservices.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DAE_RUOLO")
public class RuoloDO implements Serializable {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	private String nome;

	private String descrizione;

	@Column(name = "GLOBAL_SEARCH")
	private boolean globalSearch;

	@Column(name = "GLOBAL_SAVE")
	private boolean globalSave;

	@Column(name = "VISIBLE")
	private boolean visible;

	public RuoloDO() {
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public boolean isGlobalSearch() {
		return globalSearch;
	}

	public void setGlobalSearch(boolean globalSearch) {
		this.globalSearch = globalSearch;
	}

	public boolean isGlobalSave() {
		return globalSave;
	}

	public void setGlobalSave(boolean globalSave) {
		this.globalSave = globalSave;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
