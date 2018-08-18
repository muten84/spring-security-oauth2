package it.luigibifulco.oauth2.poc.jpa.domain.excpetion;

public class ResourceNotFoundException extends RuntimeException{
	
	
	private String type;
	
	public ResourceNotFoundException(String type,String message) {
		super(message);
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
