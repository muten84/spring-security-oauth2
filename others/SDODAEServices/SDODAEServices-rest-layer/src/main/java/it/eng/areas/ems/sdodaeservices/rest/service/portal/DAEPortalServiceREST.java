package it.eng.areas.ems.sdodaeservices.rest.service.portal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import it.eng.areas.ems.common.sdo.dto.CompoundDTORule;
import it.eng.areas.ems.common.sdo.dto.DTOService;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeMinimalDepthRule;
import it.eng.areas.ems.sdodaeservices.delegate.DaeDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.Dae;
import it.eng.areas.ems.sdodaeservices.delegate.model.DaeImageUpload;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Image;
import it.eng.areas.ems.sdodaeservices.delegate.model.ProgrammaManutenzioneHistory;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.entity.DaeDO;
import it.eng.areas.ems.sdodaeservices.exception.DaeDuplicateException;
import it.eng.areas.ems.sdodaeservices.rest.exception.AppException;
import it.eng.areas.ems.sdodaeservices.rest.model.DaeDuplicateResponse;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;
import it.eng.areas.ems.sdodaeservices.rest.service.app.DAEServiceREST;
import it.eng.areas.ems.sdodaeservices.rest.utils.FilterUtils;
import it.eng.areas.ems.sdodaeservices.rest.utils.ImageUtils;

@Component
@Path("/portal/daeService")
@Api(value = "/portal/daeService", protocols = "http", description = "Servizio dedicato alla gestione dei DAE")
public class DAEPortalServiceREST extends DAEServiceREST {

	private Logger logger = LoggerFactory.getLogger(DAEPortalServiceREST.class);

	@Autowired
	protected DaeDelegateService daeDelegateService;

	@Context
	private SecurityContext secContext;

	@Autowired
	private FilterUtils filterUtils;

	@Autowired
	private DTOService dtoService;

	protected Dae processDaeImage(HttpServletRequest httpServletRequest, Dae dae) {

		Image img = new Image();
		img.setData("");
		String path = httpServletRequest.getContextPath() + "/rest/portal/daeService/getDaeImg/" + dae.getId();
		img.setUrl(path);
		dae.setImmagine(img);
		return dae;
	}

	@GET
	@Path("/synchGisDATA")
	public Response synchGisDATA(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse) {
		Boolean ret = daeDelegateService.synchGisDATA();
		return Response.ok(ret).build();
	}

	@POST
	@Secured
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("uploadDAEImage")
	@ApiOperation(value = "uploadDAEImage", notes = "Metodo che consente di caricare un'immagine per un DAE")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Esecuzione corretta del metodo", response = Dae.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response uploadDAEImage(
			@ApiParam(value = "Bean che consente l'upload dell'immagine") DaeImageUpload uploadImage)
			throws AppException {
		try {
			Utente utente = (Utente) secContext.getUserPrincipal();

			Dae daes = daeDelegateService.uploadDaeImage(uploadImage, utente);
			return Response.ok(daes).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING uploadDAEImage", e);
			throw new AppException(1, 1, "ERROR WHILE EXECUTING uploadDAEImage", e.getMessage(), "");
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("saveDae")
	@Secured
	@ApiOperation(value = "saveDae", notes = "Metodo che consente di salvare un Dae")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "DAE censito correttmanete", response = Dae.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response saveDae(@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse, @ApiParam(value = "Dae che si vuole censire") Dae dae)
			throws AppException {
		try {
			logger.info("EXECUTING saveDae");
			Utente utente = (Utente) secContext.getUserPrincipal();
			logger.info("EXECUTING saveDae FR ID: " + utente.getId());
			if (filterUtils.isEnabledToSave(utente, dae)) {

				Dae ret = daeDelegateService.insertDae(dae, utente, false);
				return Response.ok(processDaeImage(httpServletRequest, ret)).build();
			} else {
				GenericResponse ndr = new GenericResponse();
				ndr.setError(true);
				ndr.setMessage("Utente non autrizzato al salvataggio del DAE in quel Comune");
				return Response.ok(ndr).build();
			}
		} catch (DaeDuplicateException e) {
			logger.error("DaeDuplicateException ");
			DaeDuplicateResponse bean = new DaeDuplicateResponse();

			bean.setDae((List<Dae>) dtoService.convertCollection(e.getDae(), Dae.class,
					new CompoundDTORule(DaeDO.class, Dae.class, DaeMinimalDepthRule.NAME)));

			bean.setMessage("Dae già presente sul database");

			return Response.ok(bean).build();

		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING saveDae", e);
			return Response.serverError().build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("deleteDAE")
	@Secured
	@ApiOperation(value = "deleteDAE", notes = "Metodo che consente di eliminare logicamente un DAE")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "DAE censito correttmanete", response = GenericResponse.class),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	public Response deleteDAE(@ApiParam(value = "Dae che si vuole eliminare") Dae dae) throws AppException {
		try {
			Utente utente = (Utente) secContext.getUserPrincipal();

			boolean ret = daeDelegateService.deleteLogicallyDae(dae, utente);
			GenericResponse ndr = new GenericResponse();
			ndr.setResponse(ret);
			return Response.ok(ndr).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING deleteDAE", e);
			GenericResponse ndr = new GenericResponse();
			ndr.setResponse(false);
			return Response.ok(ndr).build();

		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("uploadImmagine/{id}")
	@Secured
	public Response uploadImmagine(@PathParam(value = "id") String id, @Context HttpServletRequest request,
			@Context HttpServletResponse httpServletResponse) throws AppException {
		try {
			String encode = ImageUtils.extractAndEncode(request, 1000, 1000);

			daeDelegateService.updateImageDae(id, encode);
			ImageUtils.cleanImage(request, id);

			return Response.ok().build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING uploadImmagine", e);
			return Response.serverError().build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("getProgrammaHistory/{id}")
	@Secured
	public Response getProgrammaHistory(@PathParam(value = "id") String id) throws AppException {
		try {
			List<ProgrammaManutenzioneHistory> hists = daeDelegateService.listProgrammaManutenzioneHistory(id);

			return Response.ok(hists).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING uploadImmagine", e);
			return Response.serverError().build();
		}
	}

}
