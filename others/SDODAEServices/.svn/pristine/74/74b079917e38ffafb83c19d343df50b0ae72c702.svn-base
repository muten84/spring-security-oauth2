package it.eng.areas.ems.sdodaeservices.entity.filter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import it.eng.areas.ems.sdodaeservices.entity.FRStatoProfiloEnum;

@XmlType
public class FirstResponderFilterDO extends TerritorialFilterDO implements Serializable {

	protected String id;

	protected String nome;

	protected String cognome;

	protected List<String> categoriaFrIDS;

	private Boolean isAvailable;

	private Boolean haveCoords;

	private Boolean daAttivare;

	private String logon;

	private String categoriaDescription;

	private Integer coordinatesFromMinutes;

	private FRStatoProfiloEnum statoProfilo;

	private Boolean deleted;

	private Boolean notInIntervention;

	protected String comune;

	protected String province;

	protected String email;

	private boolean logonLike;

	private Date dataIscrizioneMax;

	public FirstResponderFilterDO() {
		super();
	}

	public Boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public FirstResponderFilterDO(String fetchrule) {
		super(fetchrule);
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

	public Boolean getDaAttivare() {
		return daAttivare;
	}

	public void setDaAttivare(Boolean daAttivare) {
		this.daAttivare = daAttivare;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getLogon() {
		return logon;
	}

	public void setLogon(String logon) {
		this.logon = logon;
	}

	public String getCategoriaDescription() {
		return categoriaDescription;
	}

	public void setCategoriaDescription(String categoriaDescription) {
		this.categoriaDescription = categoriaDescription;
	}

	public List<String> getCategoriaFrIDS() {
		return categoriaFrIDS;
	}

	public void setCategoriaFrIDS(List<String> categoriaFrIDS) {
		this.categoriaFrIDS = categoriaFrIDS;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Integer getCoordinatesFromMinutes() {
		return coordinatesFromMinutes;
	}

	public void setCoordinatesFromMinutes(Integer coordinatesFromMinutes) {
		this.coordinatesFromMinutes = coordinatesFromMinutes;
	}

	public FRStatoProfiloEnum getStatoProfilo() {
		return statoProfilo;
	}

	public void setStatoProfilo(FRStatoProfiloEnum statoProfilo) {
		this.statoProfilo = statoProfilo;
	}

	public Boolean isNotInIntervention() {
		return notInIntervention;
	}

	public void setNotInIntervention(Boolean notInIntervention) {
		this.notInIntervention = notInIntervention;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public Boolean getNotInIntervention() {
		return notInIntervention;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public Boolean getHaveCoords() {
		return haveCoords;
	}

	public void setHaveCoords(Boolean haveCoords) {
		this.haveCoords = haveCoords;
	}

	public boolean isLogonLike() {
		return logonLike;
	}

	public void setLogonLike(boolean logonLike) {
		this.logonLike = logonLike;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataIscrizioneMax() {
		return dataIscrizioneMax;
	}

	public void setDataIscrizioneMax(Date dataIscrizioneMax) {
		this.dataIscrizioneMax = dataIscrizioneMax;
	}

}
