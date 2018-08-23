package it.eng.areas.ems.sdodaeservices.entity.filter;

public class UtenteFilterDO extends TerritorialFilterDO {

	private String id;

	private String notId;

	private String nome;

	private String cognome;

	private String email;

	private String logon;

	private boolean lognEqual = false;

	private boolean emailLike = false;

	private String ruoloNome;

	private String gruppoNome;

	public UtenteFilterDO() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogon() {
		return logon;
	}

	public void setLogon(String logon) {
		this.logon = logon;
	}

	public String getRuoloNome() {
		return ruoloNome;
	}

	public void setRuoloNome(String ruoloNome) {
		this.ruoloNome = ruoloNome;
	}

	public String getGruppoNome() {
		return gruppoNome;
	}

	public void setGruppoNome(String gruppoNome) {
		this.gruppoNome = gruppoNome;
	}

	public boolean isLognEqual() {
		return lognEqual;
	}

	public void setLognEqual(boolean lognEqual) {
		this.lognEqual = lognEqual;
	}

	public String getNotId() {
		return notId;
	}

	public void setNotId(String notId) {
		this.notId = notId;
	}

	public boolean isEmailLike() {
		return emailLike;
	}

	public void setEmailLike(boolean emailLike) {
		this.emailLike = emailLike;
	}

}
