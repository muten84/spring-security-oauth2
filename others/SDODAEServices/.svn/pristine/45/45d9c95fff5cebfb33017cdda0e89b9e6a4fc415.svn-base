package it.eng.areas.ems.sdodaeservices.rest.exception;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class AppExceptionMapper implements ExceptionMapper<AppException> {

	@Context
	private HttpHeaders headers;

	Logger lgger = LoggerFactory.getLogger(AppExceptionMapper.class);

	public Response toResponse(AppException ex) {

		lgger.warn("AppExceptionMapper " + ex.getMessage());

		ErrorMessage er = new ErrorMessage(ex);
		Response ret = Response.status(ex.getStatus()).entity(er).type(MediaType.APPLICATION_JSON).build();
		return ret;
	}

}