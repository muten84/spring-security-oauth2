/**
 * 
 */
package it.eng.areas.ems.mobileagent.db;

import java.util.List;

/**
 * @author Bifulco Luigi
 *
 */
public interface LookupService {

	public List lookup(String pattern);

	public LookupViewer getViewer();

	public List getAll();

}