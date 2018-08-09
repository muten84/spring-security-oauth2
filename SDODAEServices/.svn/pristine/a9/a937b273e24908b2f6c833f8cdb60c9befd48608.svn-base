package it.eng.areas.ems.sdodaeservices.rest.listener;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;

public class DAERequestEventListener implements RequestEventListener, ContainerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(DAERequestEventListener.class);

	private volatile long startTime = System.currentTimeMillis();

	@Context
	private HttpServletRequest sr;

	@Context
	private SecurityContext secContext;

	@Override
	public void onEvent(RequestEvent event) {
		try {
			switch (event.getType()) {
			case RESOURCE_METHOD_START:
				// spostato nel metodo sotto, aggiunto pure l'ip
				break;
			case FINISHED:
				logger.info("End call method:  " + event.getUriInfo().getMatchedResourceMethod().getHttpMethod() + " "
						+ event.getUriInfo().getPath() + " " + " Processing time "
						+ (System.currentTimeMillis() - startTime) + " ms.");
				break;
			}
		} catch (Exception e) {
			logger.error("error", e);
		}
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		try {
			startTime = System.currentTimeMillis();
			// prendo l'ip dall'header
			String ip = sr.getHeader("X-Forwarded-For");
			// se l'ip non è presente prendo quello passato dalla rete
			ip = ip != null ? ip : sr.getRemoteAddr();
			// utente loggato
			Utente user = (Utente) secContext.getUserPrincipal();
			String userLogon = user != null ? user.getLogon() : "";

			logger.info("Start call method: [" + userLogon + "] [" + ip + "] " + requestContext.getUriInfo().getPath());
		} catch (Exception e) {
			logger.error("error", e);
		}
	}
}
