package it.eng.areas.sdoordinari.ordinarigto.converter.impl;

import it.eng.areas.sdoordinari.ordinarigto.converter.ConditionalValCustomConverter;
import it.eng.areas.sdoordinari.ordinarigto.converter.ReplaceValCustomConverter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConditionalValCustomConverterImpl implements ConditionalValCustomConverter {

	String parameter;
	@Autowired
	private ReplaceValCustomConverter replaceValCustomConverter;
	
	
	Map<String, String> keyval = null;
			

	
	
	@Override
	public Object convert(Object existingDestinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		
		
		String val= null;
		String key ="";
		if(sourceFieldValue != null){
			key=sourceFieldValue.toString();
				
		}		
		
		String[] paramAsVector = parameter.split(",");  
		
		if (paramAsVector[0].equals(key)){
			sourceClass = paramAsVector[1].getClass();
		}
		
		if (parameter.equals(key)){
			
			replaceValCustomConverter.convert(existingDestinationFieldValue, sourceFieldValue, destinationClass, sourceClass);
			
		}
			
		
		return val  ;
		
	}

	@Override
	public void setParameter(String parameter) {
		this.parameter = parameter;
		
	}
	

}

