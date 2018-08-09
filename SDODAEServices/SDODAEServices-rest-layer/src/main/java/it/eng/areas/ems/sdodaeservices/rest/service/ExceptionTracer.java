package it.eng.areas.ems.sdodaeservices.rest.service;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ExceptionTracer implements ExceptionMapper<Exception> {

	private Logger logger = LoggerFactory.getLogger(ExceptionTracer.class);

	@Override
	public Response toResponse(Exception ex) {
		logger.error("ExceptionTracer - Error intercepted ", ex);
		return Response.status(500).entity(ex.getMessage()).type("text/plain").build();
	}

}
