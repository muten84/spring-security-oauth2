package it.eng.areas.ems.sdodaeservices.entity.filter;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import it.eng.areas.ems.sdodaeservices.entity.DaeFaultStatoEnum;
import it.eng.areas.ems.sdodaeservices.entity.DaeStatoEnum;
import it.eng.areas.ems.sdodaeservices.entity.TipoManutenzioneEnum;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class DaeFilterDO extends TerritorialFilterDO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private List<String> ids;

	private String posizione;

	private String comune;

	private String modello;

	private String tipo;

	private String indirizzo;

	private Calendar scadenzaDae;

	private String nomeSede;

	private Integer positionRange;

	private String tipologiaStruttura;

	private DaeStatoEnum statoValidazione;

	private String notInStatoDAE;

	private Boolean statoVisible;

	private Boolean statoVisible118;

	private Boolean operativo;

	private Date scadenzaManutenzioneDa;

	private Date scadenzaManutenzioneA;

	private TipoManutenzioneEnum tipoManutenzione;

	protected Boolean isInFault;

	protected DaeFaultStatoEnum faultState;

	private LocationFilterDO location;

	private Integer pageSize;

	private Integer fromIndex;

	private String currentStato;

	private String province;

	private String email;

	protected TipoManutenzioneEnum[] tipoManutenzioneList;

	public DaeFilterDO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DaeFilterDO(String fetchrule) {
		super(fetchrule);
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

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Calendar getScadenzaDae() {
		return scadenzaDae;
	}

	public String getNomeSede() {
		return nomeSede;
	}

	public void setNomeSede(String nomeSede) {
		this.nomeSede = nomeSede;
	}

	public void setScadenzaDae(Calendar scadenzaDae) {
		this.scadenzaDae = scadenzaDae;
	}

	public Integer getPositionRange() {
		return positionRange;
	}

	public void setPositionRange(Integer positionRange) {
		this.positionRange = positionRange;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getNotInStatoDAE() {
		return notInStatoDAE;
	}

	public void setNotInStatoDAE(String notInStatoDAE) {
		this.notInStatoDAE = notInStatoDAE;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public Boolean getIsInFault() {
		return isInFault;
	}

	public void setIsInFault(Boolean isInFault) {
		this.isInFault = isInFault;
	}

	public DaeFaultStatoEnum getFaultState() {
		return faultState;
	}

	public void setFaultState(DaeFaultStatoEnum faultState) {
		this.faultState = faultState;
	}

	public LocationFilterDO getLocation() {
		return location;
	}

	public void setLocation(LocationFilterDO location) {
		this.location = location;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(Integer fromIndex) {
		this.fromIndex = fromIndex;
	}

	public Boolean getStatoVisible() {
		return statoVisible;
	}

	public void setStatoVisible(Boolean statoVisible) {
		this.statoVisible = statoVisible;
	}

	public String getCurrentStato() {
		return currentStato;
	}

	public void setCurrentStato(String currentStato) {
		this.currentStato = currentStato;
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

	public Boolean getStatoVisible118() {
		return statoVisible118;
	}

	public void setStatoVisible118(Boolean statoVisible118) {
		this.statoVisible118 = statoVisible118;
	}

	public TipoManutenzioneEnum[] getTipoManutenzioneList() {
		return tipoManutenzioneList;
	}

	public void setTipoManutenzioneList(TipoManutenzioneEnum[] tipoManutenzioneList) {
		this.tipoManutenzioneList = tipoManutenzioneList;
	}

	@Override
	public String toString() {
		return "DaeFilterDO [id=" + id + ", posizione=" + posizione + ", comune=" + comune + ", modello=" + modello
				+ ", tipo=" + tipo + ", indirizzo=" + indirizzo + ", scadenzaDae=" + scadenzaDae + ", nomeSede="
				+ nomeSede + ", positionRange=" + positionRange + ", tipologiaStruttura=" + tipologiaStruttura
				+ ", statoValidazione=" + statoValidazione + ", notInStatoDAE=" + notInStatoDAE + ", operativo="
				+ operativo + ", scadenzaManutenzioneDa=" + scadenzaManutenzioneDa + ", scadenzaManutenzioneA="
				+ scadenzaManutenzioneA + ", tipoManutenzione=" + tipoManutenzione + "]";
	}

}
