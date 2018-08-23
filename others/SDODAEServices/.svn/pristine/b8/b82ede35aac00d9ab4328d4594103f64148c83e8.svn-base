package it.eng.areas.ems.sdodaeservices.rest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class UmbrellaExceptionMapper implements ExceptionMapper<Throwable> {

	Logger lgger = LoggerFactory.getLogger(UmbrellaExceptionMapper.class);

	@Override
	public Response toResponse(Throwable ex) {

		lgger.warn("UmbrellaException " + ex.getMessage());

		return Response.status(Response.Status.BAD_REQUEST)
				.entity("This is an invalid json. The request can not be parsed - ").type(MediaType.TEXT_PLAIN).build();
	}
}