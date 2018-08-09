/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.rest.service.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.area118.sdodae.service.model.v1.DaeEventAlert;
import it.eng.area118.sdodae.service.model.v1.DaeResponder;
import it.eng.area118.sdodae.service.model.v1.DaeResponse;
import it.eng.area118.sdodae.service.model.v1.DaeResponseData;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.EventDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.FirstResponderDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.CategoriaFr;
import it.eng.areas.ems.sdodaeservices.delegate.model.Event;
import it.eng.areas.ems.sdodaeservices.delegate.model.EventInfoRequest;
import it.eng.areas.ems.sdodaeservices.delegate.model.FRPositionToCO;
import it.eng.areas.ems.sdodaeservices.delegate.model.GPSLocation;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericException;
import it.eng.areas.ems.sdodaeservices.delegate.model.Intervento;
import it.esel.parsley.lang.StringUtils;

/**
 * @author Bifulco Luigi
 *
 */
@Component
@Path("/emsService")
@Api(value = "/emsService", protocols = "http", description = "Servizio di integrazione tra EMS e il DAE")
public class EmsServiceFacadeREST {

	@Autowired
	private EventDelegateService eventDelegateService;

	@Autowired
	private FirstResponderDelegateService frService;

	@Autowired
	private AnagraficheDelegateService anagService;

	private Logger logger = LoggerFactory.getLogger(EmsServiceFacadeREST.class);

	@GET
	@Path("/getPositions")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@ApiOperation(value = "getPositions", notes = "Metodo per il prelievo delle posizioni inviate volontariamente dai FR")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = DaeResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getPositions() throws GenericException {
		return getPositions(10);
	}

	@GET
	@Path("/getPositions/{maxResult}")
	@Consumes(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@ApiOperation(value = "getPositions", notes = "Metodo per il prelievo delle posizioni inviate volontariamente dai FR")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = DaeResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response getPositions(@PathParam("maxResult") Integer maxResult) throws GenericException {
		if (maxResult == null || maxResult <= 0) {
			maxResult = 10;
		}

		List<FRPositionToCO> resp = frService.getLastFRPositionToCO(maxResult);

		return Response.ok(resp).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("sendEmergencyEvent")
	@ApiOperation(value = "sendEmergencyEvent", notes = "Metodo per l'inserimento di un nuovo allertamento DAE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = DaeResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public DaeResponse insert(DaeEventAlert emsEvent) throws GenericException {
		try {
			String external_code = emsEvent.getCode_external();
			String code = external_code.split("_")[1];
			String co = external_code.split("_")[0];

			Event daeEvent = fromEmsDAEtoEventDae(emsEvent);
			daeEvent.setCoRiferimento(co);
			daeEvent.setCartellino(code);
			daeEvent.setTimestamp(Calendar.getInstance());

			Event retEVent = eventDelegateService.insertEvent(daeEvent);
			eventDelegateService.alertFirstResponderForNewEvent(retEVent);
			return getDaeInfo(external_code);
		} catch (Exception e) {
			logger.error("ERROR WHILE insert", e);
			throw e;
		}
	}

	@POST
	@Path("/updateEmergencyEvent/{external_code}")
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@ApiOperation(value = "updateEmergencyEvent", notes = "Metodo per l'aggioranamento di un allertamento DAE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = DaeResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public DaeResponse update(@PathParam("external_code") String external_code, DaeEventAlert emsEvent)
			throws GenericException {
		String[] split = external_code.split("_");

		String code = split[1];
		String co = split[0];

		String status = StringUtils.defaultIfEmpty(emsEvent.getRecord_state(), "created");
		if (status.equals("canceled")) {

			EventInfoRequest request = new EventInfoRequest();
			request.setCoRiferimento(co);
			request.setCartellino(code);
			eventDelegateService.closeOrAbortInterventionByEvent(request, true);
			return getDaeInfo(external_code);
		}

		if (status.equals("closed")) {

			EventInfoRequest request = new EventInfoRequest();
			request.setCoRiferimento(co);
			request.setCartellino(code);
			eventDelegateService.closeOrAbortInterventionByEvent(request, false);
			return getDaeInfo(external_code);
		}

		Event daeEvent = eventDelegateService.getEventByCartellinoAndCO(code, co);
		// sovrascrivo i dati che mi sono arrivati
		copyEmsDAEtoEventDae(emsEvent, daeEvent);

		// daeEvent.setCoRiferimento(co);
		// daeEvent.setCartellino(code);
		// daeEvent.setTimestamp(Calendar.getInstance());
		try {
			if (emsEvent.getEstimated_time() != null) {
				daeEvent.setTempoArrivoAmbulanza(Integer.valueOf(emsEvent.getEstimated_time()));

				Date dataArrivoAmbulanza = new Date(
						System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(daeEvent.getTempoArrivoAmbulanza()));

				daeEvent.setDataArrivoAmbulanza(dataArrivoAmbulanza);
			}
		} catch (NumberFormatException e) {
		}
		// salvo il tutto e mando le notifiche
		eventDelegateService.updateEvent(daeEvent);

		return getDaeInfo(external_code);
	}

	@GET
	@Path("/getEmergencyEvent/{external_code}")
	@Consumes(javax.ws.rs.core.MediaType.TEXT_PLAIN)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@ApiOperation(value = "getEmergencyEvent", notes = "Metodo per il prelievo delle attuali informazioni di un allertamento DAE in corso")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = DaeResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public DaeResponse getInfo(@PathParam("external_code") String external_code) throws GenericException {
		return getDaeInfo(external_code);
	}

	protected DaeResponse getDaeInfo(String external_code) throws GenericException {
		String code = external_code.split("_")[1];
		String co = external_code.split("_")[0];
		EventInfoRequest request = new EventInfoRequest();
		request.setCoRiferimento(co);
		request.setCartellino(code);
		Event daeEvent = eventDelegateService.getEventInterventionDetails(request);
		return fromDaeEventToDaeResponse(daeEvent);
	}

	private static DaeResponse fromDaeEventToDaeResponse(Event event) {
		DaeResponse response = new DaeResponse();
		response.setSuccess(true);
		response.setTime(Calendar.getInstance().getTime());
		DaeResponseData data = new DaeResponseData();

		DaeEventAlert alert = new DaeEventAlert();
		alert.setCode_external(event.getCartellino());
		alert.setCivic(event.getCivico());
		alert.setFr_category(event.getCategoria().getId());
		// alert.setFloor(event.getfl);
		alert.setLatitude(String.valueOf(event.getCoordinate().getLatitudine()));
		alert.setLongitude(String.valueOf(event.getCoordinate().getLongitudine()));
		alert.setStreet(event.getIndirizzo());
		if (event.getClosed() != null && event.getClosed()) {
			alert.setRecord_state("closed");
		} else {
			alert.setRecord_state("created");
		}

		List<DaeResponder> responders = new ArrayList<DaeResponder>();
		List<Intervento> intervs = event.getInterventi();
		if (intervs != null && intervs.size() > 0) {
			intervs.forEach((i) -> {

				DaeResponder r = createResponder(i);
				responders.add(r);
			});
		}

		alert.setFirst_responders(responders);

		data.setIntervention(alert);
		response.setData(data);
		return response;
	}

	private static DaeResponder createResponder(Intervento i) {
		DaeResponder responder = new DaeResponder();

		responder.setAccepted(i.getAccettatoDa() != null);
		responder.setUpdate_time(Calendar.getInstance().getTime());
		responder.setRequest_time(Calendar.getInstance().getTime());
		responder.setAccuracy(String.valueOf("0"));
		responder.setAltitude(String.valueOf("0"));
		// responder.setCourse(i.getEseguitoDa().getLastPosition().get);
		responder.setEmail(i.getEseguitoDa().getEmail());
		responder.setFirst_name(i.getEseguitoDa().getNome());
		responder.setFr_category(i.getEseguitoDa().getCategoriaFr().getId());
		responder.setLast_name(i.getEseguitoDa().getCognome());
		responder.setLatitude(String.valueOf(i.getEseguitoDa().getLastPosition().getLatitudine()));
		responder.setLongitude(String.valueOf(i.getEseguitoDa().getLastPosition().getLongitudine()));
		responder.setOnsite(i.getDataChiusura() != null);
		responder.setOnsite_time(i.getDataChiusura());
		responder.setPhone_number(i.getEseguitoDa().getNumCellulare());
		responder.setStart_time(i.getDataAccettazione());
		responder.setStarted(i.getDataAccettazione() != null);
		return responder;
	}

	private static DaeResponse fromEventToDaeResponse(DaeEventAlert alert) {
		DaeResponse response = new DaeResponse();
		response.setSuccess(true);
		DaeResponseData data = new DaeResponseData();
		data.setIntervention(alert);
		response.setData(data);
		return response;

	}

	private Event copyEmsDAEtoEventDae(DaeEventAlert alert, Event ev) {
		// ev.setCartellino(alert.getCode_external());
		ev.setCivico(alert.getCivic());
		if (!StringUtils.isEmpty(alert.getPlace()) && alert.getPlace().contains("-")) {
			String place[] = alert.getPlace().split("-");
			// inverto comune e località
			ev.setComune(place[1].trim() + " - " + place[0].trim());
		} else {
			ev.setComune(alert.getPlace());
		}

		ev.setIndirizzo(alert.getStreet());
		ev.setProvincia(alert.getArea());
		ev.setInfo(alert.getObservation());
		ev.setRiferimento(alert.getObservation());

		GPSLocation location = new GPSLocation();
		location.setLatitudine(Float.valueOf(alert.getLatitude()));
		location.setLongitudine(Float.valueOf(alert.getLongitude()));
		ev.setCoordinate(location);
		// aggiunto luogo del giudizio di sintesi
		ev.setLuogo(alert.getSynthesisPlace());

		String id;
		if (!StringUtils.isEmpty(alert.getFr_category())) {
			id = alert.getFr_category();
		} else {
			id = "FIRST_RESPONDER";
		}

		CategoriaFr fr = anagService.getCategoriaById(id);

		ev.setCategoria(fr);

		return ev;

	}

	private Event fromEmsDAEtoEventDae(DaeEventAlert alert) {
		Event ev = new Event();
		return copyEmsDAEtoEventDae(alert, ev);
	}
}
