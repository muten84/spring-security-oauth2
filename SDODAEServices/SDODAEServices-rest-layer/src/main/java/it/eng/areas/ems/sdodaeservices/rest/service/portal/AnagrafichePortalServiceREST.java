package it.eng.areas.ems.sdodaeservices.rest.service.portal;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import it.eng.areas.ems.sdodaeservices.rest.service.app.AnagraficheServiceREST;

@Component
@Path("/portal/anagraficheService")
@Api(value = "/portal/anagraficheService", protocols = "http", description = "Servizio dedicato alla gestione delle Anagrafiche")
public class AnagrafichePortalServiceREST extends AnagraficheServiceREST {

}
