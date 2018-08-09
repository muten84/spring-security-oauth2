/**
 * 
 */
package it.eng.areas.ems.mobileagent.http;

import org.apache.log4j.Logger;

import spark.embeddedserver.EmbeddedServers;
import spark.embeddedserver.jetty.EmbeddedJettyFactory;

/**
 * @author Bifulco Luigi
 *
 */
public class SparkUtils {
	public static void createServerWithRequestLog(Logger logger) {
		EmbeddedJettyFactory factory = createEmbeddedJettyFactoryWithRequestLog(logger);
		EmbeddedServers.add(EmbeddedServers.Identifiers.JETTY, factory);
	}

	private static EmbeddedJettyFactory createEmbeddedJettyFactoryWithRequestLog(org.apache.log4j.Logger logger) {
		// AbstractNCSARequestLog requestLog = new
		return new EmbeddedJettyFactoryConstructor(null).create();
	}
}
