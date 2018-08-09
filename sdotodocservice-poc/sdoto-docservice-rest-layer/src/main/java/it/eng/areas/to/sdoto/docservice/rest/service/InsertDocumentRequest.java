/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.rest.service;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Bifulco Luigi
 *
 */
@ApiModel(value = "InsertDocumentRequest", description = "Request for insert a document associated to a parking")
@JsonAutoDetect(getterVisibility = Visibility.ANY, fieldVisibility = Visibility.ANY)
public class InsertDocumentRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8904825655305543505L;

	public InsertDocumentRequest() {

	}

	@ApiModelProperty(dataType = "String", value = "The parking or department assigned to the booking", required = true, name = "parking")
	@JsonProperty
	private String parking;

	@ApiModelProperty(dataType = "String", value = "The doc reference, usually the booking code", required = true, name = "docReference")
	@JsonProperty
	private String docReference;

	/**
	 * @return the parking
	 */
	public String getParking() {
		return parking;
	}

	/**
	 * @param parking
	 *            the parking to set
	 */
	public void setParking(String parking) {
		this.parking = parking;
	}

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

}
