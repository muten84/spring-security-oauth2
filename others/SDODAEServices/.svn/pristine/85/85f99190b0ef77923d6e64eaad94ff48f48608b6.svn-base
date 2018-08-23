package it.eng.areas.ems.sdodaeservices.rest.exception;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.areas.ems.sdodaeservices.delegate.model.GenericException;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<GenericException> {

	@Context
	private HttpHeaders headers;

	Logger lgger = LoggerFactory.getLogger(GenericExceptionMapper.class);

	public Response toResponse(GenericException ex) {

		lgger.warn("GenericException " + ex.getMessage());

		Response ret = Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex).type(MediaType.APPLICATION_JSON)
				.build();
		return ret;
	}

}