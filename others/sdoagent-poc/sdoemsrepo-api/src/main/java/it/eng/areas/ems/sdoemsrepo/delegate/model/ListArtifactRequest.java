/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.model;

import java.io.Serializable;

/**
 * @author Bifulco Luigi
 *
 */
public class ListArtifactRequest implements Serializable {

	private String groupId;

	private String type;

	public ListArtifactRequest() {
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 773114287489948513L;

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
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

}
