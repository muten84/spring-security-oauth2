/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Bifulco Luigi
 *
 */
public class JsonMapper {

	public ObjectMapper mapper = new ObjectMapper();

	public <E> String stringify(E e) throws JsonProcessingException {
		return mapper.writeValueAsString(e);
	}

	public <E> E parse(String json, Class<E> valueType) throws IOException {
		return mapper.readValue(json.getBytes(), valueType);
	}
}
