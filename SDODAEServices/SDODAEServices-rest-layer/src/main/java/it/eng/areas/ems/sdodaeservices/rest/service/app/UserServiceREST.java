package it.eng.areas.ems.sdodaeservices.rest.service.app;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.eng.areas.ems.sdodaeservices.delegate.AnagraficheDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.UserDelegateService;
import it.eng.areas.ems.sdodaeservices.delegate.model.GenericResponse;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.exception.OldPasswordNotValidException;
import it.eng.areas.ems.sdodaeservices.delegate.model.exception.PasswordAlreadyUsedException;
import it.eng.areas.ems.sdodaeservices.delegate.model.exception.PasswordWithUserDetailsException;
import it.eng.areas.ems.sdodaeservices.entity.ParametersEnum;
import it.eng.areas.ems.sdodaeservices.rest.model.ChangePassword;
import it.eng.areas.ems.sdodaeservices.rest.security.annotation.Secured;
import it.eng.areas.ems.sdodaeservices.utils.PasswordUtils;

@Component
@Path("/userService")
@Api(value = "/userService", protocols = "http", description = "Servizio dedicato alla gestione degli utenti")
@RolesAllowed("USER")
public class UserServiceREST {

	private Logger logger = LoggerFactory.getLogger(UserServiceREST.class);

	@Context
	protected SecurityContext secContext;

	@Autowired
	private AnagraficheDelegateService anagraficheService;

	@Autowired
	protected UserDelegateService userDelegateService;

	@Autowired
	protected PasswordUtils passwordUtils;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("changePassword")
	@ApiOperation(value = "APPLICATION_JSON", notes = "Metodo che consente all'utente di cambiare la propria password")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione eseguita correttamente"),
			@ApiResponse(code = 500, message = "Errore durante l'elaborazione") })
	@Secured
	public Response changePassword(ChangePassword passwordBean) {
		try {
			// carico l'utente loggato
			Utente u = (Utente) secContext.getUserPrincipal();

			Boolean enablePrivacy = Boolean
					.valueOf(anagraficheService.getParameter(ParametersEnum.ENABLE_PRIVACY.name()));

			final String regExpPasswd = anagraficheService.getParameter(ParametersEnum.REGEXP_PWD.name());
			final String regExpPasswdFourChar = anagraficheService
					.getParameter(ParametersEnum.REGEXP_PWD_FOUR_CHAR.name());
			final String regExpPasswdDoubleCharMax = anagraficheService
					.getParameter(ParametersEnum.REGEXP_PWD_DOUBLE_CHAR_MAX.name());

			boolean checkPwd;

			if (enablePrivacy) {
				checkPwd = passwordBean.getNewPassword().matches(regExpPasswd)
						&& passwordBean.getNewPassword().matches(regExpPasswdFourChar)
						&& passwordBean.getNewPassword().matches(regExpPasswdDoubleCharMax);
			} else {
				checkPwd = passwordUtils.checkStrength(passwordBean.getNewPassword());
			}

			if (checkPwd) {

				boolean result = userDelegateService.changePassword(u.getId(), passwordBean.getOldPassword(),
						passwordBean.getNewPassword());

				GenericResponse resp = new GenericResponse();
				resp.setResponse(result);

				return Response.ok(resp).build();
			} else {
				if (enablePrivacy) {
					GenericResponse message = new GenericResponse(
							"La password fornita non soddisfa i requisiti minimi di complessità. Selezionare un'altra password che soddisfi tutti i seguenti criteri: Non include dati dell'incaricato; Non contiene password recenti; Contiene da 8 a 32 caratteri; Contiene tutti i seguenti caratteri: Lettere maiuscole (A-Z); Lettere minuscole (a-z); Numeri (0-9); Caratteri non alfanumerici (ad esempio: !,$,#,%)",
							false);
					return Response.ok(message).build();
				} else {
					GenericResponse message = new GenericResponse(
							"Password utilizzata troppo debole. Inserire caratteri tipo (1;R;r;&;%;$)", false);
					return Response.ok(message).build();
				}
			}
		} catch (PasswordWithUserDetailsException e) {
			GenericResponse message = new GenericResponse(
					"La password fornita non soddisfa i requisiti minimi di complessità. Selezionare un'altra password che soddisfi tutti i seguenti criteri: Non include dati dell'incaricato; Non contiene password recenti; Contiene da 8 a 32 caratteri; Contiene tutti i seguenti caratteri: Lettere maiuscole (A-Z); Lettere minuscole (a-z); Numeri (0-9); Caratteri non alfanumerici (ad esempio: !,$,#,%)",
					false);
			return Response.ok(message).build();
		} catch (OldPasswordNotValidException e) {
			GenericResponse message = new GenericResponse("Vecchia password non valida", false);
			return Response.ok(message).build();
		} catch (PasswordAlreadyUsedException e) {
			GenericResponse message = new GenericResponse("Password già utilizzata in passato", false);
			return Response.ok(message).build();
		} catch (Exception e) {
			logger.error("ERROR WHILE EXECUTING changePassword", e);
			return Response.serverError().build();
		}
	}

}
