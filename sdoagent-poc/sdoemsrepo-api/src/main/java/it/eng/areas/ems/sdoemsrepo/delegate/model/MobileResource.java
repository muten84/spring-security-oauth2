/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import java.util.Map;

import io.jsondb.annotation.Document;

/**
 * @author Bifulco Luigi
 *
 */
@Document(collection = "resources", schemaVersion = "1.0")
public class MobileResource {

	@io.jsondb.annotation.Id
	private String id;

	private String group;

	private String type;

	private String name;

	private String value;

	private Map<String, String> extra;

	public MobileResource() {

	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the extra
	 */
	public Map<String, String> getExtra() {
		return extra;
	}

	/**
	 * @param extra
	 *            the extra to set
	 */
	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

}
