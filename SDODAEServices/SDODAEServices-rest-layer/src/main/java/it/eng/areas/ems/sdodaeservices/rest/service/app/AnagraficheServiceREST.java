package it.eng.areas.ems.sdodaeservices.rest.service.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.area118.sdogis.model.v_1_0_0.Address;
import it.eng.area118.sdogis.model.v_1_0_0.GeocodingResult;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.StradeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.GruppoDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.CategoriaFr;
import it.eng.areas.ems.sdodaeservices.delegate.model.Comune;
import it.eng.areas.ems.sdodaeservices.delegate.model.EnteCertificatore;
import it.eng.areas.ems.sdodaeservices.delegate.model.GPSLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.Gruppo;
import it.eng.areas.ems.sdodaeservices.delegate.model.Localita;
import it.eng.areas.ems.sdodaeservices.delegate.model.Professione;
import it.eng.areas.ems.sdodaeservices.delegate.model.ProfessioneFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.Provincia;
import it.eng.areas.ems.sdodaeservices.delegate.model.Ruolo;
import it.eng.areas.ems.sdodaeservices.delegate.model.StaticData;
import it.eng.areas.ems.sdodaeservices.delegate.model.Stato;
import it.eng.areas.ems.sdodaeservices.delegate.model.StatoDAE;
import it.eng.areas.ems.sdodaeservices.delegate.model.Strade;
import it.eng.areas.ems.sdodaeservices.delegate.model.TipoDisponibilita;
import it.eng.areas.ems.sdodaeservices.delegate.model.TipoProgrammaManutenzione;
import it.eng.areas.ems.sdodaeservices.delegate.model.TipoStruttura;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.ComuneFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.LocalitaFilter;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.StradeFilter;
import it.eng.areas.ems.sdodaeservices.entity.TipoDisponibilitaEnum;
import it.eng.areas.ems.sdodaeservices.entity.TipoManutenzioneEnum;
import it.eng.areas.ems.sdodaeservices.rest.exception.AppException;
import it.eng.areas.ems.sdodaeservices.rest.model.ReverseGeocodingResult;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;
import it.eng.areas.ems.sdodaeservices.rest.service.GeocodeService;
import it.esel.nue112.centauro.model.Province;

@Component
@Path("/anagraficheService")
@Api(value = "/anagraficheService", protocols = "http", description = "Servizio dedicato alla gestione delle Anagrafiche")
public class AnagraficheServiceREST {

	private Logger logger = LoggerFactory.getLogger(AnagraficheServiceREST.class);

	@Autowired
	protected AnagraficheDelegateService anagraficheDelegateService;

	@Value("${dae.provinces}")
	protected String[] area;

	@Autowired
	protected GruppoDelegateService gruppoDelegateService;

	@Autowired
	private GeocodeService geocodeService;

	@PostConstruct
	public void init() {
		logger.info("DAE Provinces " + Arrays.toString(area));
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllProvince")
	@ApiOperation(value = "getAllProvince", notes = "Metodo che restituisce la lista completa delle Province d'italia")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista di tutte le province", responseContainer = "List", response = Province.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllProvince() throws AppException {
		try {

			List<Provincia> province = anagraficheDelegateService.getAllProvince();
			province.sort(new Comparator<Provincia>() {

				@Override
				public int compare(Provincia o1, Provincia o2) {
					return o1.getNomeProvincia().compareTo(o2.getNomeProvincia());
				}

			});
			return Response.ok(province).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllProvince", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllProvinceByCompetence")
	@ApiOperation(value = "getAllProvinceByCompetence", notes = "Metodo che restituisce la lista delle Province da utilizzare sulla funzionalità dei comuni di competenza del FR")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista delle province in base all'area configurata sul backend", responseContainer = "List", response = Province.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllProvinceByCompetence() throws AppException {
		try {
			List<Provincia> province = anagraficheDelegateService.getProvinceByArea(area);
			province.sort(new Comparator<Provincia>() {

				@Override
				public int compare(Provincia o1, Provincia o2) {
					return o1.getNomeProvincia().compareTo(o2.getNomeProvincia());
				}

			});
			return Response.ok(province).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllProvinceByCompetence", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllStatoDae")
	@ApiOperation(value = "getAllStatoDae", notes = "Metodo che restituisce la lista completa degli stati dei DAE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista di tutti gli stati dei DAE", responseContainer = "List", response = StatoDAE.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllStatoDae() throws AppException {
		try {

			List<StatoDAE> statoDaeList = anagraficheDelegateService.getAllStatoDae();
			statoDaeList.sort(new Comparator<StatoDAE>() {

				@Override
				public int compare(StatoDAE o1, StatoDAE o2) {
					return o1.getStato().getNome().compareTo(o2.getStato().getNome());
				}

			});
			return Response.ok(statoDaeList).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllStatoDae", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllTipoManutenzione")
	@ApiOperation(value = "getAllTipoManutenzione", notes = "Metodo che restituisce la lista delle tipologie di manutenzione ")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista di tutti gli stati dei ", responseContainer = "List", response = TipoProgrammaManutenzione.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllTipoManutenzione() throws AppException {
		try {

			List<TipoProgrammaManutenzione> tmpList = new ArrayList<TipoProgrammaManutenzione>();

			for (TipoManutenzioneEnum tp : TipoManutenzioneEnum.values()) {
				TipoProgrammaManutenzione tpm = new TipoProgrammaManutenzione();
				tpm.setId(tp.name());
				tpm.setDescrizione(tp.getDescription());
				tmpList.add(tpm);
			}

			return Response.ok(tmpList).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllTipoManutenzione", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllTipoDisponibilita")
	@ApiOperation(value = "getAllTipoDisponibilita")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista di tutti gli stati dei ", responseContainer = "List", response = TipoDisponibilita.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllTipoDisponibilita() throws AppException {
		try {

			List<TipoDisponibilita> tmpList = new ArrayList<TipoDisponibilita>();

			for (TipoDisponibilitaEnum tp : TipoDisponibilitaEnum.values()) {
				TipoDisponibilita tpm = new TipoDisponibilita();
				tpm.setId(tp.name());
				tpm.setDescrizione(tp.getDescription());
				tmpList.add(tpm);
			}

			return Response.ok(tmpList).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllTipoDisponibilita", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllStato")
	@ApiOperation(value = "getAllStato", notes = "Metodo che restituisce la lista completa degli stati ")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista di tutti gli stati dei ", responseContainer = "List", response = Stato.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllStato() throws AppException {
		try {

			List<Stato> statoList = anagraficheDelegateService.getAllStato();
			return Response.ok(statoList).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllStato", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllProfessione")
	@ApiOperation(value = "getAllProfessione", notes = "Metodo che restituisce la lista completa delle Professioni")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista di tutte le professioni", responseContainer = "List", response = Professione.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllProfessione() throws AppException {
		try {

			List<Professione> professioneList = anagraficheDelegateService.getAllProfessione();
			professioneList.sort(new Comparator<Professione>() {

				@Override
				public int compare(Professione o1, Professione o2) {
					return o1.getDescrizione().compareTo(o2.getDescrizione());
				}

			});
			return Response.ok(professioneList).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllProfessione", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("searchProfessioneByFilter")
	@ApiOperation(value = "searchProfessioneByFilter", notes = "Metodo che restituisce la lista delle Professioni filtrate")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista delle professioni", responseContainer = "List", response = Professione.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response searchProfessioneByFilter(
			@ApiParam(value = "Bean contenente il filtro di ricerca") ProfessioneFilter filter) throws AppException {
		try {

			List<Professione> professioneList = anagraficheDelegateService.searchProfessioneByFilter(filter);
			professioneList.sort(new Comparator<Professione>() {

				@Override
				public int compare(Professione o1, Professione o2) {
					return o1.getDescrizione().compareTo(o2.getDescrizione());
				}

			});
			return Response.ok(professioneList).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchProfessioneByFilter", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllEnteCertificatore")
	@ApiOperation(value = "getAllEnteCertificatore", notes = "Metodo che restituisce la lista completa degli Enti Certificatori")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista di tutti gli Enti Certificatori", responseContainer = "List", response = EnteCertificatore.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllEnteCertificatore() throws AppException {
		try {

			List<EnteCertificatore> enteCertificatoreList = anagraficheDelegateService.getAllEnteCertificatore();
			return Response.ok(enteCertificatoreList).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllEnteCertificatore", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllEnteCertificatoreForMedico")
	@ApiOperation(value = "getAllEnteCertificatoreForMedico", notes = "Metodo che restituisce la lista completa degli Enti Certificatori per i Medici")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista di tutti gli Enti Certificatori", responseContainer = "List", response = EnteCertificatore.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllEnteCertificatoreForMedico() throws AppException {
		try {

			List<EnteCertificatore> enteCertificatoreList = anagraficheDelegateService
					.getAllEnteCertificatoreForMedico();
			return Response.ok(enteCertificatoreList).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllEnteCertificatore", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllCategory")
	@ApiOperation(value = "getAllCategory", notes = "Metodo che restituisce la lista completa delle categorie di First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista  completa delle categorie di First Responder", responseContainer = "List", response = CategoriaFr.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllCategory() throws AppException {
		try {

			List<CategoriaFr> catFR = anagraficheDelegateService.getAllCategoriaFR();
			catFR.sort(new Comparator<CategoriaFr>() {

				@Override
				public int compare(CategoriaFr o1, CategoriaFr o2) {
					return o1.getDescrizione().compareTo(o2.getDescrizione());
				}

			});
			return Response.ok(catFR).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllCateogorie", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllCategorie")
	@ApiOperation(value = "getAllCategorie", notes = "Metodo che restituisce la lista completa delle categorie di First Responder")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista  completa delle categorie di First Responder", responseContainer = "List", response = CategoriaFr.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllCategorie() throws AppException {
		try {

			List<CategoriaFr> catFR = anagraficheDelegateService.getAllCategoriaFRForApp();
			catFR.sort(new Comparator<CategoriaFr>() {

				@Override
				public int compare(CategoriaFr o1, CategoriaFr o2) {
					return o1.getDescrizione().compareTo(o2.getDescrizione());
				}

			});
			return Response.ok(catFR).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllCateogorie", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("searchComuniByFilter")
	@ApiOperation(value = "searchComuniByFilter", notes = "Metodo che restituisce la lista dei comuni filtrati")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista dei comuni filtrati", responseContainer = "List", response = Comune.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllComuniByFilter(@ApiParam(value = "Bean di filtering dei comuni") ComuneFilter filter)
			throws AppException {
		try {

			List<Comune> comuni = anagraficheDelegateService.getComuniByFilter(filter);
			// comuni.sort(new Comparator<Comune>() {
			//
			// @Override
			// public int compare(Comune o1, Comune o2) {
			// return o1.getNomeComune().compareTo(o2.getNomeComune());
			// }
			//
			// });
			return Response.ok(comuni).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllComuniByFilter", e);
			return Response.serverError().build();
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("geocodeAddress")
	@ApiOperation(value = "geocodeAddress", notes = "Metodo che restituisce comune, provincia ed indirizzo più vicini alle coordinate passate")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista dei comuni filtrati", response = GeocodingResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response geocodeAddress(@ApiParam(value = "Bean con posizione di reverse Geocoding") Address address) {
		try {
			GeocodingResult result = geocodeService.geocodeAddress(address);

			return Response.ok(result).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING geocodeAddress", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("reverseGeocoding")
	@ApiOperation(value = "reverseGeocoding", notes = "Metodo che restituisce comune, provincia ed indirizzo più vicini alle coordinate passate")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista dei comuni filtrati", response = ReverseGeocodingResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response reverseGeocoding(@ApiParam(value = "Bean con posizione di reverse Geocoding") GPSLocation location)
			throws AppException {
		try {
			return Response.ok(geocodeService.reverseGeocoding(location)).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING reverseGeocoding", e);
			return Response.serverError().build();
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("searchStradeByFilter")
	@ApiOperation(value = "searchStradeByFilter", notes = "Metodo che restituisce la lista delle strade filtrate")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista dei comuni filtrati", responseContainer = "List", response = Strade.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response searchStradeByFilter(@ApiParam(value = "Bean di filtering delle strade") StradeFilter filter)
			throws AppException {
		try {
			filter.setFetchRule(StradeDeepDepthRule.NAME);
			List<Strade> strade = anagraficheDelegateService.searchStradeByFilter(filter);
			strade.sort(new Comparator<Strade>() {

				@Override
				public int compare(Strade o1, Strade o2) {
					return o1.getName().compareTo(o2.getName());
				}

			});
			return Response.ok(strade).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchStradeByFilter", e);
			return Response.serverError().build();
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("searchLocalitaByFilter")
	@ApiOperation(value = "searchLocalitaByFilter", notes = "Metodo che restituisce la lista delle localita filtrate")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista dei comuni filtrati", responseContainer = "List", response = Strade.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response searchLocalitaByFilter(@ApiParam(value = "Bean di filtering delle strade") LocalitaFilter filter)
			throws AppException {
		try {
			filter.setFetchRule(StradeDeepDepthRule.NAME);
			List<Localita> localita = anagraficheDelegateService.searchLocalitaByFilter(filter);

			return Response.ok(localita).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchStradeByFilter", e);
			return Response.serverError().build();
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllTipoStruttura")
	@ApiOperation(value = "getAllTipoStruttura", notes = "Metodo che restituisce la lista delle tipologie di struttura")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista delle tipologie di struttura", responseContainer = "List", response = TipoStruttura.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllTipoStruttura() throws AppException {
		try {
			List<TipoStruttura> tipoStrutture = anagraficheDelegateService.getAllTipoStruttura();
			tipoStrutture.sort(new Comparator<TipoStruttura>() {

				@Override
				public int compare(TipoStruttura o1, TipoStruttura o2) {
					return o1.getDescrizione().compareTo(o2.getDescrizione());
				}

			});
			return Response.ok(tipoStrutture).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllTipoStruttura", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllGruppi")
	@ApiOperation(value = "getAllGruppi", notes = "Metodo che restituisce la lista dei Gruppi")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Metodo che restituisce la lista dei Gruppi", responseContainer = "List", response = Gruppo.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllGruppi() throws AppException {
		try {
			List<Gruppo> listaGruppi = gruppoDelegateService.getAllGruppo();
			return Response.ok(listaGruppi).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllTipoStruttura", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllRuolo")
	@ApiOperation(value = "getAllRuolo", notes = "Metodo che restituisce la lista dei Ruoli disponibili")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Metodo che restituisce la lista dei Gruppi", responseContainer = "List", response = Ruolo.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllRuolo() throws AppException {
		try {
			List<Ruolo> listaRuoli = anagraficheDelegateService.getAllRuolo();
			return Response.ok(listaRuoli).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllRuolo", e);
			return Response.serverError().build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("searchStaticDataByType/{type}")
	@ApiOperation(value = "searchStaticDataByType", notes = "Metodo che restituisce la lista degli Static Data filtrati per tipo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Metodo che restituisce la lista dei Gruppi", responseContainer = "List", response = StaticData.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response searchStaticDataByType(@PathParam("type") String type) throws AppException {
		try {
			List<StaticData> staticData = anagraficheDelegateService.searchStaticDataByType(type);
			return Response.ok(staticData).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchStaticDataByType", e);
			return Response.serverError().build();
		}
	}

}
