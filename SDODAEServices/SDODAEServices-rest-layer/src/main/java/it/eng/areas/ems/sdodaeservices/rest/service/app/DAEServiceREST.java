package it.eng.areas.ems.sdodaeservices.rest.service.app;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeMinimalDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.DaeDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.Dae;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeFault;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeLight;
import it.eng.areas.ems.sdodaeservices.delegate.model.Disponibile;
import it.eng.areas.ems.sdodaeservices.delegate.model.DisponibilitaGiornaliera;
import it.eng.areas.ems.sdodaeservices.delegate.model.Image;
import it.eng.areas.ems.sdodaeservices.delegate.model.NewDAEResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.DaeFilter;
import it.eng.areas.ems.sdodaeservices.delegate.utils.DaeUtils;
import it.eng.areas.ems.sdodaeservices.entity.DaeStatoEnum;
import it.eng.areas.ems.sdodaeservices.entity.TipoDisponibilitaEnum;
import it.eng.areas.ems.sdodaeservices.exception.DaeDuplicateException;
import it.eng.areas.ems.sdodaeservices.rest.exception.AppException;
import it.eng.areas.ems.sdodaeservices.rest.model.Feature;
import it.eng.areas.ems.sdodaeservices.rest.model.GeoJSON;
import it.eng.areas.ems.sdodaeservices.rest.model.Geometry;
import it.eng.areas.ems.sdodaeservices.rest.model.ReverseGeocodingResult;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;
import it.eng.areas.ems.sdodaeservices.rest.service.GeocodeService;
import it.eng.areas.ems.sdodaeservices.rest.utils.FilterUtils;
import it.eng.areas.ems.sdodaeservices.rest.utils.ImageUtils;
import it.eng.areas.ems.sdodaeservices.rest.utils.ImageUtils.Size;

@Component
@Path("/daeService")
@Api(value = "/daeService", protocols = "http", description = "Servizio dedicato alla gestione dei DAE")
public class DAEServiceREST {

	private Logger logger = LoggerFactory.getLogger(DAEServiceREST.class);

	@Autowired
	protected DaeDelegateService daeDelegateService;

	@Context
	private SecurityContext secContext;

	@Autowired
	private FirstResponderDelegateService frDelegateService;

	@Autowired
	private FilterUtils filterUtils;

	@Autowired
	private GeocodeService geocodeService;

	private ExecutorService imageExecutor = Executors.newFixedThreadPool(10);

	private DateFormat hourFormat = new SimpleDateFormat("HH:mm");

	protected List<Dae> processDaeList(HttpServletRequest httpServletRequest, List<Dae> daes) {
		List<Dae> retDae = new ArrayList<>();
		if (daes != null) {
			for (Dae dae : daes) {
				retDae.add(processDaeImage(httpServletRequest, dae));
				setDisponibilita(dae);
				// tolgo l'istat per non farlo visualizzare sul dettaglio dae
				if (dae.getGpsLocation() != null && dae.getGpsLocation().getComune() != null) {
					dae.getGpsLocation().getComune().setCodiceIstat(null);
				}
			}
		}
		return retDae;
	}

	protected void setDisponibilita(Dae dae) {
		// carico la deisponibilità del DAE
		dae.setDisponibile(false);

		TipoDisponibilitaEnum tipoDisp = TipoDisponibilitaEnum.byDescription(dae.getTipologiaDisponibilita());

		switch (tipoDisp) {
		case DISPONIBILITAH24:
			dae.setDisponibile(true);
			break;
		case NON_DEFINITA:
			dae.setDisponibilitaIndefinita(true);
			break;
		case DA_PROGRAMMA:
			if (dae.getDisponibilita() != null && !dae.getDisponibilita().isEmpty()) {
				/// data attuale
				Calendar c = Calendar.getInstance();
				// i mesi nella classe Calendar partono da 0
				int actualMonth = c.get(Calendar.MONTH) + 1;
				int actualDAY = c.get(Calendar.DAY_OF_WEEK);
				String actualHour = hourFormat.format(c.getTime());

				for (Disponibile d : dae.getDisponibilita()) {
					if (d.getDisponibilitaSpecifica() != null && d.getDa() != null && d.getA() != null) {

						int daM = d.getDa().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();
						int aM = d.getA().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue();

						boolean in = false;
						if (daM < aM) {
							if (daM <= actualMonth && actualMonth <= aM) {
								in = true;
							}
						} else {
							// nel caso in cui il mese di inizio è maggiore del
							// mese di fine
							if (daM <= actualMonth || actualMonth <= aM) {
								in = true;
							}
						}
						if (in) {
							for (DisponibilitaGiornaliera dg : d.getDisponibilitaSpecifica()
									.getDisponibilitaGiornaliera()) {
								if (dg.getGiornoSettimana().getOrder() == actualDAY) {
									// se il giorno è quello odierno controllo
									// l'orario
									if (dg.getOrarioDa().compareTo(actualHour) <= 0
											&& dg.getOrarioA().compareTo(actualHour) >= 0) {
										dae.setDisponibile(true);
									}
								}
							}
						}
					}
				}
			} else {
				dae.setDisponibile(true);
			}
			break;
		default:
			break;
		}
	}

	protected Dae processDaeImage(HttpServletRequest httpServletRequest, Dae dae) {

		Image img = new Image();
		img.setData("");
		String path = httpServletRequest.getContextPath() + "/rest/daeService/getDaeImg/" + dae.getId();
		img.setUrl(path);
		dae.setImmagine(img);
		return dae;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllDAEgeoJSON")
	@ApiOperation(value = "getAllDAEgeoJSON", notes = "Metodo che restituisce la lista completa dei DAE dislocati sul territorio in formato geoJSON")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista di tutti i DAE", response = GeoJSON.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllDAEgeoJSON(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse) throws AppException {
		try {

			DaeFilter daeFilter = new DaeFilter();

			daeFilter.setOperativo(true);
			daeFilter.setStatoValidazione(DaeStatoEnum.VALIDATO);
			daeFilter.setStatoVisible(true);
			daeFilter.setFetchRule(DaeDeepDepthRule.NAME);

			List<Dae> daes = daeDelegateService.searchDaeByFilter(daeFilter);

			GeoJSON geo = new GeoJSON();
			geo.setType("FeatureCollection");

			daes.forEach(d -> {

				Feature feature = new Feature();
				feature.setType("Feature");

				feature.setGeometry(new Geometry("Point",
						new double[] { d.getGpsLocation().getLongitudine(), d.getGpsLocation().getLatitudine() }));

				feature.getProperties().put("nome", d.getNomeSede());
				feature.getProperties().put("indirizzo",
						d.getGpsLocation().getIndirizzo().getName() + " " + d.getGpsLocation().getCivico());
				// feature.getProperties().put("cap", value);
				feature.getProperties().put("citta", d.getGpsLocation().getComune().getNomeComune());

				if (d.getResponsabile() != null && d.getResponsabile().getTelefono() != null) {
					feature.getProperties().put("tel", d.getResponsabile().getTelefono());
				}

				feature.getProperties().put("Ubicazione", d.getUbicazione());

				if (d.getDisponibilita() != null && d.getDisponibilita().size() > 0) {
					feature.getProperties().put("Orari", DaeUtils.getOrariMap(d));
				}
				feature.getProperties().put("Note", d.getNotediAccessoallaSede() + ", " + d.getNoteGenerali());

				geo.getFeatures().add(feature);

			});

			return Response.ok(geo).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllDAE", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING getAllDAE", e.getMessage(), "");

		}
	}

	@POST
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllDAE")
	@ApiOperation(value = "getAllDAE", notes = "Metodo che restituisce la lista completa dei DAE dislocati sul territorio")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista di tutti i DAE", responseContainer = "List", response = Dae.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllDAE(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse) throws AppException {
		try {
			DaeFilter daeFilter = new DaeFilter();
			daeFilter.setFetchRule(DaeDeepDepthRule.NAME);
			filterUtils.addProvinceToFilter(secContext, daeFilter);
			List<Dae> daes = daeDelegateService.searchDaeByFilter(daeFilter);

			daes = processDaeList(httpServletRequest, daes);

			filterUtils.setReadOnly(secContext, daes);

			return Response.ok(daes).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllDAE", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING getAllDAE", e.getMessage(), "");

		}
	}

	@POST
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getAllDAEForMap")
	@ApiOperation(value = "getAllDAEForMap", notes = "Metodo che restituisce la lista completa dei DAE dislocati sul territorio priva di informazioni aggiuntive")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ottiene la lista di tutti i DAE", responseContainer = "List", response = DaeLight.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getAllDAEForMap(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse) throws AppException {
		try {
			DaeFilter daeFilter = new DaeFilter();
			daeFilter.setFetchRule(DaeMinimalDepthRule.NAME);
			daeFilter.setStatoValidazione(DaeStatoEnum.VALIDATO);
			filterUtils.addProvinceToFilter(secContext, daeFilter);
			List<Dae> daes = daeDelegateService.searchDaeByFilter(daeFilter);

			daes = processDaeList(httpServletRequest, daes);

			filterUtils.setReadOnly(secContext, daes);

			return Response.ok(daes).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllDAEForMap", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING getAllDAEForMap", e.getMessage(), "");

		}
	}

	@GET
	@Path("/getDaeImg/{daeID}")
	@Produces("image/png")
	public Response getFullImage(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse, @PathParam("daeID") String daeId,
			@DefaultValue("MEDIUM") @QueryParam("size") Size size) {
		try {

			Future<ByteArrayOutputStream> future = imageExecutor.submit(() -> {

				Image localDAEImg = daeDelegateService.getImageByID(daeId);
				BufferedImage retImg = null;
				if (localDAEImg != null && !StringUtils.isEmpty(localDAEImg.getData())) {
					retImg = ImageUtils.processImage(httpServletRequest, localDAEImg, daeId, size);
				} else {
					Image def = daeDelegateService.getImageByID("DEFAULT_DAE");
					retImg = ImageUtils.processImage(httpServletRequest, def, daeId, size);
				}
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(retImg, "png", baos);

				return baos;
			});

			byte[] imageData = future.get(5, TimeUnit.SECONDS).toByteArray();

			return Response.ok(imageData).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getFullImage", e);
			return Response.serverError().build();
		}

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("addNewDae")
	@Secured
	@ApiOperation(value = "addNewDae", notes = "Metodo che consente di censire un nuovo Dae")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "DAE censito correttmanete", response = NewDAEResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response addNewDae(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse, @ApiParam(value = "Dae che si vuole censire") Dae dae)
			throws AppException {
		try {
			logger.info("EXECUTING addNewDae");
			Utente u = (Utente) secContext.getUserPrincipal();

			if (dae.getId() == null) {
				dae.setCreatoDA(u);
			}
			// se non è presente la località la calcolo col reverse geocoding
			if (dae.getGpsLocation().getLocalita() == null) {
				ReverseGeocodingResult result = geocodeService.reverseGeocoding(dae.getGpsLocation());
				if (result.getLocalita() != null) {
					dae.getGpsLocation().setLocalita(result.getLocalita());
				}
			}

			Dae ret = daeDelegateService.insertDae(dae, u, true);
			NewDAEResponse ndr = new NewDAEResponse();
			ndr.setEsito(true);
			ndr.setTimeStamp(Calendar.getInstance());
			return Response.ok(ndr).build();
		} catch (DaeDuplicateException e) {
			logger.error("DaeDuplicateException ");
			NewDAEResponse ndr = new NewDAEResponse();
			ndr.setEsito(false);
			ndr.setTimeStamp(Calendar.getInstance());
			ndr.setMessaggio(
					"Errore durante l’inserimento del DAE. Controllare i campi obbligatori, la connessione dati, oppure se il DAE è già presente");
			return Response.ok(ndr).build();

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING addNewDae", e);
			NewDAEResponse ndr = new NewDAEResponse();
			ndr.setEsito(false);
			ndr.setTimeStamp(Calendar.getInstance());
			ndr.setMessaggio(
					"Errore durante l’inserimento del DAE. Controllare i campi obbligatori, la connessione dati, oppure se il DAE è già presente");
			return Response.ok(ndr).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("getDaeById")
	@Secured
	@ApiOperation(value = "getDaeById", notes = "Metodo che consente di ottenere le info di uno specifico DAE ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "DAE censito correttmanete", response = Dae.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getDaeById(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse,
			@ApiParam(value = "Filtro di ricerca - ") DaeFilter daeFilter) throws AppException {
		try {
			if (daeFilter.getId() != null) {
				Dae ret = daeDelegateService.getDaeById(daeFilter.getId());
				List<Dae> daes = new ArrayList<>();
				daes.add(ret);
				daes = processDaeList(httpServletRequest, daes);
				return Response.ok(daes.get(0)).build();
			}
			return Response.serverError().build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getDaeById", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("searchDAEByFilter")
	@Secured
	@ApiOperation(value = "searchDAEByFilter", notes = "Metodo che consente di ricerca i Dae mediante un filtro")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ricerca eseguita correttamente", responseContainer = "List", response = Dae.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione della ricerca") })
	public Response searchDAEByFilter(@ApiParam(value = "Filtro di ricerca") DaeFilter filter,
			@Context HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse)
			throws AppException {
		try {
			filterUtils.addProvinceToFilter(secContext, filter);

			List<Dae> daes = daeDelegateService.searchDaeByFilter(filter);
			daes = processDaeList(httpServletRequest, daes);

			filterUtils.setReadOnly(secContext, daes);
			return Response.ok(daes).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchDAEByFilter", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("reportFault")
	@Secured
	@ApiOperation(value = "reportFault", notes = "Metodo che consente di segnalare e aggiornare un guasto al DAE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ricerca eseguita correttamente", response = DaeFault.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione della ricerca") })
	public Response reportFault(@ApiParam(value = "Filtro di ricerca") DaeFault fault) throws AppException {
		try {

			Utente u = (Utente) secContext.getUserPrincipal();
			// aggiungo l'utente loggato
			fault.setUtente(u);

			fault = daeDelegateService.saveDaeFault(fault);

			return Response.ok(fault).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING searchDAEByFilter", e);
			return Response.serverError().build();
		}
	}

}
