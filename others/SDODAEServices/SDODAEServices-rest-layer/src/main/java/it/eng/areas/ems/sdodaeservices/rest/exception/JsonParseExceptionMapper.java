package it.eng.areas.ems.sdodaeservices.rest.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;

@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<JsonParseException> {

	Logger lgger = LoggerFactory.getLogger(JsonParseExceptionMapper.class);

	@Override
	public Response toResponse(JsonParseException exception) {

		lgger.warn("JsonParseException - This is an invalid json. The request can not be parsed - "
				+ exception.getMessage());

		return Response.status(Response.Status.BAD_REQUEST)
				.entity("This is an invalid json. The request can not be parsed").type(MediaType.APPLICATION_JSON)
				.build();
	}

}