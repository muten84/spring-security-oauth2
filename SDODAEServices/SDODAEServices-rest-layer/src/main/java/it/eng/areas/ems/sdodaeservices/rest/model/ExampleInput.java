/**
 * 
 */
package it.eng.areas.ems.sdodaeservices.rest.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "ExampleInputRRequest", description = "Request example input")
public class ExampleInput implements Serializable {
	
	private String field1;
	
	private String field2;
	
	public ExampleInput(){
		
	}
	
	public String getField1(){
		return this.field1;
	}
	
	public String getField2(){
		return this.field2;
	}
	
	public void setField1(String f1){
		this.field1 = f1;
	}
	
	public void setField2(String f2){
		this.field2 = f2;
	}
}
