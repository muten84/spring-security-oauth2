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
@ApiModel(value = "NotifyDocumentRequest", description = "Request for send a notification to a receiver knowing its reference and the docReference")
public class NotifyDocumentRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -128643298311285494L;

	@ApiModelProperty(dataType = "String", value = "A document reference if exists", required = false, name = "docReference")
	private String docReference;

	@ApiModelProperty(dataType = "String", value = "A receiver reference, department description or parking code", required = true, name = "receiverReference")
	private String receiverReference;

	/**
	 * @return the docReference
	 */
	public String getDocReference() {
		return docReference;
	}

	/**
	 * @param docReference
	 *            the docReference to set
	 */
	public void setDocReference(String docReference) {
		this.docReference = docReference;
	}

	/**
	 * @return the receiverReference
	 */
	public String getReceiverReference() {
		return receiverReference;
	}

	/**
	 * @param receiverReference
	 *            the receiverReference to set
	 */
	public void setReceiverReference(String receiverReference) {
		this.receiverReference = receiverReference;
	}

}
