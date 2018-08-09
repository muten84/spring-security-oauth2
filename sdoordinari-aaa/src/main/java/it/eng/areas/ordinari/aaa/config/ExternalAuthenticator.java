/**
 * 
 */
package it.eng.areas.ordinari.aaa.config;

import it.eng.areas.ordinari.aaa.model.AuthenticationWithTokenAndSession;

/**
 * @author Bifulco Luigi
 *
 */
public interface ExternalAuthenticator {

    AuthenticationWithTokenAndSession authenticate(String username, String password);
}
