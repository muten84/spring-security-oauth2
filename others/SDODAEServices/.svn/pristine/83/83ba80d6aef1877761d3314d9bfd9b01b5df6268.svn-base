package it.eng.areas.ems.sdodaeservices.rest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonMappingException;

@Provider
public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {

	Logger lgger = LoggerFactory.getLogger(JsonMappingExceptionMapper.class);

	@Override
	public Response toResponse(JsonMappingException exception) {
		lgger.warn(
				"JsonMappingExceptionMapper - This is an invalid request. At least one field format is not readable by the system. - "
						+ exception.getMessage());

		return Response.status(Response.Status.BAD_REQUEST)
				.entity("This is an invalid request. At least one field format is not readable by the system.")
				.type(MediaType.TEXT_PLAIN).build();
	}

}