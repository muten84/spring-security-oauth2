package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import it.eng.areas.ems.sdodaeservices.entity.DaeFaultDO;
import it.eng.areas.ems.sdodaeservices.entity.DaeStatoEnum;

@ApiModel(value = "DAE", description = "Modello dati che rappresenta un Defibrillatore Automatico Elettronico")
@JsonInclude(Include.NON_NULL)
public class Dae extends TerritorialEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private boolean operativo;
	private String matricola;
	private String modello;
	private String tipo;
	private String alias;

	private CertificatoDae certificatoDae;

	private TipoStruttura tipologiaStruttura;
	private String nomeSede;
	private Responsabile responsabile;
	private String ubicazione;
	private List<Disponibile> disponibilita = new ArrayList<Disponibile>();
	private String notediAccessoallaSede;
	private GPSLocation gpsLocation;
	private Image immagine;
	private Calendar scadenzaDae;
	private List<ProgrammaManutenzione> programmiManutenzione = new ArrayList<ProgrammaManutenzione>();
	private String noteGenerali;
	private Stato currentStato;
	private Distance distance;
	private Utente creatoDA;

	private DaeStatoEnum statoValidazione;
	private Date dataInserimento;
	private Date timestampInserimento;
	private String tipologiaDisponibilita;
	private Boolean disponibilitaPermanente;
	private Boolean disponibile;
	private Boolean disponibilitaIndefinita;
	private List<DaeFaultDO> guasti;

	private Date dataValidazione;
	private String utenteValidazione;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isOperativo() {
		return operativo;
	}

	public void setOperativo(boolean operativo) {
		this.operativo = operativo;
	}

	public String getMatricola() {
		return matricola;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
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

	public String getAlias() {
		return alias;
	}

	public Date getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Date getTimestampInserimento() {
		return timestampInserimento;
	}

	public void setTimestampInserimento(Date timestampInserimento) {
		this.timestampInserimento = timestampInserimento;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public CertificatoDae getCertificatoDae() {
		return certificatoDae;
	}

	public void setCertificatoDae(CertificatoDae certificatoDae) {
		this.certificatoDae = certificatoDae;
	}

	public TipoStruttura getTipologiaStruttura() {
		return tipologiaStruttura;
	}

	public void setTipologiaStruttura(TipoStruttura tipologiaStruttura) {
		this.tipologiaStruttura = tipologiaStruttura;
	}

	public String getNomeSede() {
		return nomeSede;
	}

	public void setNomeSede(String nomeSede) {
		this.nomeSede = nomeSede;
	}

	public Responsabile getResponsabile() {
		return responsabile;
	}

	public void setResponsabile(Responsabile responsabile) {
		this.responsabile = responsabile;
	}

	public String getUbicazione() {
		return ubicazione;
	}

	public void setUbicazione(String ubicazione) {
		this.ubicazione = ubicazione;
	}

	public String getNotediAccessoallaSede() {
		return notediAccessoallaSede;
	}

	public void setNotediAccessoallaSede(String notediAccessoallaSede) {
		this.notediAccessoallaSede = notediAccessoallaSede;
	}

	public GPSLocation getGpsLocation() {
		return gpsLocation;
	}

	public void setGpsLocation(GPSLocation gpsLocation) {
		this.gpsLocation = gpsLocation;
	}

	public Image getImmagine() {
		return immagine;
	}

	public void setImmagine(Image immagine) {
		this.immagine = immagine;
	}

	public Calendar getScadenzaDae() {
		return scadenzaDae;
	}

	public void setScadenzaDae(Calendar scadenzaDae) {
		this.scadenzaDae = scadenzaDae;
	}

	public List<ProgrammaManutenzione> getProgrammiManutenzione() {
		return programmiManutenzione;
	}

	public void setProgrammiManutenzione(List<ProgrammaManutenzione> programmiManutenzione) {
		this.programmiManutenzione = programmiManutenzione;
	}

	public String getNoteGenerali() {
		return noteGenerali;
	}

	public void setNoteGenerali(String noteGenerali) {
		this.noteGenerali = noteGenerali;
	}

	public Utente getCreatoDA() {
		return creatoDA;
	}

	public void setCreatoDA(Utente creatoDA) {
		this.creatoDA = creatoDA;
	}

	public List<Disponibile> getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(List<Disponibile> disponibilita) {
		this.disponibilita = disponibilita;
	}

	public Distance getDistance() {
		return distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	public Stato getCurrentStato() {
		return currentStato;
	}

	public void setCurrentStato(Stato currentStato) {
		this.currentStato = currentStato;
	}

	public DaeStatoEnum getStatoValidazione() {
		return statoValidazione;
	}

	public void setStatoValidazione(DaeStatoEnum statoValidazione) {
		this.statoValidazione = statoValidazione;
	}

	public List<DaeFaultDO> getGuasti() {
		return guasti;
	}

	public void setGuasti(List<DaeFaultDO> guasti) {
		this.guasti = guasti;
	}

	public Boolean getDisponibilitaPermanente() {
		return disponibilitaPermanente;
	}

	public void setDisponibilitaPermanente(Boolean disponibilitaPermanente) {
		this.disponibilitaPermanente = disponibilitaPermanente;
	}

	public Boolean getDisponibile() {
		return disponibile;
	}

	public String getTipologiaDisponibilita() {
		return tipologiaDisponibilita;
	}

	public void setTipologiaDisponibilita(String tipologiaDisponibilita) {
		this.tipologiaDisponibilita = tipologiaDisponibilita;
	}

	public void setDisponibile(Boolean disponibile) {
		this.disponibile = disponibile;
	}

	public Boolean getDisponibilitaIndefinita() {
		return disponibilitaIndefinita;
	}

	public void setDisponibilitaIndefinita(Boolean disponibilitaIndefinita) {
		this.disponibilitaIndefinita = disponibilitaIndefinita;
	}

	public Date getDataValidazione() {
		return dataValidazione;
	}

	public void setDataValidazione(Date dataValidazione) {
		this.dataValidazione = dataValidazione;
	}

	public String getUtenteValidazione() {
		return utenteValidazione;
	}

	public void setUtenteValidazione(String utenteValidazione) {
		this.utenteValidazione = utenteValidazione;
	}

	@Override
	public String getProvince() {
		if (getGpsLocation() != null && //
				getGpsLocation().getComune() != null && //
				getGpsLocation().getComune().getProvincia() != null) {
			return getGpsLocation().getComune().getProvincia().getNomeProvincia();
		} else {
			return "";
		}
	}

	@Override
	public String getMunicipality() {
		if (getGpsLocation() != null && //
				getGpsLocation().getComune() != null) {
			return getGpsLocation().getComune().getNomeComune();
		} else {
			return "";
		}
	}

}
