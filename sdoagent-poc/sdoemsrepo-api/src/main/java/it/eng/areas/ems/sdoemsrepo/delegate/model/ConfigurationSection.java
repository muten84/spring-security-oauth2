/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.jsondb.annotation.Document;

/**
 * @author Bifulco Luigi
 *
 */
@Document(collection = "configurations", schemaVersion = "1.0")
public class ConfigurationSection {

	@io.jsondb.annotation.Id
	private String name;

	@JsonProperty("Parameter")
	private List<ConfigurationParameter> Parameter;

	@JsonProperty("Section")
	private List<ConfigurationSection> Section;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parameter
	 */
	public List<ConfigurationParameter> getParameter() {
		return Parameter;
	}

	/**
	 * @param parameter
	 *            the parameter to set
	 */
	public void setParameter(List<ConfigurationParameter> parameter) {
		Parameter = parameter;
	}

	/**
	 * @return the section
	 */
	public List<ConfigurationSection> getSection() {
		return Section;
	}

	/**
	 * @param section
	 *            the section to set
	 */
	public void setSection(List<ConfigurationSection> section) {
		Section = section;
	}

}
