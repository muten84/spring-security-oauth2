/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest.service;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @author Bifulco Luigi
 *
 */
@ApiModel(value = "ChangeDocumentStatusRequest", description = "Request for changing the document status. You should know the document id")
public class ChangeDocumentStatusRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7523201905549046105L;

	@ApiModelProperty(dataType = "String", value = "The document id, returned when inserted or fetched", required = true, name = "documentId")
	private String documentId;

	@ApiModelProperty(dataType = "String", allowableValues = "CREATED,SENT,OPENED,CLOSED", value = "The new document state", required = true, name = "newState")
	private String newState;

	/**
	 * @return the documentId
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * @param documentId
	 *            the documentId to set
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * @return the newState
	 */
	public String getNewState() {
		return newState;
	}

	/**
	 * @param newState
	 *            the newState to set
	 */
	public void setNewState(String newState) {
		this.newState = newState;
	}

}
