package it.eng.areas.ems.sdodaeservices.rest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@Provider
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {

	Logger lgger = LoggerFactory.getLogger(UnrecognizedPropertyExceptionMapper.class);

	@Override
	public Response toResponse(UnrecognizedPropertyException exception) {

		lgger.warn("UnrecognizedPropertyExceptionMapper - This is an invalid request. The field "
				+ exception.getPropertyName() + " is not recognized by the system. - " + exception.getMessage());

		return Response
				.status(Response.Status.BAD_REQUEST).entity("This is an invalid request. The field "
						+ exception.getPropertyName() + " is not recognized by the system.")
				.type(MediaType.TEXT_PLAIN).build();
	}

}