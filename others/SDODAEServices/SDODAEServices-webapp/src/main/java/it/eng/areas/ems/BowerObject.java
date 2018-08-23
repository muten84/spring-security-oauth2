/**
 * 
 */
package it.eng.areas.ems;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Bifulco Luigi
 *
 */
@JsonIgnoreProperties
public class BowerObject {

	private Map<String, String> dependencies;

	/**
	 * @return the dependencies
	 */
	public Map<String, String> getDependencies() {
		return dependencies;
	}

	/**
	 * @param dependencies
	 *            the dependencies to set
	 */
	public void setDependencies(Map<String, String> dependencies) {
		this.dependencies = dependencies;
	}

}
