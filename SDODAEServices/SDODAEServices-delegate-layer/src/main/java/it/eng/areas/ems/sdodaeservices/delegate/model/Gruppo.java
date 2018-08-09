package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.List;

public class Gruppo {

	private String id;

	private String nome;

	private List<GruppoProvincia> province;

	private List<GruppoComune> comuni;

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

	public List<GruppoProvincia> getProvince() {
		return province;
	}

	public void setProvince(List<GruppoProvincia> province) {
		this.province = province;
	}

	public List<GruppoComune> getComuni() {
		return comuni;
	}

	public void setComuni(List<GruppoComune> comuni) {
		this.comuni = comuni;
	}

}
