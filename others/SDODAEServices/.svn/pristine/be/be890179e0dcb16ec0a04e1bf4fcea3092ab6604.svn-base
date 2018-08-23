package it.eng.areas.ems.sdodaeservices.delegate.model.filter;

import java.util.List;

import io.swagger.annotations.ApiModel;
import it.eng.areas.ems.sdodaeservices.delegate.model.GPSLocation;
import it.eng.areas.ems.sdodaeservices.entity.DistanceEnum;
import it.eng.areas.ems.sdodaeservices.entity.FRStatoProfiloEnum;

@ApiModel(value = "FirstResponderFilter", description = "Modello dati utile alla ricerca dei First Responder")
public class FirstResponderFilter extends TerritorialFilter {

	private String id;

	private String nome;

	private String cognome;

	private List<String> categoriaFrIDS;

	private Boolean daAttivare;

	private Integer coordinatesFromMinutes;

	private String categoriaDescription;

	private GPSLocation location;

	private DistanceEnum distanceRange;

	private Boolean isAvailable;

	private Boolean deleted;

	private boolean notInIntervention;

	private FRStatoProfiloEnum statoProfilo;

	protected String comune;

	protected String province;

	protected String email;

	private String logon;

	private boolean logonLike;

	public FirstResponderFilter() {
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

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCategoriaDescription() {
		return categoriaDescription;
	}

	public void setCategoriaDescription(String categoriaDescription) {
		this.categoriaDescription = categoriaDescription;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public List<String> getCategoriaFrIDS() {
		return categoriaFrIDS;
	}

	public boolean isNotInIntervention() {
		return notInIntervention;
	}

	public void setNotInIntervention(boolean notInIntervention) {
		this.notInIntervention = notInIntervention;
	}

	public void setCategoriaFrIDS(List<String> categoriaFrIDS) {
		this.categoriaFrIDS = categoriaFrIDS;
	}

	public GPSLocation getLocation() {
		return location;
	}

	public void setLocation(GPSLocation location) {
		this.location = location;
	}

	public DistanceEnum getDistanceRange() {
		return distanceRange;
	}

	public void setDistanceRange(DistanceEnum distanceRange) {
		this.distanceRange = distanceRange;
	}

	public Integer getCoordinatesFromMinutes() {
		return coordinatesFromMinutes;
	}

	public void setCoordinatesFromMinutes(Integer coordinatesFromMinutes) {
		this.coordinatesFromMinutes = coordinatesFromMinutes;
	}

	public Boolean getDaAttivare() {
		return daAttivare;
	}

	public void setDaAttivare(Boolean daAttivare) {
		this.daAttivare = daAttivare;
	}

	public FRStatoProfiloEnum getStatoProfilo() {
		return statoProfilo;
	}

	public void setStatoProfilo(FRStatoProfiloEnum statoProfilo) {
		this.statoProfilo = statoProfilo;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
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

	public String getLogon() {
		return logon;
	}

	public void setLogon(String logon) {
		this.logon = logon;
	}

}
