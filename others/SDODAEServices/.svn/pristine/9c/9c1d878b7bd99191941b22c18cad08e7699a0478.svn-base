package it.eng.areas.ems.sdodaeservices.delegate.model.filter;

import java.util.Calendar;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import it.eng.areas.ems.sdodaeservices.entity.DaeFaultStatoEnum;
import it.eng.areas.ems.sdodaeservices.entity.DaeStatoEnum;
import it.eng.areas.ems.sdodaeservices.entity.DistanceEnum;
import it.eng.areas.ems.sdodaeservices.entity.TipoManutenzioneEnum;

@ApiModel(value = "DaeFilter", description = "Modello dati utile alla ricerca dei DAE")
public class DaeFilter extends TerritorialFilter {

	private String id;
	protected String posizione;
	protected String comune;
	protected String modello;
	protected String tipo;
	protected Calendar scadenzaDae;

	protected String tipologiaStruttura;
	protected String indirizzo;
	private String nomeSede;
	private LocationFilter location;
	private Integer pageSize;
	private Integer fromIndex;

	private DistanceEnum distanceRange;

	protected DaeStatoEnum statoValidazione;

	private Boolean operativo;
	private Boolean statoVisible;

	protected Date scadenzaManutenzioneDa;
	protected Date scadenzaManutenzioneA;
	protected TipoManutenzioneEnum tipoManutenzione;

	protected TipoManutenzioneEnum[] tipoManutenzioneList;

	protected Boolean isInFault;

	protected DaeFaultStatoEnum faultState;

	private String currentStato;

	private String province;

	private String email;

	public DaeFilter() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPosizione() {
		return posizione;
	}

	public void setPosizione(String posizione) {
		this.posizione = posizione;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getModello() {
		return modello;
	}

	public void setModello(String modello) {
		this.modello = modello;
	}

	public String getTipo() {
		return tipo;
	}

	public Integer getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(Integer fromIndex) {
		this.fromIndex = fromIndex;
	}

	public String getNomeSede() {
		return nomeSede;
	}

	public void setNomeSede(String nomeSede) {
		this.nomeSede = nomeSede;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Calendar getScadenzaDae() {
		return scadenzaDae;
	}

	public void setScadenzaDae(Calendar scadenzaDae) {
		this.scadenzaDae = scadenzaDae;
	}

	public LocationFilter getLocation() {
		return location;
	}

	public void setLocation(LocationFilter location) {
		this.location = location;
	}

	public DistanceEnum getDistanceRange() {
		return distanceRange;
	}

	public void setDistanceRange(DistanceEnum distanceRange) {
		this.distanceRange = distanceRange;
	}

	public String getTipologiaStruttura() {
		return tipologiaStruttura;
	}

	public void setTipologiaStruttura(String tipologiaStruttura) {
		this.tipologiaStruttura = tipologiaStruttura;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public DaeStatoEnum getStatoValidazione() {
		return statoValidazione;
	}

	public void setStatoValidazione(DaeStatoEnum statoValidazione) {
		this.statoValidazione = statoValidazione;
	}

	public Date getScadenzaManutenzioneDa() {
		return scadenzaManutenzioneDa;
	}

	public void setScadenzaManutenzioneDa(Date scadenzaManutenzioneDa) {
		this.scadenzaManutenzioneDa = scadenzaManutenzioneDa;
	}

	public Date getScadenzaManutenzioneA() {
		return scadenzaManutenzioneA;
	}

	public void setScadenzaManutenzioneA(Date scadenzaManutenzioneA) {
		this.scadenzaManutenzioneA = scadenzaManutenzioneA;
	}

	public Boolean getOperativo() {
		return operativo;
	}

	public void setOperativo(Boolean operativo) {
		this.operativo = operativo;
	}

	public TipoManutenzioneEnum getTipoManutenzione() {
		return tipoManutenzione;
	}

	public void setTipoManutenzione(TipoManutenzioneEnum tipoManutenzione) {
		this.tipoManutenzione = tipoManutenzione;
	}

	public DaeFaultStatoEnum getFaultState() {
		return faultState;
	}

	public void setFaultState(DaeFaultStatoEnum faultState) {
		this.faultState = faultState;
	}

	public String getCurrentStato() {
		return currentStato;
	}

	public void setCurrentStato(String currentStato) {
		this.currentStato = currentStato;
	}

	public Boolean getIsInFault() {
		return isInFault;
	}

	public void setIsInFault(Boolean isInFault) {
		this.isInFault = isInFault;
	}

	public Boolean getStatoVisible() {
		return statoVisible;
	}

	public void setStatoVisible(Boolean statoVisible) {
		this.statoVisible = statoVisible;
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

	public TipoManutenzioneEnum[] getTipoManutenzioneList() {
		return tipoManutenzioneList;
	}

	public void setTipoManutenzioneList(TipoManutenzioneEnum... tipoManutenzioneList) {
		this.tipoManutenzioneList = tipoManutenzioneList;
	}

}
