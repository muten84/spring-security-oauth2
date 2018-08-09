/**
 * 
 */
package it.eng.areas.ems.mobileagent.message;

/**
 * @author Bifulco Luigi
 *
 */
public interface RequestHandler<Request, Response> {

	Response handle(Request m) throws Exception;
}
