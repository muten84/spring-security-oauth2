package it.eng.areas.ems.sdodaeservices.delegate.model;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "DaeImageUpload", description = "Bean che consente di caricare un'immagine (Base64) ed associarlo ad un DAE")
public class DaeImageUpload {

	private String daeID;
	
	private String base64Image;
	
	public DaeImageUpload() {
		// TODO Auto-generated constructor stub
	}

	public String getDaeID() {
		return daeID;
	}

	public void setDaeID(String daeID) {
		this.daeID = daeID;
	}

	public String getBase64Image() {
		return base64Image;
	}

	public void setBase64Image(String base64Image) {
		this.base64Image = base64Image;
	}
	
	
	
}
