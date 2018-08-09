/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.rest.service.portal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.GraphServiceDelegate;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeEventActivationDTO;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeNumbersDTO;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeSubscriptionDTO;
import it.eng.areas.ems.sdodaeservices.delegate.model.FirstResponderSubscriptionDTO;
import it.eng.areas.ems.sdodaeservices.rest.model.ChartResult;
import it.eng.areas.ems.sdodaeservices.rest.model.Interval;
import it.eng.areas.ems.sdodaeservices.rest.model.NumberDaeFrResult;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;

/**
 * @author Bifulco Luigi
 *
 */

@Component
@Path("/portal/charts")
@Api(value = "/charts", protocols = "http", description = "Servizio dedicato alla gestione dei dati da inviare ai grafici")
public class GraphServiceREST {

	@Autowired
	private GraphServiceDelegate graphService;

	@Autowired
	protected AnagraficheDelegateService anagraficheDelegateService;

	@Value("${dae.provinces}")
	protected String[] area;

	@GET
	@Path("/frSubscriptions")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "frSubscriptions", notes = "Restituisce i dati dell'andamento registrazioni dei fr")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public ChartResult listFrSubscriptions() {

		Interval interval = new Interval();

		Calendar from = GregorianCalendar.getInstance();
		from.add(GregorianCalendar.DAY_OF_MONTH, -30);
		from.set(GregorianCalendar.HOUR_OF_DAY, 0);
		from.set(GregorianCalendar.MINUTE, 0);
		from.set(GregorianCalendar.SECOND, 0);

		interval.setFromCal(from);
		interval.setToCal(GregorianCalendar.getInstance());
		return listFrSubscriptions(interval);
	}

	@POST
	@Path("/frSubscriptions")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "frSubscriptions", notes = "Restituisce i dati dell'andamento registrazioni dei fr")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public ChartResult listFrSubscriptions(Interval interval) {
		ChartResult result = new ChartResult();
		List<String> data = new ArrayList<String>();
		List<String> labels = new ArrayList<String>();

		TreeMap<String, String> map = getMap(interval);

		List<FirstResponderSubscriptionDTO> dtos = graphService.listFrSubscriptions(interval.getFromCal(),
				interval.getToCal());

		// dtos.sort((s1, s2) -> s1.getGgmmaa().compareTo(s2.getGgmmaa()));
		// aggiungo i risultati alla mappa
		dtos.forEach((fr) -> {
			map.put(fr.getAaaammgg(), String.valueOf(fr.getNumIscritti()));
		});
		// ciclo la mappa per ottenere i dati e i risultati
		map.forEach((aaaammgg, numVal) -> {
			data.add(numVal);
			// labels.add(s.getGgmmaa().split("-")[0]);
			List<String> splitted = Splitter.on('-').trimResults().omitEmptyStrings().splitToList(aaaammgg);
			String label = splitted.get(2) + "/" + splitted.get(1);
			labels.add(label);
		});

		result.setData(data);
		result.setLabels(labels);
		return result;
	}

	private TreeMap<String, String> getMap(Interval interval) {

		TreeMap<String, String> map = new TreeMap<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// Monday, February 29 is a leap day in 2016 (otherwise, February only
		// has 28 days)
		Calendar from = interval.getFromCal();
		Calendar to = interval.getToCal();
		LocalDate start = LocalDate.of(from.get(Calendar.YEAR), from.get(Calendar.MONTH) + 1,
				from.get(Calendar.DAY_OF_MONTH));
		LocalDate end = LocalDate.of(to.get(Calendar.YEAR), to.get(Calendar.MONTH) + 1, to.get(Calendar.DAY_OF_MONTH));

		// 4 days between (end is inclusive in this example)
		Stream.iterate(start, date -> date.plusDays(1)).limit(ChronoUnit.DAYS.between(start, end) + 1).forEach(t -> {
			String s = t.format(formatter);
			map.put(s, "0");
		});

		return map;
	}

	@GET
	@Path("/daeSubscriptions")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "daeSubscriptions", notes = "Restituisce i dati dell'andamento registrazioni dei dae")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public ChartResult listDAESubscriptions() {

		Interval interval = new Interval();

		Calendar from = GregorianCalendar.getInstance();
		from.add(GregorianCalendar.DAY_OF_MONTH, -30);
		from.set(GregorianCalendar.HOUR_OF_DAY, 0);
		from.set(GregorianCalendar.MINUTE, 0);
		from.set(GregorianCalendar.SECOND, 0);

		interval.setFromCal(from);
		interval.setToCal(GregorianCalendar.getInstance());
		return listDAESubscriptions(interval);
	}

	@POST
	@Path("/daeSubscriptions")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "daeSubscriptions", notes = "Restituisce i dati dell'andamento registrazioni dei dae")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public ChartResult listDAESubscriptions(Interval interval) {
		ChartResult result = new ChartResult();
		List<String> data = new ArrayList<String>();
		List<String> labels = new ArrayList<String>();
		List<DaeSubscriptionDTO> dtos = graphService.listDAESubscriptions(interval.getFromCal(), interval.getToCal());

		TreeMap<String, String> map = getMap(interval);

		// dtos.sort((s1, s2) -> s1.getGgmmaa().compareTo(s2.getGgmmaa()));

		// aggiungo i risultati alla mappa
		dtos.forEach((fr) -> {
			map.put(fr.getAaaammgg(), String.valueOf(fr.getNumDAE()));
		});

		// ciclo la mappa per ottenere i dati e i risultati
		map.forEach((aaaammgg, numVal) -> {
			data.add(numVal);
			// labels.add(s.getGgmmaa().split("-")[0]);
			List<String> splitted = Splitter.on('-').trimResults().omitEmptyStrings().splitToList(aaaammgg);
			String label = splitted.get(2) + "/" + splitted.get(1);
			labels.add(label);
		});

		result.setData(data);
		result.setLabels(labels);
		return result;
	}

	@GET
	@Path("/daeActivationsByProvince")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "daeActivationsByProvince", notes = "Restituisce i dati dell'andamento attivazioni dei dae per provincia")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public ChartResult listDAEActivationsByProvince() {
		Interval interval = new Interval();

		Calendar from = GregorianCalendar.getInstance();
		from.add(GregorianCalendar.DAY_OF_MONTH, -30);
		from.set(GregorianCalendar.HOUR_OF_DAY, 0);
		from.set(GregorianCalendar.MINUTE, 0);
		from.set(GregorianCalendar.SECOND, 0);

		interval.setFromCal(from);
		interval.setToCal(GregorianCalendar.getInstance());
		return listDAEActivationsByProvince(interval);
	}

	@POST
	@Path("/daeActivationsByProvince")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "daeActivationsByProvince", notes = "Restituisce i dati dell'andamento attivazioni dei dae per provincia")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public ChartResult listDAEActivationsByProvince(Interval interval) {
		ChartResult result = new ChartResult();

		List<String> labels = new ArrayList<String>();
		List<DaeEventActivationDTO> dtosFR = graphService.listDAEActivationsByProvince("FIRST_RESPONDER",
				interval.getFromCal(), interval.getToCal());
		List<DaeEventActivationDTO> dtosLB = graphService.listDAEActivationsByProvince("2", interval.getFromCal(),
				interval.getToCal());

		List<List<DaeEventActivationDTO>> array1 = Arrays.asList(//
				dtosFR, //
				dtosLB //
		);
		List<List<String>> datas = new ArrayList<>(array1.size());

		for (List<DaeEventActivationDTO> el : array1) {
			List<String> data = new ArrayList<>();
			TreeMap<String, String> map = new TreeMap<>();

			anagraficheDelegateService.getProvinceByArea(area).forEach(p -> {
				map.put(StringUtils.capitalize(p.getNomeProvincia().toLowerCase()), "0");
			});

			// aggiungo i risultati alla mappa
			el.forEach((fr) -> {
				map.put(StringUtils.capitalize(fr.getProvincia().toLowerCase()),
						String.valueOf(fr.getNumAttivazioni()));
			});

			// ciclo la mappa per ottenere i dati e i risultati
			map.forEach((province, numVal) -> {
				data.add(numVal);
				if (!labels.contains(province)) {
					labels.add(province);
				}
			});
			datas.add(data);
		}

		Map<String, List<String>> dataSet = new HashMap<>();
		dataSet.put("daeFrEvents", datas.get(0));
		dataSet.put("daeLbEvents", datas.get(1));
		result.setDataSet(dataSet);
		result.setLabels(labels);
		return result;
	}

	@GET
	@Path("/daeActivationsByDay")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "daeActivationsByDay", notes = "Restituisce i dati dell'andamento attivazioni dei dae giorno per giorno")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public ChartResult listDAEActivationsByDay() {
		Interval interval = new Interval();

		Calendar from = GregorianCalendar.getInstance();
		from.add(GregorianCalendar.DAY_OF_MONTH, -30);
		from.set(GregorianCalendar.HOUR_OF_DAY, 0);
		from.set(GregorianCalendar.MINUTE, 0);
		from.set(GregorianCalendar.SECOND, 0);

		interval.setFromCal(from);
		interval.setToCal(GregorianCalendar.getInstance());
		return listDAEActivationsByDay(interval);
	}

	@POST
	@Path("/daeActivationsByDay")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "daeActivationsByDay", notes = "Restituisce i dati dell'andamento attivazioni dei dae giorno per giorno")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public ChartResult listDAEActivationsByDay(Interval interval) {
		ChartResult result = new ChartResult();

		List<String> labels = new ArrayList<String>();
		List<DaeEventActivationDTO> dtos = graphService.listDAEActivationsByDay(interval.getFromCal(),
				interval.getToCal(), "FIRST_RESPONDER");
		List<DaeEventActivationDTO> dtosF = graphService.listDAEActivationsByDay(interval.getFromCal(),
				interval.getToCal(), "2");
		// List<DaeEventActivationDTO> dtosAccepted =
		// graphService.listDAEAccepetedActivationsByDay(interval.getFromCal(),
		// interval.getToCal(), "FIRST_RESPONDER");
		// List<DaeEventActivationDTO> dtosFAccepted =
		// graphService.listDAEAccepetedActivationsByDay(interval.getFromCal(),
		// interval.getToCal(), "2");

		// preparo le liste per generare le label da inviare al client
		List<List<DaeEventActivationDTO>> array1 = Arrays.asList(//
				dtos, //
				dtosF //
		// ,dtosAccepted, dtosFAccepted
		);

		List<List<String>> datas = new ArrayList<>(array1.size());

		for (int i = 0; i < array1.size(); i++) {
			TreeMap<String, String> map = getMap(interval);
			List<String> data = new ArrayList<String>();

			array1.get(i).forEach((fr) -> {
				map.put(fr.getAaaammgg(), String.valueOf(fr.getNumAttivazioni()));
			});

			for (Entry<String, String> entry : map.entrySet()) {
				data.add(entry.getValue());
				// labels.add(s.getGgmmaa().split("-")[0]);
				if (i == 0) {
					List<String> splitted = Splitter.on('-').trimResults().omitEmptyStrings()
							.splitToList(entry.getKey());
					String label = splitted.get(2) + "/" + splitted.get(1);
					labels.add(label);
				}
			}

			datas.add(data);
		}

		Map<String, List<String>> dataSet = new HashMap<>();
		dataSet.put("daeEvents", datas.get(0));
		dataSet.put("daeFrEvents", datas.get(1));
		// dataSet.put("acceptedDaeEvents", datas.get(2));
		// dataSet.put("acceptedDaeFrEvents", datas.get(3));
		result.setDataSet(dataSet);
		result.setLabels(labels);
		return result;
	}

	@GET
	@Path("/frActivationsByType")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "daeActivationsByDay", notes = "Restituisce i dati dell'andamento attivazioni dei dae giorno per giorno")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public ChartResult listFRActivationsByType() {
		Interval interval = new Interval();

		Calendar from = GregorianCalendar.getInstance();
		from.add(GregorianCalendar.DAY_OF_MONTH, -30);
		from.set(GregorianCalendar.HOUR_OF_DAY, 0);
		from.set(GregorianCalendar.MINUTE, 0);
		from.set(GregorianCalendar.SECOND, 0);

		interval.setFromCal(from);
		interval.setToCal(GregorianCalendar.getInstance());
		return listFRActivationsByType(interval);
	}

	@POST
	@Path("/frActivationsByType")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "daeActivationsByDay", notes = "Restituisce i dati dell'andamento attivazioni dei dae giorno per giorno")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public ChartResult listFRActivationsByType(Interval interval) {
		ChartResult result = new ChartResult();

		// Carico i dati dal DB

		List<DaeEventActivationDTO> frAccepted = graphService.listFRAcceptedByType("FIRST_RESPONDER",
				interval.getFromCal(), interval.getToCal());
		List<DaeEventActivationDTO> lbAccepted = graphService.listFRAcceptedByType("2", interval.getFromCal(),
				interval.getToCal());
		List<DaeEventActivationDTO> suAccepted = graphService.listFRAcceptedByType("SUPER_USER", interval.getFromCal(),
				interval.getToCal());

		// preparo le liste per generare le label da inviare al client
		List<String> labels = new ArrayList<String>();
		List<List<DaeEventActivationDTO>> array1 = Arrays.asList(//
				frAccepted, //
				lbAccepted, //
				suAccepted);

		List<List<String>> datas = new ArrayList<>();

		for (int i = 0; i < array1.size(); i++) {
			TreeMap<String, String> map = getMap(interval);
			List<String> data = new ArrayList<String>();

			array1.get(i).forEach((fr) -> {
				map.put(fr.getAaaammgg(), String.valueOf(fr.getNumAttivazioni()));
			});

			for (Entry<String, String> entry : map.entrySet()) {
				data.add(entry.getValue());
				// labels.add(s.getGgmmaa().split("-")[0]);
				if (i == 0) {
					List<String> splitted = Splitter.on('-').trimResults().omitEmptyStrings()
							.splitToList(entry.getKey());
					String label = splitted.get(2) + "/" + splitted.get(1);
					labels.add(label);
				}
			}

			datas.add(data);
		}

		Map<String, List<String>> dataSet = new HashMap<>();
		dataSet.put("frAccepted", datas.get(0));
		dataSet.put("lbAccepted", datas.get(1));
		dataSet.put("suAccepted", datas.get(2));
		result.setDataSet(dataSet);
		result.setLabels(labels);
		return result;
	}

	@GET
	@Path("/daeValidation")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "daeValidation", notes = "Restituisce i dati dell'andamento registrazioni dei dae")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public ChartResult listDAEValidation() {

		Interval interval = new Interval();

		Calendar from = GregorianCalendar.getInstance();
		from.add(GregorianCalendar.DAY_OF_MONTH, -30);
		from.set(GregorianCalendar.HOUR_OF_DAY, 0);
		from.set(GregorianCalendar.MINUTE, 0);
		from.set(GregorianCalendar.SECOND, 0);

		interval.setFromCal(from);
		interval.setToCal(GregorianCalendar.getInstance());
		return listDAEValidation(interval);
	}

	@POST
	@Path("/daeValidation")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "daeValidation", notes = "Restituisce i dati dell'andamento registrazioni dei dae")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public ChartResult listDAEValidation(Interval interval) {
		ChartResult result = new ChartResult();
		List<String> data = new ArrayList<String>();
		List<String> labels = new ArrayList<String>();
		List<DaeSubscriptionDTO> dtos = graphService.listDAEValidation(interval.getFromCal(), interval.getToCal());

		TreeMap<String, String> map = getMap(interval);

		// dtos.sort((s1, s2) -> s1.getGgmmaa().compareTo(s2.getGgmmaa()));

		// aggiungo i risultati alla mappa
		dtos.forEach((fr) -> {
			map.put(fr.getAaaammgg(), String.valueOf(fr.getNumDAE()));
		});

		// ciclo la mappa per ottenere i dati e i risultati
		map.forEach((aaaammgg, numVal) -> {
			data.add(numVal);
			// labels.add(s.getGgmmaa().split("-")[0]);
			List<String> splitted = Splitter.on('-').trimResults().omitEmptyStrings().splitToList(aaaammgg);
			String label = splitted.get(2) + "/" + splitted.get(1);
			labels.add(label);
		});

		result.setData(data);
		result.setLabels(labels);
		return result;
	}

	@GET
	@Path("/fetchDaeNumbers")
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "fetchDaeNumbers", notes = "Restituisce il numero di dae e fr")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public NumberDaeFrResult fetchDaeNumbers() {

		Interval interval = new Interval();

		Calendar from = GregorianCalendar.getInstance();
		from.setTimeInMillis(0);
		interval.setFromCal(from);
		interval.setToCal(GregorianCalendar.getInstance());
		return fetchDaeNumbers(interval);
	}

	@POST
	@Path("/fetchDaeNumbers")
	@Secured
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "fetchDaeNumbers", notes = "Restituisce il numero di dae e fr")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = ChartResult.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public NumberDaeFrResult fetchDaeNumbers(Interval interval) {
		NumberDaeFrResult result = new NumberDaeFrResult();

		Map<String, Integer> dae = new TreeMap<>();

		Map<String, Integer> fr = new TreeMap<>();

		AtomicInteger totalDAE = new AtomicInteger(0);
		AtomicInteger totalFR = new AtomicInteger(0);

		List<DaeNumbersDTO> dtos = graphService.fetchDaeNumbersByStatus(interval.getFromCal(), interval.getToCal());

		dtos.forEach(d -> {
			dae.put("DAE " + d.getStatus(), d.getNumDAE());
			totalDAE.accumulateAndGet(d.getNumDAE(), (x, y) -> x + y);
		});

		dtos = graphService.fetchFrNumbersByCategory(interval.getFromCal(), interval.getToCal());

		dtos.forEach(d -> {
			fr.put(d.getStatus(), d.getNumDAE());
			totalFR.accumulateAndGet(d.getNumDAE(), (x, y) -> x + y);
		});

		result.setDae(dae);
		result.setFr(fr);
		result.setTotalDAE(totalDAE.get());
		result.setTotalFR(totalFR.get());

		return result;
	}

}
