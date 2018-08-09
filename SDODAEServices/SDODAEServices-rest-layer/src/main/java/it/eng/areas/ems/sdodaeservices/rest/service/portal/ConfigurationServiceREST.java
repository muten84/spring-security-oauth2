package it.eng.areas.ems.sdodaeservices.rest.service.portal;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.Config;
import it.eng.areas.ems.sdodaeservices.delegate.model.DashboardPosition;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.rest.exception.AppException;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;

@Component
@Path("/portal/configurationService")
@Api(value = "/portal/configurationService", protocols = "http", description = "Servizio dedicato alla modifica delle configurazioni")
public class ConfigurationServiceREST {

	private Logger logger = LoggerFactory.getLogger(ConfigurationServiceREST.class);

	@Autowired
	protected AnagraficheDelegateService anagraficheDelegateService;

	@Context
	private SecurityContext secContext;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllConfiguration")
	@Secured
	@ApiOperation(value = "getAllConfiguration", notes = "Metodo che consente di caricare tutte le configurazioni dal server")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", responseContainer = "List", response = Config.class),
			@ApiResponse(code = 500, message = "Errore durante il caricamento") })
	public Response getAllConfiguration() throws AppException {
		try {
			List<Config> toRet = anagraficheDelegateService.getAllConfiguration();
			// creo la lista di paramteri
			List<ParametersEnum> enums = Arrays.asList(ParametersEnum.values());

			// controllo se ci sono parametri nell'enum non presenti sul db
			enums.forEach(en -> {
				boolean toAdd = true;
				for (Config c : toRet) {
					if (c.getParametro().equals(en.name())) {
						toAdd = false;
					}
				}
				if (toAdd) {
					Config conf = new Config();
					conf.setId(en.name());
					conf.setDescrizione(en.getDescription());
					conf.setParametro(en.name());
					toRet.add(conf);
				}
			});
			return Response.ok(toRet).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING getAllConfiguration", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING getAllConfiguration", e.getMessage(), "");
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("saveConfiguration")
	@Secured
	@ApiOperation(value = "saveConfiguration", notes = "Metodo che consente di salvare una configurazione")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione eseguita correttamente", response = Config.class),
			@ApiResponse(code = 500, message = "Errore durante il salvataggio") })
	public Response saveConfiguration(@ApiParam(value = "configurazione da salvare") Config config)
			throws AppException {
		try {
			Config toRet = anagraficheDelegateService.saveConfiguration(config);
			return Response.ok(toRet).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING saveConfiguration", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING saveConfiguration", e.getMessage(), "");
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("saveDashboardPosition")
	@Secured
	public Response saveDashboardPosition(DashboardPosition[] positions) throws AppException {
		try {

			Utente utente = (Utente) secContext.getUserPrincipal();

			DashboardPosition[] toRet = anagraficheDelegateService.saveDashboardPosition(utente, positions);
			return Response.ok(toRet).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING saveConfiguration", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING saveConfiguration", e.getMessage(), "");
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getDashboardPosition")
	@Secured
	public Response getDashboardPosition() throws AppException {
		try {
			Utente utente = (Utente) secContext.getUserPrincipal();

			DashboardPosition[] toRet = anagraficheDelegateService.getDashboardPosition(utente.getId());
			return Response.ok(toRet).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING saveConfiguration", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING saveConfiguration", e.getMessage(), "");
		}
	}

}
