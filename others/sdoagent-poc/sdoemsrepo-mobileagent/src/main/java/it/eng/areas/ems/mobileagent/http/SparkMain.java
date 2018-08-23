/**
 * 
 */
package it.eng.areas.ems.mobileagent.http;

import static spark.Spark.get;
import static spark.Spark.port;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;

/**
 * @author Bifulco Luigi
 *
 */
public class SparkMain {

	static ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) {
		Logger logger = Logger.getLogger(SparkMain.class);
		SparkUtils.createServerWithRequestLog(logger);
		port(8888);
		get("/artifact", (request, response) -> {
			response.body(mapper.writeValueAsString(new ArtifactInfo()));
			response.header("Content-type", "application/json");
			return response.body();
		});
	}

}
